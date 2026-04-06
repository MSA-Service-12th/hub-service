package com.loopang.hub_service.domain.event;

import com.loopang.hub_service.domain.hub.entity.Hub;

import java.util.UUID;

/**
 * 허브 변경 이벤트 페이로드.
 *
 * <p>{@code hub-update-topic}으로 발행되며, company-service 등 구독자가
 * 자기 도메인의 hub 정보(이름) 동기화에 사용한다.</p>
 *
 * <p>필드 구성은 단비님(company-service) 측에서 정의한 {@code HubUpdatedEvent}와 동일.
 * JSON 키 기준으로 deserialize되므로 필드명을 그대로 맞춘다.</p>
 */
public record HubChangedPayload(
        UUID hubId,
        String hubName,
        UUID updatedBy
) {
    public static HubChangedPayload from(Hub hub, UUID updatedBy) {
        return new HubChangedPayload(
                hub.getId(),
                hub.getName(),
                updatedBy
        );
    }
}
