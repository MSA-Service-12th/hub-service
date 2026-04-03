package com.loopang.hub_service.presentation.hub.dto.request;

import lombok.Getter;

@Getter
public class HubUpdateRequest {

    private String name;
    private Short capacity;
    private String cityDo;
    private String guGun;
    private String dongDoro;
    private String detailAddress;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
}
