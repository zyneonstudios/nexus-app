package com.zyneonstudios.nexus.application.modules;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.Collections;

public abstract class NexusModule {

    private final NexusApplication application = Main.getApplication();
    private final String apiVersion;

    public NexusModule() {
        this.apiVersion = application.getVersion();
    }

    public abstract String getModuleId();
    public abstract String getModuleName();
    public abstract String getModuleVersion();
    public abstract String getModuleOwner();
    public abstract String[] getModuleContributors();
    public final String getApiVersion() {
        return apiVersion;
    }

    public abstract void onLoad();
    public abstract void onEnable();
    public abstract void onDisable();

    public String[] getModuleAuthors() {
        ArrayList<String> authors = new ArrayList<>();
        authors.add(getModuleOwner());
        if(getModuleContributors()!=null) {
            Collections.addAll(authors, getModuleContributors());
        }
        return authors.toArray(new String[0]);
    }

    protected NexusApplication getApplication() {
        return application;
    }
}