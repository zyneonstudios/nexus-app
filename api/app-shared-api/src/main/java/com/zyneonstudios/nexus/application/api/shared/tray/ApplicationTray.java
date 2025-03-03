package com.zyneonstudios.nexus.application.api.shared.tray;

import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.shared.events.ApplicationEvent;
import com.zyneonstudios.nexus.application.api.shared.events.EventType;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Objects;

public class ApplicationTray {

    private final HashMap<String, ApplicationModuleMenu> moduleMenus = new HashMap<>();

    private JMenu modules;
    private ApplicationMenuItem toggleVisibility;
    private NexusApplication application;
    private ApplicationPopupMenu popup;

    public ApplicationTray(NexusApplication application) {
        try {
            this.application = application;
            this.popup = new ApplicationPopupMenu();
            initializeMenu();
            TrayIcon trayIcon = new TrayIcon(getIcon().getImage(), "NEXUS App");
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    togglePopup(e);
                }
                boolean isPopupVisible = false;
                private void togglePopup(MouseEvent e) {
                    isPopupVisible = !isPopupVisible;
                    if(isPopupVisible) {
                        popup.setLocation(e.getX(), e.getY());
                        popup.setInvoker(popup);
                    }
                    popup.setVisible(isPopupVisible);
                }
            });
            SystemTray.getSystemTray().add(trayIcon);
        } catch (Exception e) {
            NexusDesktop.getLogger().err(e.getMessage());
        }
        for(ApplicationEvent e : SharedAPI.getEvents(EventType.TRAY_INITIALIZE_EVENT)) {
            e.execute();
        }
    }

    private String getVisibilityLabel() {
        if(application.getFrame().isVisible()) {
            return "Hide app";
        }
        return "Show app";
    }

    private void initializeMenu() {
        initializeTitle();
        popup.addSeparator();
        initializeVisiblityToggle();
        initializeReloadApp();
        popup.addSeparator();
        initializeRestartApp();
        initializeSoftRestartApp();
        initializeAppExit();
        popup.addSeparator();
        initializeModules();
        initializeCloseMenu();
    }

    private void initializeTitle() {
        ApplicationMenuItem title = new ApplicationMenuItem();
        title.setText("NEXUS App "+ApplicationStorage.getApplicationVersion().split("-")[0]);
        title.setIcon(getIcon());
        title.addActionListener(e -> {
            application.getFrame().setLocationRelativeTo(null);
            application.getFrame().setVisible(true);
            application.getFrame().setState(Frame.NORMAL);
            application.getFrame().requestFocus();
            toggleVisibility.setText(getVisibilityLabel());
        });
        popup.add(title);
    }

    private void initializeVisiblityToggle() {
        toggleVisibility = new ApplicationMenuItem();
        toggleVisibility.setText(getVisibilityLabel());
        toggleVisibility.addActionListener(e -> {
            application.getFrame().setVisible(!application.getFrame().isVisible());
            toggleVisibility.setText(getVisibilityLabel());
            if(application.getFrame().isVisible()) {
                application.getFrame().setState(Frame.NORMAL);
            }
        });
        popup.add(toggleVisibility);
    }

    private void initializeReloadApp() {
        ApplicationMenuItem reloadApp = new ApplicationMenuItem();
        reloadApp.setText("Reload app");
        reloadApp.addActionListener(e -> application.getFrame().getBrowser().loadURL(ApplicationStorage.urlBase+ApplicationStorage.language+"/"+ApplicationStorage.startPage));
        popup.add(reloadApp);
    }

    private void initializeRestartApp() {
        ApplicationMenuItem restartApp = new ApplicationMenuItem();
        restartApp.setText("Restart app");
        restartApp.addActionListener(e -> SwingUtilities.invokeLater(()-> application.restart(false)));
        popup.add(restartApp);
    }

    private void initializeSoftRestartApp() {
        ApplicationMenuItem softRestartApp = new ApplicationMenuItem();
        softRestartApp.setText("Soft restart app");
        softRestartApp.addActionListener(e -> SwingUtilities.invokeLater(()-> application.restart(true)));
        popup.add(softRestartApp);
    }

    private void initializeAppExit() {
        ApplicationMenuItem exitApp = new ApplicationMenuItem();
        exitApp.setText("Exit app");
        exitApp.addActionListener(e -> SwingUtilities.invokeLater(()-> NexusApplication.stop(true)));
        popup.add(exitApp);
    }

    private void initializeModules() {
        modules = new JMenu("Modules");
        modules.setBackground(Color.BLACK);
        modules.getPopupMenu().setBackground(Color.BLACK);
        modules.setVisible(false);
        popup.add(modules);
    }

    private void initializeCloseMenu() {
        ApplicationMenuItem closeMenu = new ApplicationMenuItem();
        closeMenu.setText("Close menu");
        closeMenu.addActionListener(e -> popup.setVisible(false));
        popup.add(closeMenu);
    }



    public ImageIcon getIcon() {
        try {
            return new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    public void addModulePopup(ApplicationModuleMenu menu) {
        moduleMenus.put(menu.getId(), menu);
        modules.add(moduleMenus.get(menu.getId()));
        modules.setVisible(true);
    }

    public void removeModulePopup(ApplicationModuleMenu menu) {
        if(moduleMenus.containsKey(menu.getId())) {
            modules.remove(moduleMenus.get(menu.getId()));
            moduleMenus.remove(menu.getId());
            if (moduleMenus.isEmpty()) {
                modules.setVisible(false);
            }
        }
    }
}