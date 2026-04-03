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
        var address = hub.getAddress();
        return HubResponse.builder()
                .hubId(hub.getId())
                .name(hub.getName())
                .capacity(hub.getCapacity())
                .currentLoad(hub.getCurrentLoad())
                .status(hub.getStatus().getDescription())
                .cityDo(address != null ? address.getCityDo() : null)
                .guGun(address != null ? address.getGuGun() : null)
                .dongDoro(address != null ? address.getDongDoro() : null)
                .detailAddress(address != null ? address.getDetailAddress() : null)
                .fullAddress(address != null ? address.getFullAddress() : null)
                .latitude(address != null ? address.getLatitude() : null)
                .longitude(address != null ? address.getLongitude() : null)
                .createdAt(hub.getCreatedAt())
                .updatedAt(hub.getUpdatedAt())
                .build();
    }
}
