package com.zyneonstudios.nexus;

import com.formdev.flatlaf.FlatDarkLaf;
import com.zyneonstudios.nexus.application.bootstrapper.NexusBootstrapper;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
            UIManager.setLookAndFeel(FlatDarkLaf.class.getName());
        } catch (Exception ignore) {}
        new NexusBootstrapper("app/app-bootstrapper/target/run/");
    }

    public static File getDefaultFolder(boolean old) {
        String folderName = "Zyneon/NEXUS App";
        if(old) {
            folderName = "Zyneon/Application";
        }
        String appData;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            appData = System.getenv("LOCALAPPDATA");
        } else if (os.contains("mac")) {
            appData = System.getProperty("user.home") + "/Library/Application Support";
        } else {
            appData = System.getProperty("user.home") + "/.local/share";
        }
        Path folderPath = Paths.get(appData, folderName);
        String applicationPath = folderPath.toString().replace("\\","/") + "/";
        return new File(applicationPath);
    }
}