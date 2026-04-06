package com.loopang.hub_service.infrastructure.persistence;

import com.loopang.hub_service.domain.inventory.entity.HubInventory;
import com.loopang.hub_service.domain.inventory.repository.HubInventoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubInventoryRepository extends JpaRepository<HubInventory, UUID>, HubInventoryRepository {
}
