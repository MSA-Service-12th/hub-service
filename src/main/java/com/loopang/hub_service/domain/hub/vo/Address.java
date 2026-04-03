package com.loopang.hub_service.domain.hub.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
        this.fullAddress = Objects.requireNonNull(fullAddress, "fullAddress는 필수입니다.");
        this.latitude = Objects.requireNonNull(latitude, "latitude는 필수입니다.");
        this.longitude = Objects.requireNonNull(longitude, "longitude는 필수입니다.");

        if (this.latitude < -90 || this.latitude > 90) {
            throw new IllegalArgumentException("latitude 범위는 -90~90이어야 합니다.");
        }
        if (this.longitude < -180 || this.longitude > 180) {
            throw new IllegalArgumentException("longitude 범위는 -180~180이어야 합니다.");
        }

        this.cityDo = cityDo;
        this.guGun = guGun;
        this.dongDoro = dongDoro;
        this.detailAddress = detailAddress;
    }
}
