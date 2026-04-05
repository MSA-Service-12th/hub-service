package com.loopang.hub_service.domain.event;

import java.util.UUID;

public record HubStockUpdatedPayload(
        UUID orderId,
        UUID itemId,
        UUID hubId,
        int quantity,
        int balance,
        boolean success
) {}
