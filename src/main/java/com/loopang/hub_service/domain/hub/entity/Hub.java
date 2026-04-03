package com.loopang.hub_service.domain.hub.entity;

import com.loopang.common.domain.BaseUserEntity;
import com.loopang.hub_service.domain.hub.vo.Address;
import com.loopang.hub_service.domain.hub.vo.HubStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "p_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Hub extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id")
    private UUID id;

    @Version
    private int version;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Short capacity;

    @Column(nullable = false)
    private Short currentLoad;

    @Embedded
    private Address address;

    @Builder
    public Hub(String name, Short capacity, Address address) {
        this.name = name;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.address = address;
    }

    public void update(String name, Short capacity, Address address) {
        if (name != null) this.name = name;
        if (capacity != null) this.capacity = capacity;
        if (address != null) this.address = address;
    }

    public void updateLoad(Short currentLoad) {
        this.currentLoad = currentLoad;
    }

    public HubStatus getStatus() {
        return HubStatus.of(this.capacity, this.currentLoad);
    }
}
