package com.zyneonstudios.nexus.application.api.library.events;

import com.zyneonstudios.nexus.application.api.library.Library;

import java.util.UUID;

public abstract class LibraryLoadEvent implements LibraryEvent {

    private final UUID uuid = UUID.randomUUID();
    private final Library library;

    public LibraryLoadEvent(Library library) {
        this.library = library;
    }

    @Override
    public LibraryEventType getType() {
        return LibraryEventType.LIBRARY_LOAD_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onLoad();
    }

    public Library getLibrary() {
        return library;
    }

    public abstract boolean onLoad();
}
