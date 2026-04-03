package com.loopang.hub_service.presentation.hub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class HubCreateRequest {

    @NotBlank(message = "허브 이름은 필수입니다.")
    private String name;

    @NotNull(message = "용적은 필수입니다.")
    @Positive(message = "용적은 양수여야 합니다.")
    private Short capacity;

    private String cityDo;
    private String guGun;
    private String dongDoro;
    private String detailAddress;

    @NotBlank(message = "전체 주소는 필수입니다.")
    private String fullAddress;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;
}
