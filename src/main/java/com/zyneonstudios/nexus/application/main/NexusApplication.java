package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.application.frame.ApplicationFrame;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebSetup;
import com.zyneonstudios.nexus.utilities.file.FileActions;
import com.zyneonstudios.nexus.utilities.file.FileExtractor;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

import java.awt.*;
import java.io.File;

public class NexusApplication {

    private boolean launched = false;
    private final NexusRunner runner = new NexusRunner(this);
    private static String workingDir;
    private final NexusWebSetup webSetup;
    private ApplicationFrame applicationFrame = null;
    private boolean onlineUI;

    public NexusApplication(String path, boolean online) {
        getLogger().log("Initializing application...");
        onlineUI = online;
        File workingDir = new File(path);
        if (workingDir.mkdirs()) {
            getLogger().deb("Creating working directory (first run)...");
        }
        NexusApplication.workingDir = workingDir.getAbsolutePath().replace("\\", "/");
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
            System.exit(1);
        }
        webSetup = new NexusWebSetup(workingDir.getAbsolutePath() + "/libs/cef/");
        webSetup.enableCache(true);
        webSetup.enableCookies(true);
        webSetup.setup();
        webSetup.getWebClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                String url = browser.getURL();
                frame.executeJavaScript("app = true; localStorage.setItem('enabled','true'); document.getElementById('menu').classList.add('transition');", url, 0);
                if (url.contains("discover.html")) {
                    frame.executeJavaScript("enableMenu(true);", url, 0);
                } else {
                    frame.executeJavaScript("disableMenu(true)", url, 0);
                }
            }
        });
    }

    public boolean launch() {
        if(!launched) {
            try {
                String url = "localhost:"+Main.getPort()+"/index.html?app=true";
                if(onlineUI) {
                    url = "https://app.nexus.zyneonstudios.net/?app=true";
                }
                applicationFrame = new ApplicationFrame(webSetup, url, true);
                applicationFrame.setTitlebar("v3.0.0-alpha.6", Color.black, Color.white);
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
}