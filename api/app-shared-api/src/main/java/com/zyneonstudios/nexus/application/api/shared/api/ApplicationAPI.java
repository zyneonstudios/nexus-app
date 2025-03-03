package com.zyneonstudios.nexus.application.api.shared.api;

import com.zyneonstudios.nexus.application.main.NexusApplication;

public interface ApplicationAPI {

    void load(NexusApplication application);
    default void enable() {}
    default void shutdown() {}
}