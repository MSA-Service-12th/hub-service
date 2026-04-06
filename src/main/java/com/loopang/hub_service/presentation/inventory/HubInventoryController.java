package com.loopang.hub_service.presentation.inventory;

import com.loopang.common.exception.ForbiddenException;
import com.loopang.common.response.CommonResponse;
import com.loopang.common.response.PageInfo;
import com.loopang.hub_service.application.inventory.HubInventoryService;
import com.loopang.hub_service.presentation.inventory.dto.request.HubInventoryCreateRequest;
import com.loopang.hub_service.presentation.inventory.dto.request.HubInventoryUpdateRequest;
import com.loopang.hub_service.presentation.inventory.dto.response.HubInventoryDeleteResponse;
import com.loopang.hub_service.presentation.inventory.dto.response.HubInventoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hub-inventories")
@RequiredArgsConstructor
public class HubInventoryController {

    private final HubInventoryService hubInventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<HubInventoryResponse> create(
            @RequestHeader("X-User-Role") String userRole,
            @Valid @RequestBody HubInventoryCreateRequest request) {
        checkMaster(userRole);
        return CommonResponse.success(hubInventoryService.create(request), "허브 재고 등록에 성공했습니다.");
    }

    @GetMapping("/{hubInventoryId}")
    public CommonResponse<HubInventoryResponse> getInventory(
            @PathVariable UUID hubInventoryId,
            @RequestHeader("X-User-Role") String userRole) {
        checkMaster(userRole);
        return CommonResponse.success(hubInventoryService.getInventory(hubInventoryId), "허브 재고 상세 조회에 성공했습니다.");
    }

    @GetMapping
    public CommonResponse<List<HubInventoryResponse>> getInventories(
            @RequestHeader("X-User-Role") String userRole,
            Pageable pageable) {
        checkMaster(userRole);
        Page<HubInventoryResponse> page = hubInventoryService.getInventories(pageable);
        return CommonResponse.success(page.getContent(), "허브 재고 목록 조회에 성공했습니다.", PageInfo.from(page));
    }

    @PatchMapping("/{hubInventoryId}")
    public CommonResponse<HubInventoryResponse> updateInventory(
            @PathVariable UUID hubInventoryId,
            @RequestHeader("X-User-Role") String userRole,
            @Valid @RequestBody HubInventoryUpdateRequest request) {
        checkMaster(userRole);
        return CommonResponse.success(hubInventoryService.updateInventory(hubInventoryId, request), "허브 재고 수정에 성공했습니다.");
    }

    @DeleteMapping("/{hubInventoryId}")
    public CommonResponse<HubInventoryDeleteResponse> deleteInventory(
            @PathVariable UUID hubInventoryId,
            @RequestHeader("X-User-Role") String userRole) {
        checkMaster(userRole);
        return CommonResponse.success(hubInventoryService.deleteInventory(hubInventoryId), "허브 재고 삭제에 성공했습니다.");
    }

    private void checkMaster(String userRole) {
        if (!"ROLE_MASTER".equals(userRole)) {
            throw new ForbiddenException("마스터 관리자만 수행할 수 있는 작업입니다.");
        }
    }
}
