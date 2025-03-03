package com.zyneonstudios.nexus.application.api.library;

import com.zyneonstudios.nexus.application.api.LibraryAPI;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEventType;
import com.zyneonstudios.nexus.application.api.library.events.LibraryLoadEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryLoadedEvent;

import java.util.UUID;

public interface Library {

    String id = UUID.randomUUID().toString();

    default void load() {
        boolean loaded = true;
        for(LibraryEvent events : LibraryAPI.getEvents(LibraryEventType.LIBRARY_LOAD_EVENT)) {
            LibraryLoadEvent event = (LibraryLoadEvent)events;
            if(event.getLibrary()==this) {
                if(!event.execute()) {
                    loaded = false;
                }
            }
        }
        if(loaded) {
            postLoad();
        }
    }

    default void postLoad() {
        for(LibraryEvent events : LibraryAPI.getEvents(LibraryEventType.LIBRARY_LOADED_EVENT)) {
            LibraryLoadedEvent event = (LibraryLoadedEvent)events;
            if(event.getLibrary()==this) {
                event.execute();
            }
        }
    }

    default void reload() {
        boolean reload = true;
        for(LibraryEvent events : LibraryAPI.getEvents(LibraryEventType.LIBRARY_RELOAD_EVENT)) {
            LibraryLoadedEvent event = (LibraryLoadedEvent)events;
            if(event.getLibrary()==this) {
                if(!event.execute()) {
                    reload = false;
                }
            }
        }
        if(reload) {
            load();
        }
    }

    LibraryInstance[] getLibraryInstances();

    LibraryInstance getLibraryInstance(String id);

    void addLibraryInstance(LibraryInstance instance);

    void removeLibraryInstance(LibraryInstance instance);

    void removeLibraryInstance(String instanceID);

    default String getLibraryId() {
        return id;
    }
}