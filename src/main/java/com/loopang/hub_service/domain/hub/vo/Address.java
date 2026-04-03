package com.loopang.hub_service.domain.hub.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(length = 50)
    private String cityDo;

    @Column(length = 50)
    private String guGun;

    @Column(length = 50)
    private String dongDoro;

    @Column(length = 100)
    private String detailAddress;

    @Column(nullable = false, length = 300)
    private String fullAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    public Address(String cityDo, String guGun, String dongDoro,
                   String detailAddress, String fullAddress,
                   Double latitude, Double longitude) {
        this.cityDo = cityDo;
        this.guGun = guGun;
        this.dongDoro = dongDoro;
        this.detailAddress = detailAddress;
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
