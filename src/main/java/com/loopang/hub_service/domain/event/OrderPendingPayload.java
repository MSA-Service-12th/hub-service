package com.loopang.hub_service.domain.event;

import java.util.UUID;

public record OrderPendingPayload(
        UUID orderId,
        UUID itemId,
        UUID hubId,
        int quantity
) {}
