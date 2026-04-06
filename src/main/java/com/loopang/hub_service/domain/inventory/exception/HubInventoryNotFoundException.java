package com.loopang.hub_service.domain.inventory.exception;

import com.loopang.common.exception.NotFoundException;

import java.util.UUID;

public class HubInventoryNotFoundException extends NotFoundException {

    public HubInventoryNotFoundException(UUID id) {
        super("허브재고를 찾을 수 없습니다. ID: " + id);
    }
}
