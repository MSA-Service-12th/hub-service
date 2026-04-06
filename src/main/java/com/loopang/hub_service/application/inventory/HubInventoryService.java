package com.loopang.hub_service.application.inventory;

import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.domain.hub.exception.HubNotFoundException;
import com.loopang.hub_service.domain.hub.repository.HubRepository;
import com.loopang.hub_service.domain.inventory.entity.HubInventory;
import com.loopang.hub_service.domain.inventory.exception.HubInventoryNotFoundException;
import com.loopang.hub_service.domain.inventory.repository.HubInventoryRepository;
import com.loopang.hub_service.domain.inventory.service.ItemProvider;
import com.loopang.hub_service.domain.inventory.service.dto.ItemData;
import com.loopang.hub_service.presentation.inventory.dto.request.HubInventoryCreateRequest;
import com.loopang.hub_service.presentation.inventory.dto.request.HubInventoryUpdateRequest;
import com.loopang.hub_service.presentation.inventory.dto.response.HubInventoryDeleteResponse;
import com.loopang.hub_service.presentation.inventory.dto.response.HubInventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubInventoryService {

    private final HubInventoryRepository hubInventoryRepository;
    private final HubRepository hubRepository;
    private final ItemProvider itemProvider;

    @Transactional
    public HubInventoryResponse create(HubInventoryCreateRequest request) {
        // 허브 존재 여부 검증 + 이름 스냅샷 — 재고 엔티티의 hub_name 컬럼이 null로 저장되지 않도록.
        Hub hub = hubRepository.findById(request.getHubId())
                .orElseThrow(() -> new HubNotFoundException(request.getHubId()));

        ItemData itemData = itemProvider.getItem(request.getItemId());

        HubInventory inventory = HubInventory.builder()
                .hubId(hub.getId())
                .hubName(hub.getName())
                .itemId(itemData.itemId())
                .itemName(itemData.itemName())
                .quantity(request.getQuantity())
                .reservedQuantity(request.getReservedQuantity() != null ? request.getReservedQuantity() : 0)
                .companyId(itemData.companyId())
                .companyName(itemData.companyName())
                .build();

        return HubInventoryResponse.from(hubInventoryRepository.save(inventory));
    }

    public HubInventoryResponse getInventory(UUID id) {
        return HubInventoryResponse.from(findById(id));
    }

    public Page<HubInventoryResponse> getInventories(Pageable pageable) {
        return hubInventoryRepository.findAll(pageable).map(HubInventoryResponse::from);
    }

    @Transactional
    public HubInventoryResponse updateInventory(UUID id, HubInventoryUpdateRequest request) {
        HubInventory inventory = findById(id);
        if (request.getQuantity() != null) {
            inventory.updateQuantity(request.getQuantity());
        }
        return HubInventoryResponse.from(inventory);
    }

    @Transactional
    public HubInventoryDeleteResponse deleteInventory(UUID id) {
        HubInventory inventory = findById(id);
        // TODO: SecurityUtil 연동 후 deletedBy 전달
        inventory.delete(null);
        return HubInventoryDeleteResponse.from(id);
    }

    private HubInventory findById(UUID id) {
        return hubInventoryRepository.findById(id)
                .orElseThrow(() -> new HubInventoryNotFoundException(id));
    }
}
