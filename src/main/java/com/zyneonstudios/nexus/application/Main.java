package com.zyneonstudios.nexus.application;

import com.zyneonstudios.nexus.application.frame.ZyneonSplash;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

public class Main {

    public static void main(String[] args) {
        NexusDesktop.init();
        ZyneonSplash splash = new ZyneonSplash();
        splash.setVisible(true);
        NexusApplication application = new NexusApplication();
        if(application.launch()) {
            splash.dispose();
            System.gc();
        } else {
            System.exit(-1);
        }
    }
}