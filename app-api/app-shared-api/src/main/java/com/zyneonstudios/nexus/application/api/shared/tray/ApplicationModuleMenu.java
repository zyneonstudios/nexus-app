package com.zyneonstudios.nexus.application.api.shared.tray;

import com.zyneonstudios.nexus.application.api.shared.modules.ApplicationModule;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class ApplicationModuleMenu extends JMenu {

    private final ApplicationModule module;
    private final UUID uuid = UUID.randomUUID();

    public ApplicationModuleMenu(ApplicationModule module) {
        setBackground(Color.BLACK);
        getPopupMenu().setBackground(Color.BLACK);
        setText(module.getName());
        this.module = module;
    }

    public String getId() {
        return uuid.toString();
    }

    public ApplicationModule getModule() {
        return module;
    }
}