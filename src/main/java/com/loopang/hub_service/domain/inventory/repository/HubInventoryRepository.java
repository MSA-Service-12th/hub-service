package com.loopang.hub_service.domain.inventory.repository;

import com.loopang.hub_service.domain.inventory.entity.HubInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubInventoryRepository {

    HubInventory save(HubInventory hubInventory);

    Optional<HubInventory> findById(UUID id);

    Page<HubInventory> findAll(Pageable pageable);

    Optional<HubInventory> findByHubIdAndItemId(UUID hubId, UUID itemId);
}
