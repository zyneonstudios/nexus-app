package com.zyneonstudios.nexus.application.api.library.events;

import com.zyneonstudios.nexus.application.api.library.Library;

import java.util.UUID;

public abstract class LibraryLoadedEvent implements LibraryEvent {

    private final UUID uuid = UUID.randomUUID();
    private final Library library;

    public LibraryLoadedEvent(Library library) {
        this.library = library;
    }

    @Override
    public LibraryEventType getType() {
        return LibraryEventType.LIBRARY_LOADED_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return postLoad();
    }

    public Library getLibrary() {
        return library;
    }

    public abstract boolean postLoad();
}
