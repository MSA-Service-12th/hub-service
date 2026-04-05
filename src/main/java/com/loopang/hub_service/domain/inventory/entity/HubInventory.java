package com.loopang.hub_service.domain.inventory.entity;

import com.loopang.common.domain.BaseUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "p_hub_inventory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class HubInventory extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_inventory_id")
    private UUID id;

    @Version
    private int version;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "hub_name", length = 100)
    private String hubName;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Builder
    public HubInventory(UUID hubId, String hubName, UUID itemId, String itemName,
                        int quantity, int reservedQuantity, UUID companyId, String companyName) {
        if (hubId == null) throw new IllegalArgumentException("hubId는 필수입니다.");
        if (itemId == null) throw new IllegalArgumentException("itemId는 필수입니다.");
        if (companyId == null) throw new IllegalArgumentException("companyId는 필수입니다.");
        if (quantity < 0) throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");

        this.hubId = hubId;
        this.hubName = hubName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public void updateQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        this.quantity = quantity;
    }

    public void reduceStock(int amount) {
        if (amount < 1) throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        if (this.quantity < amount) throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.quantity);
        this.quantity -= amount;
    }

    public void addStock(int amount) {
        if (amount < 1) throw new IllegalArgumentException("추가 수량은 1 이상이어야 합니다.");
        this.quantity += amount;
    }

    public void restoreStock(int amount) {
        if (amount < 1) throw new IllegalArgumentException("복원 수량은 1 이상이어야 합니다.");
        this.quantity += amount;
    }
}
