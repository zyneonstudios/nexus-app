package com.zyneonstudios.nexus.application;

import com.zyneonstudios.nexus.application.frame.ZyneonSplash;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

public class Main {

    private static String path = ".";

    public static void main(String[] args) {
        NexusDesktop.init();
        resolveArguments(args);
        ZyneonSplash splash = new ZyneonSplash();
        splash.setVisible(true);
        NexusApplication application = new NexusApplication(path);
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
            if(arg.equals("-p")||arg.equals("--path")) {
                if(i+1<args.length) {
                    path = args[i+1];
                }
            }
        }
    }
}