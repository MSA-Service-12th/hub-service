package com.loopang.hub_service.presentation.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubInventoryCreateRequest {

    @NotNull(message = "허브 ID는 필수입니다.")
    private UUID hubId;

    @NotNull(message = "아이템 ID는 필수입니다.")
    private UUID itemId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private Integer quantity;

    @Min(value = 0, message = "예약 재고는 0 이상이어야 합니다.")
    private Integer reservedQuantity;
}
