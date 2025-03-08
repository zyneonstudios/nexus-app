package com.zyneonstudios.nexus.application.api.library.events;

import com.zyneonstudios.nexus.application.api.LibraryAPI;
import com.zyneonstudios.nexus.application.api.library.Library;

import java.util.Collection;
import java.util.UUID;

public abstract class LibrariesLoadEvent implements LibraryEvent {

    private final UUID uuid = UUID.randomUUID();
    private final Collection<Library> libraries = LibraryAPI.getLibraries().values();

    public LibrariesLoadEvent() {}

    @Override
    public LibraryEventType getType() {
        return LibraryEventType.LIBRARIES_LOAD_EVENT;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onLoad();
    }

    public Collection<Library> getLibraries() {
        return libraries;
    }

    public abstract boolean onLoad();
}
