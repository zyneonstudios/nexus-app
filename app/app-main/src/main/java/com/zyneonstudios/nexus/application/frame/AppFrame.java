package com.zyneonstudios.nexus.application.frame;

import com.zyneonstudios.nexus.desktop.NexusDesktop;
import com.zyneonstudios.nexus.desktop.frame.nexus.NexusFrame;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends NexusFrame {

    private JPanel sideMenu = new JPanel();

    public AppFrame() {
        initSideMenu();
        setTitle("NEXUS Application");
        getContentPane().setBackground(Color.decode("#0a0a0a"));
        getTitlebar().setBackground(Color.decode("#1a1a1a"));
        getCloseButton().addActionListener(e -> System.exit(0));
        getCloseButton().setBackground(Color.decode("#1a1a1a"));
        getMinimizeButton().setBackground(Color.decode("#1a1a1a"));
    }

    public JPanel getSideMenu() {
        return sideMenu;
    }

    public void setSideMenu(JPanel sideMenu) {
        this.sideMenu = sideMenu;
    }

    private void initSideMenu() {
        sideMenu.setBackground(Color.decode("#ffffff"));
        getMainPane().add(sideMenu);
    }

    public static void main(String[] args) {
        NexusDesktop.init();
        new AppFrame().setVisible(true);
    }
}