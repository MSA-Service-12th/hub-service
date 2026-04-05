package com.loopang.hub_service.presentation.inventory.dto.response;

import com.loopang.hub_service.domain.inventory.entity.HubInventory;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HubInventoryResponse {

    private UUID hubInventoryId;
    private UUID hubId;
    private UUID itemId;
    private String itemName;
    private int quantity;
    private int reservedQuantity;

    public static HubInventoryResponse from(HubInventory inventory) {
        return HubInventoryResponse.builder()
                .hubInventoryId(inventory.getId())
                .hubId(inventory.getHubId())
                .itemId(inventory.getItemId())
                .itemName(inventory.getItemName())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .build();
    }
}
