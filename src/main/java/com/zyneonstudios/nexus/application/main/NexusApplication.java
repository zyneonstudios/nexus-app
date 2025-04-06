package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.application.frame.ApplicationFrame;
import com.zyneonstudios.nexus.application.modules.ModuleLoader;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebSetup;
import com.zyneonstudios.nexus.utilities.file.FileActions;
import com.zyneonstudios.nexus.utilities.file.FileExtractor;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;
import com.zyneonstudios.nexus.utilities.storage.JsonStorage;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class NexusApplication {

    private final NexusEventHandler eventHandler = new NexusEventHandler();
    private boolean launched = false;
    private final NexusRunner runner = new NexusRunner();
    private static String workingDir;
    private final NexusWebSetup webSetup;
    private ApplicationFrame applicationFrame = null;
    private final boolean onlineUI;
    private static String uiPath = null;
    private final String version = "v3.0.0-alpha.7";
    private final JsonStorage settings;
    private final ModuleLoader moduleLoader;

    public NexusApplication(String path, String uiPath) {
        getLogger().log("Initializing application...");
        File workingDir = new File(path);
        if (workingDir.mkdirs()) {
            getLogger().deb("Creating working directory (first run)...");
        }
        settings = new JsonStorage(workingDir.getAbsolutePath() + "/config/settings.json");

        NexusApplication.workingDir = workingDir.getAbsolutePath().replace("\\", "/");
        if(uiPath!=null) {
            onlineUI = uiPath.equals("online");
            if(!onlineUI) {
                NexusApplication.uiPath = uiPath;
            }
        } else {
            onlineUI = false;
            NexusApplication.uiPath = NexusApplication.getWorkingDir().getAbsolutePath()+"/temp/ui";
        }
        File temp = new File(NexusApplication.workingDir + "/temp");
        if (temp.exists()) {
            if (!temp.delete()) {
                try {
                    FileActions.deleteFolder(temp);
                } catch (Exception ignore) {
                }
            }
        }
        if (temp.mkdirs()) {
            temp.deleteOnExit();
            FileExtractor.extractResourceFile("html.zip", NexusApplication.workingDir + "/temp/ui.zip", Main.class);
            File ui = new File(NexusApplication.workingDir + "/temp/ui.zip");
            FileExtractor.unzipFile(ui.getAbsolutePath(), NexusApplication.workingDir + "/temp/ui/");
            if (!ui.delete()) {
                ui.deleteOnExit();
            }
        } else {
            getLogger().err("Couldn't create temp folder: Old temp folder could not be deleted.");
            Main.stop(1);
        }
        webSetup = new NexusWebSetup(workingDir.getAbsolutePath() + "/libs/cef/");
        webSetup.enableCache(true);
        webSetup.enableCookies(true);
        webSetup.setup();
        webSetup.getWebClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                String url = browser.getURL();
                if(getLogger().isDebugging()) {
                    frame.executeJavaScript("localStorage.setItem('devtools','enabled');",url,0);
                }
                frame.executeJavaScript("app = true; localStorage.setItem('enabled','true'); document.getElementById('menu').classList.add('transition');", url, 0);
                if (url.contains("discover.html")) {
                    frame.executeJavaScript("enableMenu(true);", url, 0);
                } else {
                    frame.executeJavaScript("disableMenu(true)", url, 0);
                }
            }
        });
        moduleLoader = new ModuleLoader();
        loadModules();
    }

    private void loadModules() {
        try {
            File modules = new File(workingDir + "/modules");
            if (modules.exists()) {
                if (modules.isDirectory()) {
                    for (File module : Objects.requireNonNull(modules.listFiles())) {
                        try {
                            if (!module.isDirectory()) {
                                moduleLoader.loadModule(moduleLoader.readModule(module));
                            }
                        } catch (Exception e) {
                            getLogger().err("Couldn't load module " + module.getName() + ": " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            getLogger().err("Couldn't load modules: " + e.getMessage());
        }
    }

    public boolean launch() {
        if(!launched) {
            try {
                String url = "localhost:"+Main.getPort()+"/index.html?app=true&page=discover.html";
                if(onlineUI) {
                    url = "https://zyneonstudios.github.io/nexus-app/src/main/html?app=true&page=discover.html";
                }
                applicationFrame = new ApplicationFrame(webSetup, url, true);
                applicationFrame.setTitlebar(version, Color.black, Color.white);
                applicationFrame.setSize(1200, 720);
                applicationFrame.setLocationRelativeTo(null);
                applicationFrame.setVisible(true);
                launched = true;
            } catch (Exception e) {
                getLogger().log("Couldn't launch application: " + e.getMessage());
                launched = false;
            }
        }
        runner.start();
        moduleLoader.activateModules();
        return launched;
    }

    public static NexusLogger getLogger() {
        return Main.getLogger();
    }

    public NexusRunner getRunner() {
        return runner;
    }

    public String getWorkingPath() {
        return workingDir;
    }

    public ApplicationFrame getApplicationFrame() {
        return applicationFrame;
    }

    public NexusWebSetup getWebSetup() {
        return webSetup;
    }

    public static File getWorkingDir() {
        return new File(workingDir);
    }

    public boolean isLaunched() {
        return launched;
    }

    public static String getUiPath() {
        return uiPath;
    }

    public boolean isOnlineUI() {
        return onlineUI;
    }

    public String getVersion() {
        return version;
    }

    public JsonStorage getSettings() {
        return settings;
    }

    public ModuleLoader getModuleLoader() {
        return moduleLoader;
    }

    public NexusEventHandler getEventHandler() {
        return eventHandler;
    }
}