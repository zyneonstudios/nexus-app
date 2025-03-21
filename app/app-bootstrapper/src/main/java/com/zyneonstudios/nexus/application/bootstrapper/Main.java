package com.zyneonstudios.nexus.application.bootstrapper;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.File;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class Main {

    private static String path = getDefaultFolder().getAbsolutePath().replace("\\","/")+"/";
    private static boolean disableOutput = false;
    private static String[] arguments;

    public static void main(String[] args) {
        resolveArgruments(args);
        CompletableFuture.runAsync(() -> {
            try {
                new ServerSocket(49969).accept();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Another instance of this application is already open.");
                System.exit(-1);
            }
        });
        
        try {
            FlatDarkLaf.setup();
            UIManager.setLookAndFeel(FlatDarkLaf.class.getName());
        } catch (Exception ignore) {}
        new NexusBootstrapper(path);
    }

    private static void resolveArgruments(String[] args) {
        int i = 0;
        for(String arg : args) { i++;
            if(arg.equals("--path")||arg.equals("-p")) {
                try {
                    String path = args[i].replace("\\","/");
                    if(!path.endsWith("/")) {
                        path = path+"/";
                    }
                    Main.path = path;
                } catch (Exception e) {
                    System.err.println("Error resolving path argument: " + e.getMessage());
                    System.exit(-1);
                }
            } else if(arg.equals("--disableOutput")||arg.equals("-dO")) {
                disableOutput = true;
            }
        }
        arguments = args;
    }

    public static File getDefaultFolder() {
        String folderName = "Zyneon/NEXUS App";
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

    public static String[] getArguments() {
        return arguments;
    }

    public static String getRunningPath() {
        return path;
    }

    public static boolean isOutputDisabled() {
        return disableOutput;
    }
}