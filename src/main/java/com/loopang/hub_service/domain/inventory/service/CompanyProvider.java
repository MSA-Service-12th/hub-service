package com.loopang.hub_service.domain.inventory.service;

import com.loopang.hub_service.domain.inventory.service.dto.CompanyData;

import java.util.UUID;

public interface CompanyProvider {

    CompanyData getCompany(UUID companyId);
}
