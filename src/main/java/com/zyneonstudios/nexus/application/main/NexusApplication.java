package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.utilities.logger.NexusLogger;

public class NexusApplication {

    private boolean launched = false;
    private static final NexusLogger logger = new NexusLogger("NEXUS");
    private final NexusRunner runner = new NexusRunner(this);

    public NexusApplication() {
        logger.log("Initializing application...");
    }

    public boolean launch() {
        if(!launched) {
            try {
                launched = true;
            } catch (Exception e) {
                logger.log("Couldn't launch application: " + e.getMessage());
                launched = false;
            }
        }
        return launched;
    }

    public static NexusLogger getLogger() {
        return logger;
    }

    public NexusRunner getRunner() {
        return runner;
    }
}