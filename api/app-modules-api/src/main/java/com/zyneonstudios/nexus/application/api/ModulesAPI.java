package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.modules.ApplicationModule;
import com.zyneonstudios.nexus.application.api.modules.events.ModuleEvent;
import com.zyneonstudios.nexus.application.api.modules.events.ModuleEventType;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class ModulesAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<ModuleEventType, ArrayList<ModuleEvent>> events = new HashMap<>();
    private final HashMap<String, ApplicationModule> modules = new HashMap<>();

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

    public void registerModule(ApplicationModule module) {
        if(!modules.containsKey(module.getId())) {
            modules.put(module.getId(), module);
        }
    }

    public ApplicationModule getModule(String id) {
        return modules.get(id);
    }

    boolean loaded = false;
    public void loadModules() {
        if(!loaded) {
            loaded = true;
            for(ApplicationModule module : modules.values()) {
                module.onLoad();
            }
        }
    }

    boolean enabled = false;
    public void enableModues() {
        if(!enabled) {
            enabled = true;
            for(ApplicationModule module : modules.values()) {
                module.onEnable();
            }
        }
    }

    boolean disabled = false;
    public void disableModules() {
        if(!disabled) {
            disabled = true;
            for(ApplicationModule module : modules.values()) {
                module.onDisable();
            }
        }
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