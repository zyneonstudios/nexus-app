package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.library.Library;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEventType;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LibraryAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<LibraryEventType, ArrayList<LibraryEvent>> events = new HashMap<>();
    private static final HashMap<String, Library> libraries = new HashMap<>();
    private static Library activeLibrary = null;

    public LibraryAPI() {

    }

    @Override
    public void load(NexusApplication application) {
        this.application = application;
    }

    @Override
    public void enable() {

    }

    @Override
    public void shutdown() {

    }

    public static boolean addLibrary(Library library) {
        return addLibrary(library.getLibraryId(),library);
    }

    public static boolean addLibrary(UUID uuid, Library library) {
        return addLibrary(uuid.toString(),library);
    }

    public static boolean addLibrary(String libraryId, Library library) {
        if(!libraries.containsKey(libraryId)) {
            libraries.put(libraryId, library);
            return true;
        }
        NexusDesktop.getLogger().err("Couldn't add library to library list: ID already registered.");
        return false;
    }

    public static HashMap<String, Library> getLibraries() {
        return libraries;
    }

    public static void registerEvent(LibraryEvent event) {
        if(!events.containsKey(event.getType())) {
            events.put(event.getType(), new ArrayList<>());
        }
        events.get(event.getType()).add(event);
    }

    public static ArrayList<LibraryEvent> getEvents(LibraryEventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }

    public static Library getActiveLibrary() {
        return activeLibrary;
    }

    public static boolean setActiveLibrary(Library library) {
        if(libraries.containsKey(library.getLibraryId())) {
            activeLibrary = library;
            return true;
        }
        NexusDesktop.getLogger().err("Couldn't set active library to "+library.getLibraryId()+": Library not registered.");
        return false;
    }

    public static boolean setActiveLibrary(String libraryId) {
        if(libraries.containsKey(libraryId)) {
            activeLibrary = libraries.get(libraryId);
            return true;
        }
        NexusDesktop.getLogger().err("Couldn't set active library to "+libraryId+": Library not registered.");
        return false;
    }
}