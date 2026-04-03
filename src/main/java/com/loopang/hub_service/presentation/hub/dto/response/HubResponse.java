package com.loopang.hub_service.presentation.hub.dto.response;

import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.domain.hub.vo.HubStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class HubResponse {

    private UUID hubId;
    private String name;
    private Short capacity;
    private Short currentLoad;
    private String status;
    private String cityDo;
    private String guGun;
    private String dongDoro;
    private String detailAddress;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HubResponse from(Hub hub) {
        return HubResponse.builder()
                .hubId(hub.getId())
                .name(hub.getName())
                .capacity(hub.getCapacity())
                .currentLoad(hub.getCurrentLoad())
                .status(hub.getStatus().getDescription())
                .cityDo(hub.getAddress().getCityDo())
                .guGun(hub.getAddress().getGuGun())
                .dongDoro(hub.getAddress().getDongDoro())
                .detailAddress(hub.getAddress().getDetailAddress())
                .fullAddress(hub.getAddress().getFullAddress())
                .latitude(hub.getAddress().getLatitude())
                .longitude(hub.getAddress().getLongitude())
                .createdAt(hub.getCreatedAt())
                .updatedAt(hub.getUpdatedAt())
                .build();
    }
}
