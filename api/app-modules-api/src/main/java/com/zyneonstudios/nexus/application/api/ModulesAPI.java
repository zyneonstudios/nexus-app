package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.modules.events.ModuleEvent;
import com.zyneonstudios.nexus.application.api.modules.events.ModuleEventType;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class ModulesAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<ModuleEventType, ArrayList<ModuleEvent>> events = new HashMap<>();

    public ModulesAPI() {

    }

    @Override
    public void load(NexusApplication application) {
        this.application = application;
    }

    @Override
    public void enable() {
        ApplicationAPI.super.enable();
    }

    @Override
    public void shutdown() {
        ApplicationAPI.super.shutdown();
    }

    public static void registerEvent(ModuleEvent event) {
        if(!events.containsKey(event.getType())) {
            events.put(event.getType(), new ArrayList<>());
        }
        events.get(event.getType()).add(event);
    }

    public static ArrayList<ModuleEvent> getEvents(ModuleEventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }
}