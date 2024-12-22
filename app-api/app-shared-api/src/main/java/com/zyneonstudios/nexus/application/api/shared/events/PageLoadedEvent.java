package com.zyneonstudios.nexus.application.api.shared.events;

import java.util.UUID;

public abstract class PageLoadedEvent implements ApplicationEvent {

    private final UUID uuid = UUID.randomUUID();

    public PageLoadedEvent() {

    }

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
        return false;
    }

    protected abstract void onLoad();
}
