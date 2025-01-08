package com.zyneonstudios.nexus.application.api.discover.search;

public class SearchResult {

    private String id;
    private String name;
    private String[] authors;
    private String summary;
    private String metaSummary;
    private String imageUrl;
    private String downloadUrl;
    private String detailsUrl;

    public SearchResult(String id, String name, String[] authors, String summary, String imageUrl, String downloadUrl, String detailsUrl) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.summary = summary;
        this.metaSummary = summary;
        this.imageUrl = imageUrl;
        this.downloadUrl = downloadUrl;
        this.detailsUrl = detailsUrl;
    }

    public SearchResult(String id, String name, String author, String summary, String imageUrl, String downloadUrl, String detailsUrl) {
        this.id = id;
        this.name = name;
        this.authors = new String[]{author};
        this.summary = summary;
        this.metaSummary = summary;
        this.imageUrl = imageUrl;
        this.downloadUrl = downloadUrl;
        this.detailsUrl = detailsUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
}
