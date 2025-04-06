package com.zyneonstudios.nexus.application.events;

import com.zyneonstudios.nexus.utilities.events.Event;

import java.util.UUID;

public abstract class PageLoadedEvent implements Event {

    final UUID uuid = UUID.randomUUID();
    String url;

    public PageLoadedEvent(String page) {
        this.url = page;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(String url) {
        this.url = url;
    }

    @Override
    public final UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onLoad();
    }

    public abstract boolean onLoad();
}
