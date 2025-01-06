package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.api.shared.events.ApplicationEvent;
import com.zyneonstudios.nexus.application.api.shared.events.EventType;
import com.zyneonstudios.nexus.application.frame.web.ApplicationFrame;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class SharedAPI implements ApplicationAPI {

    private NexusApplication application;
    private static ApplicationFrame frame;
    private static final HashMap<EventType, ArrayList<ApplicationEvent>> events = new HashMap<>();

    public SharedAPI() {

    }

    @Override
    public void load(NexusApplication application) {
        this.application = application;
    }

    @Override
    public void enable() {
        frame = this.application.getFrame();
    }

    @Override
    public void shutdown() {

    }

    public static void registerEvent(ApplicationEvent event) {
        if(!events.containsKey(event.getType())) {
            events.put(event.getType(), new ArrayList<>());
        }
        events.get(event.getType()).add(event);
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

    public static String getFrameUrl() {
        return frame.getBrowser().getURL();
    }

    public static void openFrameUrl(String url) {
        url = (url.replace("https://","file://").replace("http://","file://")).replace("\\\\","\\").replace("\\","/");
        if(!url.startsWith("file://")) {
            url = "file://"+url;
        }
        frame.getBrowser().loadURL(url);
    }

    public static void openAppPage(AppPage page) {
        openAppPage(page.toString().toLowerCase());
    }

    public static void openAppPage(String pageString) {
        if(!pageString.endsWith(".html")) {
            pageString = pageString+".html";
        }
        if(!pageString.startsWith(ApplicationStorage.urlBase)) {
            pageString = ApplicationStorage.urlBase+ApplicationStorage.language+"/+"+pageString;
        }
        openFrameUrl(pageString);
    }

    public enum AppPage {
        DISCOVER,
        LIBRARY,
        DOWNLOADS,
        SETTINGS
    }
}
