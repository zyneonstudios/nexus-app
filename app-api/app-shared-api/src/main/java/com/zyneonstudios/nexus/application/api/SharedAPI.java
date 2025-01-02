package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.api.shared.events.ApplicationEvent;
import com.zyneonstudios.nexus.application.api.shared.events.EventType;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class SharedAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<EventType, ArrayList<ApplicationEvent>> events = new HashMap<>();

    public SharedAPI() {

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

    public static void registerEvent(EventType eventType, ApplicationEvent event) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        events.get(eventType).add(event);
    }

    public static ArrayList<ApplicationEvent> getEvents(EventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }

    public static String getWorkingDirectory() {
        return ApplicationStorage.getApplicationPath();
    }
}
