package com.zyneonstudios.nexus.application.api;

import com.zyneonstudios.nexus.application.api.discover.Discover;
import com.zyneonstudios.nexus.application.api.discover.events.*;
import com.zyneonstudios.nexus.application.api.discover.search.DiscoverSearch;
import com.zyneonstudios.nexus.application.api.discover.search.Search;
import com.zyneonstudios.nexus.application.api.discover.search.SearchHandler;
import com.zyneonstudios.nexus.application.api.discover.search.zyndex.ModuleSearch;
import com.zyneonstudios.nexus.application.api.shared.api.ApplicationAPI;
import com.zyneonstudios.nexus.application.api.shared.body.elements.*;
import com.zyneonstudios.nexus.application.api.shared.events.ElementActionEvent;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.NexusDesktop;
import com.zyneonstudios.nexus.index.ReadableZyndex;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscoverAPI implements ApplicationAPI {

    private NexusApplication application;
    private static final HashMap<DiscoverEventType, ArrayList<DiscoverEvent>> events = new HashMap<>();
    private static Discover discover = null;
    private static ReadableZyndex nex;

    @Override
    public void load(NexusApplication application) {
        this.application = application;
    }

    @Override
    public void enable() {
        discover = new Discover(application);

        try {
            nex = new ReadableZyndex("https://zyneonstudios.github.io/nexus-nex/zyndex/index.json");
            ModuleSearch moduleSearch = new ModuleSearch(nex);
            moduleSearch.setId("modules",true);
            moduleSearch.setName("Official modules",true);
            discover.getSearch().addSearchSource(moduleSearch);
        } catch (Exception e) {
            NexusDesktop.getLogger().err("Couldn't load official Zyndex \"NEX\": "+e.getMessage());
            NexusDesktop.getLogger().err("Disabled module search...");
        }

        registerEvent(new DiscoverLoadEvent() {
            @Override
            public boolean onLoad() {
                return discover.load();
            }
        });

        registerEvent(new DiscoverOpenSearchEvent() {
            @Override
            public boolean onOpen() {
                DiscoverSearch search = getDiscover().getSearch();
                for(Search source:discover.getSearch().getSearchSources()) {
                    application.getFrame().executeJavaScript("document.getElementById('search-type-select').innerHTML += \"<option value='"+source.getId()+"'>"+source.getName()+"</option>\"");
                }
                if(search.getActiveSearchSource()==null) {
                    search.setActiveSearchSource(search.getSearchSources()[0]);
                }
                application.getFrame().executeJavaScript("document.getElementById('search-type-select').value = \""+search.getActiveSourceId()+"\";");
                application.getFrame().executeJavaScript("moduleId = \""+search.getActiveSourceId()+"\";");
                return true;
            }
        });

        registerEvent(new SearchHandler());

        BodyImage descriptionImage = new BodyImage();
        descriptionImage.setAlt("NEXUS App logo");
        descriptionImage.setSrc("../assets/application/images/logos/app/normal.png");

        BodyButton descriptionDiscord = new BodyButton();
        descriptionDiscord.setText("Discord");
        descriptionDiscord.addActionEvent(new ElementActionEvent() {
            @Override
            public boolean onAction() {
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://discord.gg/Q2AEWfesZW"));
                    } catch (Exception e) {
                        NexusDesktop.getLogger().err(e.getMessage());
                    }
                }
                return false;
            }
        });

        BodyButton descriptionWebsite = new BodyButton();
        descriptionWebsite.setText("Website");
        descriptionWebsite.addActionEvent(new ElementActionEvent() {
            @Override
            public boolean onAction() {
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://nexus.zyneonstudios.com"));
                    } catch (Exception e) {
                        NexusDesktop.getLogger().err(e.getMessage());
                    }
                }
                return false;
            }
        });

        BodyTextCard descriptionCard = new BodyTextCard();
        descriptionCard.setTitle("About NEXUS App");
        descriptionCard.setText("The NEXUS App ist a modular and easy extensible cross os desktop app which allows everyone to use it for literally anything. (If they can code or find a module)");
        descriptionCard.addButton(descriptionDiscord);
        descriptionCard.addButton(descriptionWebsite);

        BodyRow descriptionRow = new BodyRow();
        descriptionRow.addElement(descriptionImage);
        descriptionRow.addElement(descriptionCard);

        BodyButton actionsReload = new BodyButton();
        actionsReload.setText("Reload app");
        actionsReload.addActionEvent(new ElementActionEvent() {
            @Override
            public boolean onAction() {
                SharedAPI.openAppPage(ApplicationStorage.startPage);
                return true;
            }
        });

        BodyButton actionsRestartSoft = new BodyButton();
        actionsRestartSoft.setText("Restart app (soft)");
        actionsRestartSoft.addActionEvent(new ElementActionEvent(actionsRestartSoft.getUUID()) {
            @Override
            public boolean onAction() {
                SwingUtilities.invokeLater(()->{
                    application.restart(true);
                });
                return true;
            }
        });

        BodyButton actionsRestart = new BodyButton();
        actionsRestart.setText("Restart app (hard)");
        actionsRestart.addActionEvent(new ElementActionEvent(actionsRestart.getUUID()) {
            @Override
            public boolean onAction() {
                SwingUtilities.invokeLater(()->{
                    application.restart(false);
                });
                return true;
            }
        });

        BodyActionCard actionsCard = new BodyActionCard();
        actionsCard.setTitle("Quick actions");
        actionsCard.addButton(actionsReload);
        actionsCard.addButton(actionsRestartSoft);
        actionsCard.addButton(actionsRestart);

        BodyImage nexusImage = new BodyImage();
        nexusImage.setAlt("Zyneon NEXUS logo");
        nexusImage.setSrc("../assets/application/images/logos/nexus.png");

        BodyRow actionsRow = new BodyRow();
        actionsRow.addElement(actionsCard);
        actionsRow.addElement(nexusImage);

        BodyPage discoverPage = new BodyPage();
        discoverPage.setActive(true);
        discoverPage.setId("home");
        discoverPage.setTitle("Home");
        discoverPage.addElement(descriptionRow);
        discoverPage.addElement(actionsRow);

        try {
            discover.addPage(discoverPage);
        } catch (Exception ignore) {}
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

    public static ReadableZyndex getNEX() {
        return nex;
    }
}