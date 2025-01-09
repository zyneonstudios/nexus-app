package com.zyneonstudios.nexus.application.api.discover.events;

import com.zyneonstudios.nexus.application.api.discover.search.Search;
import com.zyneonstudios.nexus.application.api.discover.search.SearchResult;

import java.util.UUID;

public abstract class DiscoverInstallEvent implements DiscoverEvent {

    private UUID uuid = UUID.randomUUID();
    private SearchResult result;

    public DiscoverInstallEvent(SearchResult result) {
        this.result = result;
        this.uuid = this.result.getUUID();
    }

    public DiscoverInstallEvent() {

    }

    public void bindToSearchResult(SearchResult result) {
        this.result = result;
        this.uuid = result.getUUID();
    }

    public Search getSource() {
        return result.getSource();
    }

    public String getId() {
        return result.getId();
    }

    public String getName() {
        return result.getName();
    }

    public String getVersion() {
        return result.getVersion();
    }

    public String[] getAuthors() {
        return result.getAuthors();
    }

    public String getSummary() {
        return result.getActions();
    }

    public String getMetaSummary() {
        return result.getMetaSummary();
    }

    public String getImageUrl() {
        return result.getImageUrl();
    }

    public String getDownloadUrl() {
        return result.getDownloadUrl();
    }

    public String getDetailsUrl() {
        return result.getDetailsUrl();
    }

    @Override
    public DiscoverEventType getType() {
        return DiscoverEventType.DISCOVER_INSTALL_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onInstall();
    }

    public abstract boolean onInstall();
}