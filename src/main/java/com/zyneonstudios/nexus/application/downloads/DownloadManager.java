package com.zyneonstudios.nexus.application.downloads;

import com.zyneonstudios.nexus.application.main.NexusRunner;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.HashMap;
import java.util.UUID;

public class DownloadManager {

    private final HashMap<UUID,Download> downloads = new HashMap<>();

    private final NexusRunner runner;

    public DownloadManager(NexusApplication application) {
        this.runner = application.getRunner();
    }

    public NexusRunner getRunner() {
        return runner;
    }

    public HashMap<UUID, Download> getDownloads() {
        return downloads;
    }

    public boolean addDownload(Download download) {
        if(!downloads.containsKey(download.getUuid())&&!downloads.containsValue(download)) {
            downloads.put(download.getUuid(),download);
            return true;
        }
        return false;
    }

    public boolean removeDownload(Download download) {
        if(downloads.containsValue(download)) {
            downloads.remove(download.getUuid(),download);
            return true;
        }
        return false;
    }

    public boolean removeDownload(UUID uuid) {
        if(downloads.containsKey(uuid)) {
            downloads.remove(uuid);
            return true;
        }
        return false;
    }

    public enum DownloadState {
        WAITING,
        RUNNING,
        FINISHED,
        FAILED
    }
}