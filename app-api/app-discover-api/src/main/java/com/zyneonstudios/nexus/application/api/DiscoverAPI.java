package com.zyneonstudios.nexus.application.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.api.discover.Discover;
import com.zyneonstudios.nexus.application.api.discover.body.elements.*;
import com.zyneonstudios.nexus.application.api.discover.events.*;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<DiscoverEventType, ArrayList<DiscoverEvent>> events = new HashMap<>();
    private static Discover discover = null;

    @Override
    public void load(NexusApplication application) {
        this.application = application;
    }

    @Override
    public void enable() {
        discover = new Discover(application);
        registerEvent(new DiscoverLoadEvent() {
            @Override
            public boolean onLoad() {
                return discover.load();
            }
        });

        registerEvent(new DiscoverSearchEvent() {
            @Override
            public boolean onSearch() {
                System.out.println(getSourceId() + " " + getOffset() + " " + getQuery());
                return true;
            }
        });

        DiscoverImage descriptionImage = new DiscoverImage();
        descriptionImage.setAlt("NEXUS App logo");
        descriptionImage.setSrc("../assets/application/images/logos/app/normal.png");

        DiscoverButton descriptionDiscord = new DiscoverButton();
        descriptionDiscord.setText("Discord");

        DiscoverButton descriptionWebsite = new DiscoverButton();
        descriptionWebsite.setText("Website");

        DiscoverTextCard descriptionCard = new DiscoverTextCard();
        descriptionCard.setTitle("About NEXUS App");
        descriptionCard.setText("The NEXUS App ist a modular and easy extensible cross platform desktop app which allows everyone to use it for literally anything.");
        descriptionCard.addButton(descriptionDiscord);
        descriptionCard.addButton(descriptionWebsite);

        DiscoverRow descriptionRow = new DiscoverRow();
        descriptionRow.addElement(descriptionImage);
        descriptionRow.addElement(descriptionCard);

        DiscoverButton actionsReload = new DiscoverButton();
        actionsReload.setText("Reload app");

        DiscoverButton actionsRestartSoft = new DiscoverButton();
        actionsRestartSoft.setText("Restart app (soft)");
        actionsRestartSoft.addActionEvent(new DiscoverActionEvent(actionsRestartSoft.getUUID()) {
            @Override
            public boolean onAction() {
                SwingUtilities.invokeLater(()->{
                    application.restart(true);
                });
                return true;
            }
        });

        DiscoverButton actionsRestart = new DiscoverButton();
        actionsRestart.setText("Restart app (hard)");
        actionsRestart.addActionEvent(new DiscoverActionEvent(actionsRestart.getUUID()) {
            @Override
            public boolean onAction() {
                SwingUtilities.invokeLater(()->{
                    application.restart(false);
                });
                return true;
            }
        });

        DiscoverActionCard actionsCard = new DiscoverActionCard();
        actionsCard.setTitle("Quick actions");
        actionsCard.addButton(actionsReload);
        actionsCard.addButton(actionsRestartSoft);
        actionsCard.addButton(actionsRestart);

        DiscoverImage nexusImage = new DiscoverImage();
        nexusImage.setAlt("Zyneon NEXUS logo");
        nexusImage.setSrc("../assets/application/images/logos/nexus.png");

        DiscoverRow actionsRow = new DiscoverRow();
        actionsRow.addElement(actionsCard);
        actionsRow.addElement(nexusImage);

        DiscoverPage discoverPage = new DiscoverPage();
        discoverPage.setActive(true);
        discoverPage.setId("home");
        discoverPage.setTitle("Home");
        discoverPage.addElement(descriptionRow);
        discoverPage.addElement(actionsRow);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().fromJson(discoverPage.getJson(), JsonObject.class));
        System.out.println(discoverPage.getHTML());

        try {
            discover.addPage(discoverPage);
        } catch (Exception ignore) {}
    }

    @Override
    public void shutdown() {

    }

    public static void registerEvent(DiscoverEvent event) {
        if(!events.containsKey(event.getType())) {
            events.put(event.getType(), new ArrayList<>());
        }
        events.get(event.getType()).add(event);
    }

    public static ArrayList<DiscoverEvent> getEvents(DiscoverEventType eventType) {
        if(!events.containsKey(eventType)) {
            events.put(eventType, new ArrayList<>());
        }
        return events.get(eventType);
    }

    public static Discover getDiscover() {
        return discover;
    }
}