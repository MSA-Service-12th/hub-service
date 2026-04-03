package com.loopang.hub_service.domain.hub.exception;

import com.loopang.common.exception.NotFoundException;

import java.util.UUID;

public class HubNotFoundException extends NotFoundException {

    public HubNotFoundException(UUID hubId) {
        super("허브를 찾을 수 없습니다. Hub ID: " + hubId);
    }
}
