package com.loopang.hub_service.presentation.hub.dto.request;

import jakarta.validation.constraints.AssertTrue;
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

    @AssertTrue(message = "주소 수정 시 위도와 경도는 필수입니다.")
    public boolean isAddressCoordinatePairValid() {
        return fullAddress == null || (latitude != null && longitude != null);
    }
}
