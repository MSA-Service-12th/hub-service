package com.loopang.hub_service.domain.hub.vo;

public enum HubStatus {

    NORMAL("정상"),
    BUSY("혼잡"),
    FULL("만원");

    private final String description;

    HubStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static HubStatus of(short capacity, short currentLoad) {
        if (capacity <= 0) return FULL;
        double ratio = (double) currentLoad / capacity;
        if (ratio >= 1.0) return FULL;
        if (ratio >= 0.7) return BUSY;
        return NORMAL;
    }
}
