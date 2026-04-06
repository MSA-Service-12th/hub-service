package com.loopang.hub_service.domain.event;

import com.loopang.hub_service.domain.hub.entity.Hub;

import java.util.UUID;

public interface HubEvents {

    /**
     * 허브 변경 이벤트 발행.
     * @param hub 변경된 허브
     * @param updatedBy 변경 주체 (마스터 관리자 등) — SecurityUtil 미연동 시 null 가능
     */
    void hubChanged(Hub hub, UUID updatedBy);

    void stockUpdated(UUID orderId, UUID itemId, UUID hubId, int quantity, int balance, boolean success);
}
