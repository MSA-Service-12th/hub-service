package com.loopang.hub_service.domain.inventory.service.dto;

import java.util.UUID;

public record ItemData(
        UUID itemId,
        String itemName,
        UUID companyId,
        String companyName
) {}
