package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.application.events.DownloadFinishEvent;
import com.zyneonstudios.nexus.application.events.PageLoadedEvent;

import java.util.ArrayList;

public class NexusEventHandler {

    private final ArrayList<DownloadFinishEvent> downloadFinishEvents = new ArrayList<>();
    private final ArrayList<PageLoadedEvent> pageLoadedEvents = new ArrayList<>();

    public ArrayList<DownloadFinishEvent> getDownloadFinishEvents() {
        return downloadFinishEvents;
    }

    public void addDownloadFinishEvent(DownloadFinishEvent event) {
        if(!downloadFinishEvents.contains(event)) {
            downloadFinishEvents.add(event);
        }
    }

    public void removeDownloadFinishEvent(DownloadFinishEvent event) {
        downloadFinishEvents.remove(event);
    }

    public ArrayList<PageLoadedEvent> getPageLoadedEvents() {
        return pageLoadedEvents;
    }

    public void addPageLoadedEvent(PageLoadedEvent event) {
        if(!pageLoadedEvents.contains(event)) {
            pageLoadedEvents.add(event);
        }
    }

    public void removePageLoadedEvent(PageLoadedEvent event) {
        pageLoadedEvents.remove(event);
    }
}
