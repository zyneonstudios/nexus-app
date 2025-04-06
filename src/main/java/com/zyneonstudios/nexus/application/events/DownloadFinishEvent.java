package com.zyneonstudios.nexus.application.events;

import com.zyneonstudios.nexus.application.downloads.Download;
import com.zyneonstudios.nexus.utilities.events.Event;

import java.util.UUID;

public abstract class DownloadFinishEvent implements Event {

    private final UUID uuid = UUID.randomUUID();
    private final Download download;

    public DownloadFinishEvent(Download download) {
        this.download = download;
    }

    public Download getDownload() {
        return download;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public final boolean execute() {
        return onFinish();
    }

    public abstract boolean onFinish();
}