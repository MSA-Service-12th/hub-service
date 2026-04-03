package com.loopang.hub_service.domain.hub.exception;

import com.loopang.common.exception.ForbiddenException;

public class MasterOnlyException extends ForbiddenException {

    public MasterOnlyException() {
        super("마스터 관리자만 수행할 수 있는 작업입니다.");
    }
}
