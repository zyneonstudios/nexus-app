package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.Discover;

import java.util.UUID;

public abstract class DiscoverOpenSearchEvent implements DiscoverEvent {

    private final UUID uuid = UUID.randomUUID();

    @Override
    public DiscoverEventType getType() {
        return DiscoverEventType.DISCOVER_OPEN_SEARCH_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onOpen();
    }

    public Discover getDiscover() {
        return DiscoverAPI.getDiscover();
    }

    public abstract boolean onOpen();
}