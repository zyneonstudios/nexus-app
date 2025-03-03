package com.zyneonstudios.nexus.application.api.modules;

import com.google.gson.JsonObject;

public abstract class ApplicationModule {

    private final String id;
    private final String name;
    private final String version;
    private final String[] authors;
    private final JsonObject meta;

    public ApplicationModule(String id, String name, String version, String[] authors, JsonObject meta) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.authors = authors;
        this.meta = meta;
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

    public JsonObject getMeta() {
        return meta;
    }

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();

}