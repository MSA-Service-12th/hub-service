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
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name은 비어 있을 수 없습니다.");
        }
        if (capacity == null || capacity < 0) {
            throw new IllegalArgumentException("capacity는 0 이상이어야 합니다.");
        }
        if (address == null) {
            throw new IllegalArgumentException("address는 필수입니다.");
        }
        this.name = name;
        this.capacity = capacity;
        this.currentLoad = (short) 0;
        this.address = address;
    }

    public void update(String name, Short capacity, Address address) {
        if (name != null) {
            if (name.isBlank()) {
                throw new IllegalArgumentException("name은 비어 있을 수 없습니다.");
            }
            this.name = name;
        }
        if (capacity != null) {
            if (capacity < 0) {
                throw new IllegalArgumentException("capacity는 0 이상이어야 합니다.");
            }
            if (capacity < this.currentLoad) {
                throw new IllegalArgumentException(
                        "capacity는 현재 currentLoad(" + this.currentLoad + ")보다 작을 수 없습니다.");
            }
            this.capacity = capacity;
        }
        if (address != null) this.address = address;
    }

    public void updateLoad(Short currentLoad) {
        if (currentLoad == null || currentLoad < 0) {
            throw new IllegalArgumentException("currentLoad는 0 이상이어야 합니다.");
        }
        if (currentLoad > this.capacity) {
            throw new IllegalArgumentException("currentLoad는 capacity를 초과할 수 없습니다. capacity: " + this.capacity);
        }
        this.currentLoad = currentLoad;
    }

    public HubStatus getStatus() {
        return HubStatus.of(this.capacity, this.currentLoad);
    }
}
