package com.loopang.hub_service.domain.inventory.service;

import com.loopang.hub_service.domain.inventory.service.dto.ItemData;

import java.util.UUID;

public interface ItemProvider {

    ItemData getItem(UUID itemId);
}
