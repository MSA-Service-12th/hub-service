package com.loopang.hub_service.presentation.hub;

import com.loopang.common.response.CommonResponse;
import com.loopang.common.response.PageInfo;
import com.loopang.hub_service.application.hub.HubService;
import com.loopang.hub_service.presentation.hub.dto.request.HubCreateRequest;
import com.loopang.hub_service.presentation.hub.dto.request.HubUpdateRequest;
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
    public CommonResponse<HubResponse> createHub(@Valid @RequestBody HubCreateRequest request) {
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
    public CommonResponse<HubResponse> updateHub(@PathVariable UUID hubId,
                                                  @RequestBody HubUpdateRequest request) {
        return CommonResponse.success(hubService.updateHub(hubId, request), "허브가 수정되었습니다.");
    }

    @DeleteMapping("/{hubId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
    }
}
