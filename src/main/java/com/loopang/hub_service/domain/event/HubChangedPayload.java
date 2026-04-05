package com.loopang.hub_service.domain.event;

import com.loopang.hub_service.domain.hub.entity.Hub;

import java.util.UUID;

public record HubChangedPayload(
        UUID hubId,
        String name,
        String fullAddress,
        Double latitude,
        Double longitude,
        boolean isDeleted
) {
    public static HubChangedPayload from(Hub hub) {
        return new HubChangedPayload(
                hub.getId(),
                hub.getName(),
                hub.getAddress() != null ? hub.getAddress().getFullAddress() : null,
                hub.getAddress() != null ? hub.getAddress().getLatitude() : null,
                hub.getAddress() != null ? hub.getAddress().getLongitude() : null,
                hub.isDeleted()
        );
    }
}
