package com.loopang.hub_service.infrastructure.client;

import com.loopang.hub_service.domain.inventory.service.dto.ItemData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "item-service")
public interface ItemFeignClient {

    @GetMapping("/api/items/{itemId}")
    ItemData getItem(@PathVariable("itemId") UUID itemId);
}
