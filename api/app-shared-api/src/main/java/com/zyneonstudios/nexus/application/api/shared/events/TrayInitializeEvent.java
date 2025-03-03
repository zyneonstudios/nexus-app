package com.zyneonstudios.nexus.application.api.shared.events;

import java.util.UUID;

public abstract class TrayInitializeEvent implements ApplicationEvent {

    private final UUID uuid = UUID.randomUUID();

    //TODO get tray from main

    @Override
    public EventType getType() {
        return EventType.TRAY_INITIALIZE_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onInitialize();
    }

    public abstract boolean onInitialize();
}