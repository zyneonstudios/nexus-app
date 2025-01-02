package com.zyneonstudios.nexus.application.api.modules;

public class ApplicationModule {

    private final String id;
    private final String name;
    private final String version;
    private final String[] authors;

    public ApplicationModule(String moduleId, String moduleName, String moduleVersion, String... moduleAuthors) {
        id = moduleId;
        name = moduleName;
        version = moduleVersion;
        authors = moduleAuthors;
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

    public void onLoad() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}