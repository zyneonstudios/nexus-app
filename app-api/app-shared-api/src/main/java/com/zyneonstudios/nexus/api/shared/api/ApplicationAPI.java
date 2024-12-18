package com.zyneonstudios.nexus.api.shared.api;

public interface ApplicationAPI {

    default void load() {}
    default void enable() {}
    default void shutdown() {}
}