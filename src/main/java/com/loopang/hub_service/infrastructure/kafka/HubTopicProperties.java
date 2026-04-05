package com.loopang.hub_service.infrastructure.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "topics.hub")
public record HubTopicProperties(
        String updated,
        String stockUpdated
) {}
