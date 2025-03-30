package com.zyneonstudios.nexus.application;

import com.zyneonstudios.nexus.application.frame.ZyneonSplash;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;

public class Main {

    private static final NexusLogger logger = new NexusLogger("NEXUS");
    private static String path = "";
    private static String ui = null;

    public static void main(String[] args) {
        NexusDesktop.init();
        resolveArguments(args);
        ZyneonSplash splash = new ZyneonSplash();
        splash.setVisible(true);
        NexusApplication application = new NexusApplication(path,ui);
        if(application.launch()) {
            splash.dispose();
            System.gc();
        } else {
            System.exit(1);
        }
    }

    private static void resolveArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toLowerCase();
            switch (arg) {
                case "-p", "--path" -> {
                    if (i + 1 < args.length) {
                        path = args[i + 1];
                    }
                }
                case "--ui" -> {
                    if (i + 1 < args.length) {
                        ui = args[i + 1];
                    }
                }
                case "-d", "--debug" -> logger.enableDebug();
            }
        }
    }

    public static NexusLogger getLogger() {
        return logger;
    }
}