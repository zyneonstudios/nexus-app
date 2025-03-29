package com.zyneonstudios.nexus.application.main;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.desktop.frame.nexus.NexusWebFrame;
import com.zyneonstudios.nexus.desktop.frame.web.NWebFrame;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebSetup;
import com.zyneonstudios.nexus.desktop.frame.web.WebFrame;
import com.zyneonstudios.nexus.utilities.file.FileActions;
import com.zyneonstudios.nexus.utilities.file.FileExtractor;
import com.zyneonstudios.nexus.utilities.logger.NexusLogger;
import com.zyneonstudios.nexus.utilities.system.OperatingSystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class NexusApplication {

    private boolean launched = false;
    private static final NexusLogger logger = new NexusLogger("NEXUS");
    private final NexusRunner runner = new NexusRunner(this);
    private final String workingDir;

    private final NexusWebSetup webSetup;
    private WebFrame applicationFrame = null;

    public NexusApplication(String path) {
        logger.log("Initializing application...");
        File workingDir = new File(path);
        if(workingDir.mkdirs()) {
            logger.deb("Creating working directory (first run)...");
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
            FileExtractor.extractResourceFile("html.zip",this.workingDir+"/temp/ui.zip",Main.class);
            File ui = new File(this.workingDir+"/temp/ui.zip");
            FileExtractor.unzipFile(ui.getAbsolutePath(),this.workingDir+"/temp/ui/");
            if(!ui.delete()) {
                ui.deleteOnExit();
            }
        } else {
            logger.err("Couldn't create temp folder: Old temp folder could not be deleted.");
            System.exit(1);
        }
        webSetup = new NexusWebSetup(workingDir.getAbsolutePath()+"/libs/cef/");
        webSetup.setup();
    }

    public boolean launch() {
        if(!launched) {
            try {
                if(OperatingSystem.getType().equals(OperatingSystem.Type.Linux)||OperatingSystem.getType().equals(OperatingSystem.Type.macOS)) {
                    applicationFrame = new NexusWebFrame(webSetup.getWebClient(), workingDir + "/temp/ui/index.html", true);
                    ((NexusWebFrame)applicationFrame).setTitle("NEXUS App");
                    ((NexusWebFrame)applicationFrame).setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/icon.png"))).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                } else {
                    applicationFrame = new NWebFrame(webSetup.getWebClient(), workingDir + "/temp/ui/index.html", true);
                    ((NWebFrame)applicationFrame).setTitlebar("NEXUS App", Color.black, Color.white, ImageIO.read(Objects.requireNonNull(getClass().getResource("/icon.png"))).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                }
                applicationFrame.getAsJFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                applicationFrame.getAsJFrame().setSize(1200, 720);
                applicationFrame.getAsJFrame().setMinimumSize(applicationFrame.getAsJFrame().getSize());
                applicationFrame.getAsJFrame().setLocationRelativeTo(null);
                applicationFrame.getAsJFrame().setVisible(true);
                launched = true;
            } catch (Exception e) {
                logger.log("Couldn't launch application: " + e.getMessage());
                launched = false;
            }
        }
        runner.start();
        return launched;
    }

    public static NexusLogger getLogger() {
        return logger;
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