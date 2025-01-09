package com.zyneonstudios.nexus.application.api.discover.search;

import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverInstallEvent;

import java.util.UUID;

public class SearchResult {

    private UUID uuid = UUID.randomUUID();
    private String actions = null;
    private final Search source;
    private DiscoverInstallEvent onInstall;

    private String id;
    private String version = null;
    private String name;
    private String[] authors;
    private String summary;
    private String metaSummary;
    private String imageUrl;
    private String downloadUrl;
    private String detailsUrl;

    public SearchResult(Search source, String id, String name, String[] authors, String summary, String imageUrl, String downloadUrl, String detailsUrl) {
        this.source = source;
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.summary = summary;
        this.metaSummary = summary;
        this.imageUrl = imageUrl;
        this.downloadUrl = downloadUrl;
        this.detailsUrl = detailsUrl;
    }

    public SearchResult(Search source, String id, String name, String author, String summary, String imageUrl, String downloadUrl, String detailsUrl) {
        this.source = source;
        this.id = id;
        this.name = name;
        this.authors = new String[]{author};
        this.summary = summary;
        this.metaSummary = summary;
        this.imageUrl = imageUrl;
        this.downloadUrl = downloadUrl;
        this.detailsUrl = detailsUrl;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getActions() {
        return actions;
    }

    public Search getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getSummary() {
        return summary;
    }

    public String getMetaSummary() {
        return metaSummary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setMetaSummary(String metaSummary) {
        this.metaSummary = metaSummary;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public void setInstallHandler(DiscoverInstallEvent onInstall) {
        onInstall.bindToSearchResult(this);
        this.onInstall = onInstall;
        DiscoverAPI.registerEvent(onInstall);
    }
}
