package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.discover.events.DiscoverEvent;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverEventType;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<DiscoverEventType, ArrayList<DiscoverEvent>> events = new HashMap<>();

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

    public static void registerEvent(DiscoverEventType eventType, DiscoverEvent event) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        events.get(eventType).add(event);
    }

    public static ArrayList<DiscoverEvent> getEvents(DiscoverEventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }
}