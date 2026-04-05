package com.loopang.hub_service.presentation.hub;

import com.loopang.common.exception.ForbiddenException;
import com.loopang.common.response.CommonResponse;
import com.loopang.common.response.PageInfo;
import com.loopang.hub_service.application.hub.HubService;
import com.loopang.hub_service.presentation.hub.dto.request.HubCreateRequest;
import com.loopang.hub_service.presentation.hub.dto.request.HubUpdateRequest;
import com.loopang.hub_service.presentation.hub.dto.response.HubDeleteResponse;
import com.loopang.hub_service.presentation.hub.dto.response.HubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<HubResponse> createHub(
            @RequestHeader("X-User-Role") String userRole,
            @Valid @RequestBody HubCreateRequest request) {
        checkMasterOrHub(userRole);
        return CommonResponse.success(hubService.createHub(request), "허브가 생성되었습니다.");
    }

    @GetMapping("/{hubId}")
    public CommonResponse<HubResponse> getHub(@PathVariable UUID hubId) {
        return CommonResponse.success(hubService.getHub(hubId), "허브 조회에 성공했습니다.");
    }

    @GetMapping
    public CommonResponse<List<HubResponse>> getHubs(Pageable pageable) {
        Page<HubResponse> page = hubService.getHubs(pageable);
        return CommonResponse.success(page.getContent(), "허브 목록 조회에 성공했습니다.", PageInfo.from(page));
    }

    @PutMapping("/{hubId}")
    public CommonResponse<HubResponse> updateHub(
            @PathVariable UUID hubId,
            @RequestHeader("X-User-Role") String userRole,
            @Valid @RequestBody HubUpdateRequest request) {
        checkMasterOrHub(userRole);
        return CommonResponse.success(hubService.updateHub(hubId, request), "허브가 수정되었습니다.");
    }

    @DeleteMapping("/{hubId}")
    public CommonResponse<HubDeleteResponse> deleteHub(
            @PathVariable UUID hubId,
            @RequestHeader("X-User-Role") String userRole) {
        checkMasterOrHub(userRole);
        return CommonResponse.success(hubService.deleteHub(hubId), "허브 삭제에 성공했습니다.");
    }

    private void checkMasterOrHub(String userRole) {
        if (!"ROLE_MASTER".equals(userRole) && !"ROLE_HUB".equals(userRole)) {
            throw new ForbiddenException("마스터 관리자 또는 허브 관리자만 수행할 수 있는 작업입니다.");
        }
    }
}
