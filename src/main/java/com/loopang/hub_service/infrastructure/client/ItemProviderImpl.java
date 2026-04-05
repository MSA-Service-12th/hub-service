package com.loopang.hub_service.infrastructure.client;

import com.loopang.hub_service.domain.inventory.service.ItemProvider;
import com.loopang.hub_service.domain.inventory.service.dto.ItemData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemProviderImpl implements ItemProvider {

    private final ItemFeignClient itemFeignClient;

    @Override
    public ItemData getItem(UUID itemId) {
        ItemData itemData = itemFeignClient.getItem(itemId);
        if (itemData == null) {
            throw new IllegalArgumentException("아이템을 찾을 수 없습니다. itemId: " + itemId);
        }
        return itemData;
    }
}
