package com.zyneonstudios.nexus.application.api.shared.events;

import com.zyneonstudios.nexus.application.api.SharedAPI;

import java.util.UUID;

public abstract class PageLoadedEvent implements ApplicationEvent {

    private final UUID uuid = UUID.randomUUID();

    @Override
    public EventType getType() {
        return EventType.PAGE_LOADED_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onLoad();
    }

    public String getUrl() {
        return SharedAPI.getFrameUrl();
    }

    protected abstract boolean onLoad();
}
