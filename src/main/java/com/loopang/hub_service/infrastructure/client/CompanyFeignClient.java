package com.loopang.hub_service.infrastructure.client;

import com.loopang.hub_service.domain.inventory.service.dto.CompanyData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyFeignClient {

    @GetMapping("/api/companies/{companyId}")
    CompanyData getCompany(@PathVariable("companyId") UUID companyId);
}
