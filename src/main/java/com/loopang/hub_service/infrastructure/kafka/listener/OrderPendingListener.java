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
                ack.acknowledge();
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

            ack.acknowledge();
        } catch (Exception e) {
            log.error("주문 대기 메시지 처리 실패 - messageId: {}, error: {}", messageId, e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "${topics.order.pending}.DLT", groupId = "hub-group")
    public void handleDLT(Message<String> message, Acknowledgment ack) {
        log.error("DLT 수신 - 최종 처리 실패 메시지: {}", message.getPayload());
        ack.acknowledge();
    }
}
