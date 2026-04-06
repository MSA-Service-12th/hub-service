package com.loopang.hub_service.infrastructure.kafka.listener;

import com.loopang.common.messaging.IdempotentConsumer;
import com.loopang.common.util.JsonUtil;
import com.loopang.hub_service.domain.event.HubEvents;
import com.loopang.hub_service.domain.event.OrderPendingPayload;
import com.loopang.hub_service.domain.inventory.entity.HubInventory;
import com.loopang.hub_service.domain.inventory.repository.HubInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPendingListener {

    private final HubInventoryRepository hubInventoryRepository;
    private final HubEvents hubEvents;
    private final JsonUtil jsonUtil;

    @Transactional
    @IdempotentConsumer("hub-order-pending-group")
    @KafkaListener(topics = "${topics.order.pending}", groupId = "hub-group")
    public void onOrderPending(Message<String> message, Acknowledgment ack) {
        Object messageId = message.getHeaders().get(KafkaHeaders.RECEIVED_KEY);

        try {
            OrderPendingPayload payload = jsonUtil.fromJson(message.getPayload(), OrderPendingPayload.class);

            if (payload == null) {
                log.error("메시지 페이로드 역직렬화 실패 - messageId: {}", messageId);
                throw new IllegalArgumentException("OrderPendingPayload is null");
            }

            Optional<HubInventory> inventoryOpt = hubInventoryRepository.findByHubIdAndItemId(
                    payload.hubId(), payload.itemId());

            if (inventoryOpt.isEmpty()) {
                log.warn("재고 없음 - hubId: {}, itemId: {}", payload.hubId(), payload.itemId());
                hubEvents.stockUpdated(payload.orderId(), payload.itemId(), payload.hubId(),
                        payload.quantity(), 0, false);
                acknowledgeAfterCommit(ack);
                return;
            }

            HubInventory inventory = inventoryOpt.get();

            try {
                inventory.reduceStock(payload.quantity());
                log.info("재고 차감 성공 - orderId: {}, itemId: {}, 차감: {}, 잔여: {}",
                        payload.orderId(), payload.itemId(), payload.quantity(), inventory.getQuantity());

                hubEvents.stockUpdated(payload.orderId(), payload.itemId(), payload.hubId(),
                        payload.quantity(), inventory.getQuantity(), true);
            } catch (IllegalArgumentException e) {
                log.warn("재고 부족 - orderId: {}, itemId: {}, 요청: {}, 현재: {}",
                        payload.orderId(), payload.itemId(), payload.quantity(), inventory.getQuantity());

                hubEvents.stockUpdated(payload.orderId(), payload.itemId(), payload.hubId(),
                        payload.quantity(), inventory.getQuantity(), false);
            }

            acknowledgeAfterCommit(ack);
        } catch (Exception e) {
            log.error("주문 대기 메시지 처리 실패 - messageId: {}, error: {}", messageId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 트랜잭션이 실제로 커밋된 뒤에 Kafka 오프셋을 ack한다.
     * <p>트랜잭션 내부에서 직접 ack하면 커밋 전에 오프셋이 확정돼,
     * 아웃박스 저장 실패나 낙관적 잠금 예외로 롤백될 때 메시지 손실이 발생한다.</p>
     */
    private void acknowledgeAfterCommit(Acknowledgment ack) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    ack.acknowledge();
                }
            });
        } else {
            // 트랜잭션 컨텍스트가 없는 경우(예: 테스트)엔 즉시 ack
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = "${topics.order.pending}.DLT", groupId = "hub-group")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("DLT 수신 - 최종 처리 실패 메시지: {}", message.getPayload());
        ack.acknowledge();
    }
}
