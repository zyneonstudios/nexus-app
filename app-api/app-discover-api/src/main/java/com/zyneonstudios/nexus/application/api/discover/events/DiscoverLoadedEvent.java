package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.Discover;

import java.util.UUID;

public abstract class DiscoverLoadedEvent implements DiscoverEvent {

    private final UUID uuid = UUID.randomUUID();

    @Override
    public DiscoverEventType getType() {
        return DiscoverEventType.DISCOVER_LOADED_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return postLoad();
    }

    public Discover getDiscover() {
        return DiscoverAPI.getDiscover();
    }

    public abstract boolean postLoad();
}
