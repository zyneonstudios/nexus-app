package com.zyneonstudios.nexus.application;

import com.zyneonstudios.nexus.application.frame.ZyneonSplash;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main {

    private static final NexusLogger logger = new NexusLogger("NEXUS");
    private static String path = "";
    private static String ui = null;
    private static int port = 8094;
    private static boolean online;
    private static NexusApplication application;

    public static void main(String[] args) {
        NexusDesktop.init();
        resolveArguments(args);
        ZyneonSplash splash = new ZyneonSplash();
        splash.setVisible(true);
        application = new NexusApplication(path,ui);
        if(!application.isOnlineUI()) {
            startWebServer(args);
        }
        if(application.launch()) {
            splash.dispose();
            System.gc();
        } else {
            NexusApplication.stop(1);
        }
    }

    private static void startWebServer(String[] args) {
        try {
            new SpringApplicationBuilder(Main.class)
                    .properties("logging.level.root=WARN", "logging.pattern.console=", "server.port=" + port)
                    .run(args);
        } catch (Exception e) {
            port++;
            startWebServer(args);
        }
    }

    private static void resolveArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                String arg = args[i].toLowerCase();
                switch (arg) {
                    case "-h", "--help" -> {
                        logger.log("NEXUS App help:");
                        logger.log("  -d, --debug: Enables debug console output.");
                        logger.log("  -h, --help: This help message.");
                        logger.log("  -o, --online: Enables the connection to the online UI. Caution: This may cause problems with some modules.");
                        logger.log("  -p <path>, --path <path>: Lets you select the run folder.");
                        logger.log("  -u <path>, --ui <path>: Lets you select the folder where the user interface should be unpacked.");
                        System.exit(0);
                    }
                    case "-u", "--ui" -> ui = args[i + 1];
                    case "-p", "--path" -> path = args[i + 1];
                    case "-o", "--online" -> ui = "online";
                    case "-d", "--debug" -> logger.enableDebug();
                }
            } catch (Exception e) {
                logger.err(e.getMessage());
                logger.err("Use -h or --help at startup to view the startup arguments and their syntax.");
                System.exit(1);
            }
        }
    }

    public static NexusLogger getLogger() {
        return logger;
    }

    public static int getPort() {
        return port;
    }
}