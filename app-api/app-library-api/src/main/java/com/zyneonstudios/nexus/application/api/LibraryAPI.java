package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.library.events.LibraryEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEventType;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class LibraryAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<LibraryEventType, ArrayList<LibraryEvent>> events = new HashMap<>();

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

    public static void registerEvent(LibraryEventType eventType, LibraryEvent event) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        events.get(eventType).add(event);
    }

    public static ArrayList<LibraryEvent> getEvents(LibraryEventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }
}