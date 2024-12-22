package com.zyneonstudios.nexus.application.api.library;

public interface LibraryInstance {

    void enableLaunch(boolean enable);

    boolean isLaunchEnabled();

    void launch();

    String getName();

    String getId();

    String getSummary();

    String getIconUrl();

    String getLogoUrl();

    String getBackgroundUrl();
}