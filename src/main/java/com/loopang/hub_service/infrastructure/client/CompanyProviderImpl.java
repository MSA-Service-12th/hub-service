package com.loopang.hub_service.infrastructure.client;

import com.loopang.hub_service.domain.inventory.service.CompanyProvider;
import com.loopang.hub_service.domain.inventory.service.dto.CompanyData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyProviderImpl implements CompanyProvider {

    private final CompanyFeignClient companyFeignClient;

    @Override
    public CompanyData getCompany(UUID companyId) {
        CompanyData companyData = companyFeignClient.getCompany(companyId);
        if (companyData == null) {
            throw new IllegalArgumentException("업체를 찾을 수 없습니다. companyId: " + companyId);
        }
        return companyData;
    }
}
