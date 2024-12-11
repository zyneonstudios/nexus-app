package com.zyneonstudios.application.main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zyneonstudios.application.download.Download;
import com.zyneonstudios.application.download.DownloadManager;
import com.zyneonstudios.application.frame.web.ApplicationFrame;
import com.zyneonstudios.nexus.utilities.json.GsonUtility;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApplicationRunner {

    private ScheduledExecutorService executor;
    private boolean started = false;
    private int u = 119;
    private final NexusApplication app;

    private UUID downloading = null;

    private final String version = ApplicationStorage.getApplicationVersion();
    //TODO private final String motd_id = "";

    public ApplicationRunner(NexusApplication app) {
        this.app = app;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public void start() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.SECONDS);
    }

    int c = 0;
    protected void run() {
        if (!started) {
            started = true;
            return;
        }

        if(c == 300) {
            c = 0;
            System.gc();
        }


        CompletableFuture.runAsync(()-> {
            if (downloading != null) {
                Download download = NexusApplication.getDownloadManager().getDownloads().get(downloading);
                if (download.isFinished()) {
                    downloading = null;
                }
            } else {
                NexusApplication.getDownloadManager().getDownloads().forEach((uuid, download) -> {
                    if (download.getState().equals(DownloadManager.DownloadState.WAITING)) {
                        downloading = uuid;
                        download.start();
                    }
                });
            }
        });

        if (((ApplicationFrame) app.getFrame()).getBrowser().getURL().contains("/downloads.html")) {
            app.getDownloadManager().getDownloads().forEach((uuid, download) -> {
                if (download.getState().equals(DownloadManager.DownloadState.WAITING)) {
                    String title = "setDownload(\"" + download.getName().replace("\"", "''") + "\",";
                    String state = "\"" + download.getState().toString().replace("\"", "''") + "\",";
                    String elapsedTime = "\"0 seconds\",";
                    String downloadSpeed = "\"0 mb/s\",";
                    String remainingTime = "\"\",";
                    String downloadSize = "\"\",";
                    String fileSize = "\"0 mb\",";
                    String path = "\"" + download.getPath().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String url = "\"" + download.getUrl().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String id = "\"" + download.getId().replace("\"", "''") + "\",";
                    String progress = "\"" + download.getPercentString() + "\",";
                    String percent = download.getPercent() + ");";
                    String command = title + state + elapsedTime + downloadSpeed + remainingTime + downloadSize + fileSize + path + url + id + progress + percent;
                    ((ApplicationFrame) app.getFrame()).executeJavaScript(command);
                } else if (download.getState().equals(DownloadManager.DownloadState.RUNNING)) {
                    String title = "setDownload(\"" + download.getName().replace("\"", "''") + "\",";
                    String state = "\"" + download.getState().toString().replace("\"", "''") + "\",";
                    String elapsedTime = "\"" + download.getElapsedTime().getSeconds() + " seconds\",";
                    String downloadSpeed = "\"" + (int) download.getSpeedMbps() + " mb/s\",";
                    String remainingTime = "\"" + download.getEstimatedRemainingTime().getSeconds() + " seconds\",";
                    String downloadSize = "\"" + (download.getFileSize() / 1000) / 1000 + " mb\",";
                    String fileSize = "\"" + (download.getLastBytesRead() / 1000) / 1000 + " mb\",";
                    String path = "\"" + download.getPath().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String url = "\"" + download.getUrl().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String id = "\"" + download.getId().replace("\"", "''") + "\",";
                    String progress = "\"" + (int)download.getPercent() + "%\",";
                    String percent = download.getPercent() + ");";
                    String command = title + state + elapsedTime + downloadSpeed + remainingTime + downloadSize + fileSize + path + url + id + progress + percent;
                    ((ApplicationFrame) app.getFrame()).executeJavaScript(command);
                } else if (download.getState().equals(DownloadManager.DownloadState.FINISHED) || download.getState().equals(DownloadManager.DownloadState.FAILED)) {
                    String title = "setDownload(\"" + download.getName().replace("\"", "''") + "\",";
                    String state = "\"" + download.getState().toString().replace("\"", "''") + "\",";
                    String elapsedTime = "\"" + download.getElapsedTime().getSeconds() + " seconds\",";
                    String downloadSpeed = "\"0 mb/s\",";
                    String remainingTime = "\"" + download.getEstimatedRemainingTime().getSeconds() + " seconds\",";
                    String downloadSize = "\"" + (download.getFileSize() / 1000) / 1000 + " mb\",";
                    String fileSize = downloadSize;
                    String path = "\"" + download.getPath().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String url = "\"" + download.getUrl().toString().replace("\\","/").replace("\"", "''") + "\",";
                    String id = "\"" + download.getId().replace("\"", "''") + "\",";
                    String progress = "\"" + download.getPercentString() + "\",";
                    String percent = download.getPercent() + ");";
                    String command = title + state + elapsedTime + downloadSpeed + remainingTime + downloadSize + fileSize + path + url + id + progress + percent;
                    ((ApplicationFrame) app.getFrame()).executeJavaScript(command);
                }
            });
        }

        try {
            JsonObject json = new Gson().fromJson(GsonUtility.getFromURL("https://raw.githubusercontent.com/zyneonstudios/nexus-nex/main/application/index.json"), JsonObject.class).getAsJsonArray("versions").get(0).getAsJsonObject();
            checkVersion(json);
        } catch (Exception ignore) {
        }
    }

    private boolean runUpdateCheck(JsonObject json) {
        if (!ApplicationStorage.test) {
            NexusApplication.getLogger().dbg("[RUNNER] Checking for Updates...");
            NexusApplication.getLogger().dbg("[RUNNER] Parsed JSON Data...");
            String v = json.get("info").getAsJsonObject().get("version").getAsString();
            NexusApplication.getLogger().dbg("[RUNNER] Latest version: " + v + "...");
            NexusApplication.getLogger().dbg("[RUNNER] Current version: " + version + "...");
            if (!v.equals(version)) {
                NexusApplication.getLogger().dbg("[RUNNER] The application is not up to date!");
                return true;
            }
        }
        return false;
    }

    private void checkVersion(JsonObject json) {
        u++;
        if (u > 120) {
            u = 0;
            if (runUpdateCheck(json)) {
                NexusApplication.getLogger().dbg("[RUNNER] Sending notification...");
                //TODO: Application.getFrame().sendNotification("Update available!", "Version " + v + " has been released!", "<a onclick=\"callJavaMethod('button.exit');\" class='button'>Install</a><a onclick=\"callJavaMethod('button.online');\" class='button'>Dynamic update</a>", v, true);
            }
        }
    }
}