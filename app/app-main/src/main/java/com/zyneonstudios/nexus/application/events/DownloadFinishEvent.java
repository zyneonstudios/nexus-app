package com.zyneonstudios.nexus.application.events;

import com.zyneonstudios.nexus.application.download.Download;
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

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean execute() {
        return onFinish();
    }

    public abstract boolean onFinish();
}