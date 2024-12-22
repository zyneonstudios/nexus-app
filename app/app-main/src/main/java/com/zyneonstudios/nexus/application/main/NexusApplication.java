package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.Main;
import com.zyneonstudios.nexus.application.api.LibraryAPI;
import com.zyneonstudios.nexus.application.api.ModulesAPI;
import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.download.DownloadManager;
import com.zyneonstudios.nexus.application.frame.web.ApplicationFrame;
import com.zyneonstudios.nexus.application.frame.web.CustomApplicationFrame;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebSetup;
import com.zyneonstudios.nexus.utilities.NexusUtilities;
import com.zyneonstudios.nexus.utilities.file.FileActions;
import com.zyneonstudios.nexus.utilities.file.FileExtractor;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import org.cef.CefApp;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.security.CodeSource;

public class NexusApplication {

    private final ApplicationFrame frame;
    private static final NexusLogger logger = NexusUtilities.getLogger();

    private final ApplicationRunner runner;
    private static DownloadManager downloadManager;

    private static SharedAPI sharedAPI;
    private static LibraryAPI libraryAPI;
    private static ModulesAPI modulesAPI;

    public NexusApplication(String[] args) {
        new ApplicationStorage(args,this);

        sharedAPI = new SharedAPI();
        sharedAPI.load(this);
        libraryAPI = new LibraryAPI();
        libraryAPI.load(this);
        modulesAPI = new ModulesAPI();
        modulesAPI.load(this);

        logger.log("[APP] Updated application ui: "+update());
        boolean disableCustomFrame = false;
        if(ApplicationStorage.getSettings().get("settings.linux.customFrame")!=null) {
            try {
                disableCustomFrame = !ApplicationStorage.getSettings().getBool("settings.linux.customFrame");
            } catch (Exception ignore) {}
        }
        String startPage = ApplicationStorage.startPage;
        if(ApplicationStorage.getSettings().get("settings.setupFinished")==null) {
            ApplicationStorage.getSettings().set("settings.setupFinished",false);
        }
        try {
            if(!ApplicationStorage.getSettings().getBool("settings.setupFinished")) {
                startPage = "firstrun.html";
            }
        } catch (Exception ignore) {}
        if(ApplicationStorage.getSettings().get("cache.restartPage")!=null) {
            try {
                startPage = ApplicationStorage.getSettings().get("cache.restartPage").toString();
                ApplicationStorage.getSettings().delete("cache.restartPage");
            } catch (Exception ignore) {}
        }
        NexusWebSetup setup = new NexusWebSetup(ApplicationStorage.getApplicationPath()+"libraries/cef");
        setup.getBuilder().setAppHandler(new MavenCefAppHandlerAdapter() {
            @Override @Deprecated
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    NexusApplication.stop(true);
                }
                if(!ApplicationStorage.getOS().startsWith("Windows")) {
                    if(state == CefApp.CefAppState.SHUTTING_DOWN) {
                        NexusApplication.stop(true);
                    }
                }
            }
        });
        setup.enableCache(true); setup.enableCookies(true); setup.setup();

        if(ApplicationStorage.getOS().startsWith("macOS")|| ApplicationStorage.getOS().startsWith("Windows")||disableCustomFrame) {
            frame = new ApplicationFrame(this, ApplicationStorage.urlBase + ApplicationStorage.language + "/" + startPage, setup.getWebClient());
            frame.pack(); frame.setSize(new Dimension(1200,720));
        } else {
            ApplicationFrame frame_ = null;
            try {
                frame_ = new CustomApplicationFrame(this, ApplicationStorage.urlBase + ApplicationStorage.language + "/" + startPage, setup.getWebClient());
                frame_.pack(); frame_.setSize(new Dimension(1080,660));
            } catch (Exception e) {
                logger.err("[APP] Couldn't load custom Linux frame: "+e.getMessage());
                logger.err("[APP] Disabling custom Linux frame and restarting...");
                ApplicationStorage.getSettings().set("settings.linux.customFrame",false);
                restart(false);
            }
            frame = frame_;
        }
        if(frame==null) {
            System.exit(-1);;
        }
        frame.setLocationRelativeTo(null);

        this.runner = new ApplicationRunner(this);
        this.runner.start();
        downloadManager = new DownloadManager(this);

        sharedAPI.enable();
        libraryAPI.enable();
        modulesAPI.enable();
    }

    public static DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public ApplicationRunner getRunner() {
        return runner;
    }

    public static NexusLogger getLogger() {
        return logger;
    }

    public ApplicationFrame getFrame() {
        return frame;
    }

    public SharedAPI getSharedAPI() {
        return sharedAPI;
    }

    public LibraryAPI getLibraryAPI() {
        return libraryAPI;
    }

    public ModulesAPI getModulesAPI() {
        return modulesAPI;
    }

    private boolean update() {

        // TRYING TO DELETE OLD TEMP FOLDER
        File temp = new File(ApplicationStorage.getApplicationPath() + "temp");
        if(temp.exists()) {
            if(temp.isDirectory()) {
                logger.dbg("[APP] Deleted temporary files: "+FileActions.deleteFolder(temp));
            } else {
                logger.dbg("[APP] Deleted temporary files: "+temp.delete());
            }
        }

        // UI UPDATE
        boolean updated;
        try {
            if(new File(ApplicationStorage.getApplicationPath() + "temp/ui/").exists()) {
                try {
                    FileActions.deleteFolder(new File(ApplicationStorage.getApplicationPath() + "temp/ui/"));
                } catch (Exception e) {
                    logger.err("Couldn't delete old temporary ui files: "+e.getMessage());
                }
            }
            logger.dbg("[APP] Created new ui path: "+new File(ApplicationStorage.getApplicationPath() + "temp/ui/").mkdirs());
            FileExtractor.extractResourceFile("content.zip", ApplicationStorage.getApplicationPath()+"temp/content.zip",Main.class);
            FileExtractor.unzipFile(ApplicationStorage.getApplicationPath()+"temp/content.zip", ApplicationStorage.getApplicationPath() + "temp/ui");
            logger.dbg("[APP] Deleted ui archive: "+new File(ApplicationStorage.getApplicationPath()+"temp/content.zip").delete());
            updated = true;
        } catch (Exception e) {
            logger.err("[APP] Couldn't update application user interface: "+e.getMessage());
            updated = false;
        }
        logger.dbg("[APP] Deleted old updatar json: "+new File(ApplicationStorage.getApplicationPath() + "updater.json").delete());
        logger.dbg("[APP] Deleted older updater json: "+new File(ApplicationStorage.getApplicationPath() + "version.json").delete());
        return updated;
    }

    public void launch() {
        frame.setVisible(true);
        if(Main.splash!=null) {
            Main.splash.setVisible(false);
            Main.splash = null;
            System.gc();
        }
    }

    public void restart(boolean soft) {
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        if(soft) {
            if (codeSource != null) {
                URL jarUrl = codeSource.getLocation();
                String jarPath = jarUrl.getPath();
                if(!ApplicationStorage.getOS().startsWith("Linux")) {
                    if (jarPath.startsWith("/")) {
                        jarPath = jarPath.replaceFirst("/", "");
                    }
                }
                StringBuilder args = new StringBuilder();
                if(ApplicationStorage.getArguments()!=null) {
                    for(String arg : ApplicationStorage.getArguments()) {
                        args.append(arg).append(" ");
                    }
                }
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, args.toString());
                try {
                    pb.start();
                } catch (Exception e) {
                    logger.err("[APP] Couldn't restart application: "+e.getMessage());
                }
                stop(false);
                System.exit(0);
            }
        } else {
            if (codeSource != null) {
                URL jarUrl = codeSource.getLocation();
                String jarPath = jarUrl.getPath();
                if(!ApplicationStorage.getOS().startsWith("Linux")) {
                    if (jarPath.startsWith("/")) {
                        jarPath = jarPath.replaceFirst("/", "");
                    }
                }
                StringBuilder args = new StringBuilder();
                if (ApplicationStorage.getArguments() != null) {
                    for (String arg : ApplicationStorage.getArguments()) {
                        args.append(arg).append(" ");
                    }
                }
                File updater;
                if(new File(ApplicationStorage.getApplicationPath()+"bootstrapper.jar").exists()) {
                    updater = new File(ApplicationStorage.getApplicationPath()+"bootstrapper.jar");
                } else {
                    updater = new File(ApplicationStorage.getApplicationPath().replace("\\", "/").replace("/NEXUS App/", "/Application/app.jar"));
                }
                if (updater.exists()) {
                    jarPath = updater.getAbsolutePath();
                }
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, args.toString());
                try {
                    pb.start();
                } catch (Exception e) {
                    logger.err("[APP] Couldn't restart application: " + e.getMessage());
                }
                stop(true);
            }
        }
        System.exit(-1);
    }

    public static void stop(boolean app) {
        modulesAPI.shutdown();
        libraryAPI.shutdown();
        sharedAPI.shutdown();

        if(app) {
            FileActions.deleteFolder(new File(ApplicationStorage.getApplicationPath() + "temp/"));
            System.exit(0);
        }
    }
}