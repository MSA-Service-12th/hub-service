package com.loopang.hub_service.presentation.hub.dto.response;

import com.loopang.hub_service.domain.hub.entity.Hub;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class HubDeleteResponse {

    private UUID hubId;
    private LocalDateTime deletedAt;

    public static HubDeleteResponse from(Hub hub) {
        return HubDeleteResponse.builder()
                .hubId(hub.getId())
                .deletedAt(hub.getDeletedAt())
                .build();
    }
}
