package com.loopang.hub_service.infrastructure.persistence;

import com.loopang.hub_service.domain.hub.entity.Hub;
import com.loopang.hub_service.domain.hub.repository.HubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID>, HubRepository {
}
