package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.Discover;

import java.util.UUID;

public abstract class DiscoverSearchEvent implements DiscoverEvent {

    private final UUID uuid = UUID.randomUUID();
    private String sourceId = null;
    private String query = null;
    private int offset = 0;

    public String getSourceId() {
        return sourceId;
    }

    public int getOffset() {
        return offset;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public DiscoverEventType getType() {
        return DiscoverEventType.DISCOVER_SEARCH_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Discover getDiscover() {
        return DiscoverAPI.getDiscover();
    }

    @Override
    public final boolean execute() {
        return onSearch();
    }

    public abstract boolean onSearch();
}