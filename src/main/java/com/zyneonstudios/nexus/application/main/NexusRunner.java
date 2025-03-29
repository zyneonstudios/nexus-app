package com.zyneonstudios.nexus.application.main;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NexusRunner {

    private final UUID runnerID = UUID.randomUUID();
    private final NexusApplication app;
    private boolean started = false;
    private ScheduledExecutorService executor;

    public NexusRunner(NexusApplication app) {
        this.app = app;
    }

    public UUID getRunnerID() {
        return runnerID;
    }

    public boolean isStarted() {
        return started;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public void start() {
        if(!started) {
            started = true;
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void run() {

    }
}