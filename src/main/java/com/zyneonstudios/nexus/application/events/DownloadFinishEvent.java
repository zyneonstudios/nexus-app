package com.zyneonstudios.nexus.application.events;

import com.zyneonstudios.nexus.application.download.Download;

import java.util.UUID;

public abstract class DownloadFinishEvent {

    private final UUID uuid = UUID.randomUUID();
    private final Download download;

    public DownloadFinishEvent(Download download) {
        this.download = download;
    }

    public Download getDownload() {
        return download;
    }

    public UUID getUUID() {
        return uuid;
    }

    public final boolean execute() {
        return onFinish();
    }

    public abstract boolean onFinish();
}