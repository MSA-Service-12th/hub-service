package com.loopang.hub_service.domain.hub.exception;

import com.loopang.common.exception.BadRequestException;

public class HubNameDuplicateException extends BadRequestException {

    public HubNameDuplicateException(String name) {
        super("이미 존재하는 허브 이름입니다: " + name, "name");
    }
}
