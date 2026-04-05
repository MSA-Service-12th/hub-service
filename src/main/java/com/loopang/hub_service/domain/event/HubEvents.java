package com.loopang.hub_service.domain.event;

import com.loopang.hub_service.domain.hub.entity.Hub;

import java.util.UUID;

public interface HubEvents {

    void hubChanged(Hub hub);

    void stockUpdated(UUID orderId, UUID itemId, UUID hubId, int quantity, int balance, boolean success);
}
