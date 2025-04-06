package com.zyneonstudios.nexus.application.modules;

import java.util.ArrayList;
import java.util.Collections;

public abstract class NexusModule {

    public abstract String getModuleId();
    public abstract String getModuleName();
    public abstract String getModuleVersion();
    public abstract String getModuleOwner();
    public abstract String[] getModuleContributors();

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
}