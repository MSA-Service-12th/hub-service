package com.loopang.hub_service.domain.hub.repository;

import com.loopang.hub_service.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    Page<Hub> findAll(Pageable pageable);

    boolean existsByName(String name);
}
