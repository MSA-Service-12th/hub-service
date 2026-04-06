package com.loopang.hub_service.application.hub;

import com.loopang.hub_service.domain.event.HubEvents;
import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.domain.hub.exception.HubNameDuplicateException;
import com.loopang.hub_service.domain.hub.exception.HubNotFoundException;
import com.loopang.hub_service.domain.hub.repository.HubRepository;
import com.loopang.hub_service.domain.hub.vo.Address;
import com.loopang.hub_service.presentation.hub.dto.request.HubCreateRequest;
import com.loopang.hub_service.presentation.hub.dto.request.HubUpdateRequest;
import com.loopang.hub_service.presentation.hub.dto.response.HubDeleteResponse;
import com.loopang.hub_service.presentation.hub.dto.response.HubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {

    private final HubRepository hubRepository;
    private final HubEvents hubEvents;

    @Transactional
    public HubResponse createHub(HubCreateRequest request) {
        if (hubRepository.existsByName(request.getName())) {
            throw new HubNameDuplicateException(request.getName());
        }

        Address address = Address.builder()
                .cityDo(request.getCityDo())
                .guGun(request.getGuGun())
                .dongDoro(request.getDongDoro())
                .detailAddress(request.getDetailAddress())
                .fullAddress(request.getFullAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Hub hub = Hub.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .address(address)
                .build();

        return HubResponse.from(hubRepository.save(hub));
    }

    public HubResponse getHub(UUID hubId) {
        Hub hub = findHubById(hubId);
        return HubResponse.from(hub);
    }

    public Page<HubResponse> getHubs(Pageable pageable) {
        return hubRepository.findAll(pageable).map(HubResponse::from);
    }

    @Transactional
    public HubResponse updateHub(UUID hubId, HubUpdateRequest request, UUID requesterId) {
        Hub hub = findHubById(hubId);

        if (request.getName() != null && !request.getName().equals(hub.getName())
                && hubRepository.existsByName(request.getName())) {
            throw new HubNameDuplicateException(request.getName());
        }

        Address address = null;
        if (request.getFullAddress() != null) {
            address = Address.builder()
                    .cityDo(request.getCityDo())
                    .guGun(request.getGuGun())
                    .dongDoro(request.getDongDoro())
                    .detailAddress(request.getDetailAddress())
                    .fullAddress(request.getFullAddress())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .build();
        }

        hub.update(request.getName(), request.getCapacity(), address);

        // 변경 이벤트 발행 (Outbox) — company-service 등 구독자가 hubName 동기화에 사용
        hubEvents.hubChanged(hub, requesterId);

        return HubResponse.from(hub);
    }

    @Transactional
    public HubDeleteResponse deleteHub(UUID hubId) {
        Hub hub = findHubById(hubId);
        // TODO: SecurityUtil 구현 후 현재 유저 ID 전달 — hub.delete(currentUserId)
        hub.delete(null);
        return HubDeleteResponse.from(hub);
    }

    private Hub findHubById(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new HubNotFoundException(hubId));
    }
}
