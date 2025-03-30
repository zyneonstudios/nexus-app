package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.application.frame.ApplicationFrame;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebSetup;
import com.zyneonstudios.nexus.desktop.frame.web.WebFrame;
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
    private final String workingDir;

    private final NexusWebSetup webSetup;
    private ApplicationFrame applicationFrame = null;

    private String url;

    public NexusApplication(String path, String url) {
        getLogger().log("Initializing application...");
        this.url = url;
        File workingDir = new File(path);
        if(workingDir.mkdirs()) {
            getLogger().deb("Creating working directory (first run)...");
        }
        this.workingDir = workingDir.getAbsolutePath().replace("\\","/");
        File temp = new File(this.workingDir+"/temp");
        if(temp.exists()) {
            if(!temp.delete()) {
                try {
                    FileActions.deleteFolder(temp);
                } catch (Exception ignore) {}
            }
        }
        if(temp.mkdirs()) {
            temp.deleteOnExit();
            if(url==null) {
                this.url = "file://" + this.workingDir + "/temp/ui/index.html";
                FileExtractor.extractResourceFile("html.zip", this.workingDir + "/temp/ui.zip", Main.class);
                File ui = new File(this.workingDir + "/temp/ui.zip");
                FileExtractor.unzipFile(ui.getAbsolutePath(), this.workingDir + "/temp/ui/");
                if (!ui.delete()) {
                    ui.deleteOnExit();
                }
            }
        } else {
            getLogger().err("Couldn't create temp folder: Old temp folder could not be deleted.");
            System.exit(1);
        }
        webSetup = new NexusWebSetup(workingDir.getAbsolutePath()+"/libs/cef/");
        webSetup.enableCache(true);
        webSetup.enableCookies(true);
        webSetup.setup();
        webSetup.getWebClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                String url = browser.getURL();
                frame.executeJavaScript("localStorage.setItem('enabled','true'); document.getElementById('menu').classList.add('transition');",url,0);
                if(url.contains("discover.html")) {
                    frame.executeJavaScript("enableMenu(true);",url,0);
                } else {
                    frame.executeJavaScript("disableMenu(true)",url,0);
                }
            }
        });
    }

    public boolean launch() {
        if(!launched) {
            try {
                applicationFrame = new ApplicationFrame(this, url, webSetup.getWebClient(), true);
                applicationFrame.setTitlebar("v3.0.0-alpha.2", Color.BLACK, Color.WHITE);
                applicationFrame.setSize(1200, 720);
                applicationFrame.setMinimumSize(applicationFrame.getSize());
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

    public WebFrame getApplicationFrame() {
        return applicationFrame;
    }

    public NexusWebSetup getWebSetup() {
        return webSetup;
    }

    public File getWorkingDir() {
        return new File(workingDir);
    }

    public boolean isLaunched() {
        return launched;
    }
}