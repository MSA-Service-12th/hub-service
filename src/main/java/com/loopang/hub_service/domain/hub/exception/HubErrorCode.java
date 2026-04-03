package com.loopang.hub_service.domain.hub.exception;

import com.loopang.common.exception.ErrorCodeSpec;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HubErrorCode implements ErrorCodeSpec {

    HUB_NOT_FOUND("HUB_NOT_FOUND", HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다.", null),
    HUB_NAME_DUPLICATE("HUB_NAME_DUPLICATE", HttpStatus.BAD_REQUEST, "이미 존재하는 허브 이름입니다.", "name"),
    HUB_CAPACITY_EXCEEDED("HUB_CAPACITY_EXCEEDED", HttpStatus.BAD_REQUEST, "허브 용적을 초과했습니다.", "capacity"),
    MASTER_ONLY("MASTER_ONLY", HttpStatus.FORBIDDEN, "마스터 관리자만 수행할 수 있는 작업입니다.", null);

    private final String code;
    private final HttpStatus status;
    private final String message;
    private final String field;

    HubErrorCode(String code, HttpStatus status, String message, String field) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.field = field;
    }
}
