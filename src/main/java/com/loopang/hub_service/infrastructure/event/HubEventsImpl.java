package com.loopang.hub_service.infrastructure.event;

import com.loopang.common.event.Events;
import com.loopang.common.event.OutboxEvent;
import com.loopang.hub_service.domain.event.HubChangedPayload;
import com.loopang.hub_service.domain.event.HubEvents;
import com.loopang.hub_service.domain.event.HubStockUpdatedPayload;
import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.infrastructure.kafka.HubTopicProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(HubTopicProperties.class)
public class HubEventsImpl implements HubEvents {

    private final HubTopicProperties properties;

    @Override
    public void hubChanged(Hub hub, UUID updatedBy) {
        OutboxEvent event = OutboxEvent.withCorrelation(
                getTraceId(),
                "HUB",
                hub.getId(),
                properties.updated(),
                HubChangedPayload.from(hub, updatedBy)
        );
        Events.trigger(event);
    }

    @Override
    public void stockUpdated(UUID orderId, UUID itemId, UUID hubId, int quantity, int balance, boolean success) {
        HubStockUpdatedPayload payload = new HubStockUpdatedPayload(
                orderId, itemId, hubId, quantity, balance, success
        );
        OutboxEvent event = OutboxEvent.withCorrelation(
                getTraceId(),
                "HUB",
                hubId,
                properties.stockUpdated(),
                payload
        );
        Events.trigger(event);
    }

    private String getTraceId() {
        String traceId = MDC.get("traceId");
        return StringUtils.hasText(traceId) ? traceId : UUID.randomUUID().toString();
    }
}
