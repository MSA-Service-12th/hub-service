package com.loopang.hub_service.domain.inventory.service.dto;

import java.util.UUID;

public record CompanyData(
        UUID companyId,
        String companyName
) {}
