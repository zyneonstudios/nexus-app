package com.zyneonstudios.nexus.application.api.shared.events;

import com.zyneonstudios.nexus.application.frame.web.ApplicationFrame;

import java.util.UUID;

public abstract class PageLoadedEvent implements ApplicationEvent {

    private final UUID uuid = UUID.randomUUID();
    private final ApplicationFrame frame;

    public PageLoadedEvent(ApplicationFrame frame) {
        this.frame = frame;
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
        return onLoad();
    }

    public String getUrl() {
        return frame.getBrowser().getURL();
    }

    protected abstract boolean onLoad();
}
