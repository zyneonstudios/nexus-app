package com.zyneonstudios.nexus.application.api.library.events;

import com.zyneonstudios.nexus.application.api.library.Library;

import java.util.UUID;

public abstract class LibraryReloadEvent implements LibraryEvent {

    private final UUID uuid = UUID.randomUUID();
    private final Library library;

    public LibraryReloadEvent(Library library) {
        this.library = library;
    }

    @Override
    public LibraryEventType getType() {
        return LibraryEventType.LIBRARY_RELOAD_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean execute() {
        return onReload();
    }

    public Library getLibrary() {
        return library;
    }

    abstract boolean onReload();
}
