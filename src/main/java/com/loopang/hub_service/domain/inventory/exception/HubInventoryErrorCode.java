package com.loopang.hub_service.domain.inventory.exception;

import com.loopang.common.exception.ErrorCodeSpec;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HubInventoryErrorCode implements ErrorCodeSpec {

    INVENTORY_NOT_FOUND("INVENTORY_NOT_FOUND", HttpStatus.NOT_FOUND, "허브재고를 찾을 수 없습니다.", null),
    INSUFFICIENT_STOCK("INSUFFICIENT_STOCK", HttpStatus.BAD_REQUEST, "재고가 부족합니다.", "quantity"),
    INVALID_QUANTITY("INVALID_QUANTITY", HttpStatus.BAD_REQUEST, "수량이 유효하지 않습니다.", "quantity");

    private final String code;
    private final HttpStatus status;
    private final String message;
    private final String field;

    HubInventoryErrorCode(String code, HttpStatus status, String message, String field) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.field = field;
    }
}
