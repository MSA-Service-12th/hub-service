package com.loopang.hub_service.presentation.inventory.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HubInventoryDeleteResponse {

    private UUID hubInventoryId;

    public static HubInventoryDeleteResponse from(UUID id) {
        return HubInventoryDeleteResponse.builder()
                .hubInventoryId(id)
                .build();
    }
}
