package com.zyneonstudios.nexus.application.frame;

import com.zyneonstudios.nexus.application.Main;
import com.zyneonstudios.nexus.application.api.DiscoverAPI;
import com.zyneonstudios.nexus.application.api.LibraryAPI;
import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverEvent;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverEventType;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverInstallEvent;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverSearchEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEvent;
import com.zyneonstudios.nexus.application.api.library.events.LibraryEventType;
import com.zyneonstudios.nexus.application.api.library.events.LibraryPreLoadEvent;
import com.zyneonstudios.nexus.application.api.shared.events.ApplicationEvent;
import com.zyneonstudios.nexus.application.api.shared.events.ElementActionEvent;
import com.zyneonstudios.nexus.application.api.shared.events.EventType;
import com.zyneonstudios.nexus.application.download.Download;
import com.zyneonstudios.nexus.application.frame.web.ApplicationFrame;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FrameConnector {

    private final ApplicationFrame frame;
    private final NexusApplication application;

    public FrameConnector(ApplicationFrame frame,NexusApplication application) {
        this.application = application;
        this.frame = frame;
    }

    public void resolveRequest(String request) {
        if(ApplicationStorage.test) {
            NexusApplication.getLogger().err("[CONNECTOR] (Request-Reader) resolving "+request+"...");
        } else {
            NexusApplication.getLogger().dbg("[CONNECTOR] (Request-Reader) resolving "+request+"...");
        }
        if(request.startsWith("event.")) {
            request = request.replace("event.","");
            if(request.startsWith("shared.")) {
                String event = request.replace("shared.","");
                if(event.startsWith("action.")) {
                    String id = event.replace("action.", "");
                    for (ApplicationEvent event_ : SharedAPI.getEvents(EventType.ELEMENT_ACTION_EVENT)) {
                        ElementActionEvent event__ = (ElementActionEvent) event_;
                        if (event__.getUUID().toString().equals(id)) {
                            event__.execute();
                        }
                    }
                } else if(event.equals("page.loaded")) {
                    for(ApplicationEvent events : SharedAPI.getEvents(EventType.PAGE_LOADED_EVENT)) {
                        events.execute();
                    }
                }
            } else if(request.startsWith("library.")) {
                String event = request.replace("library.","");
                if(event.equals("load")) {
                    frame.executeJavaScript("closeMenu();");
                    for(LibraryEvent events : LibraryAPI.getEvents(LibraryEventType.LIBRARIES_LOAD_EVENT)) {
                        events.execute();
                    }
                    for(LibraryEvent events : LibraryAPI.getEvents(LibraryEventType.LIBRARY_PRELOAD_EVENT)) {
                        if(LibraryAPI.getActiveLibrary()!=null) {
                            LibraryPreLoadEvent event_ = (LibraryPreLoadEvent)events;
                            if(event_.getLibrary().equals(LibraryAPI.getActiveLibrary())) {
                                if(event_.execute()) {
                                    event_.getLibrary().load();
                                }
                            }
                        }
                    }
                }
            } else if(request.startsWith("discover.")) {
                String event = request.replace("discover.","");
                if(event.equals("pre")) {
                    for(DiscoverEvent e : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_PRE_LOAD_EVENT)) {
                        e.execute();
                    }
                } else if(event.equals("load")) {
                    for(DiscoverEvent e : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_LOAD_EVENT)) {
                        e.execute();
                    }
                } else if(event.equals("post")) {
                    for(DiscoverEvent e : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_LOADED_EVENT)) {
                        e.execute();
                    }
                } else if(event.startsWith("install.")) {
                    String id = event.replace("install.", "");
                    for(DiscoverEvent event_ : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_INSTALL_EVENT)) {
                        DiscoverInstallEvent event__ = (DiscoverInstallEvent) event_;
                        if(event__.getUUID().toString().equals(id)) {
                            event__.execute();
                        }
                    }
                } else if(event.equals("search")) {
                    for(DiscoverEvent e : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_OPEN_SEARCH_EVENT)) {
                        e.execute();
                    }
                } else if(event.startsWith("search.")) {
                    for(DiscoverEvent e : DiscoverAPI.getEvents(DiscoverEventType.DISCOVER_SEARCH_EVENT)) {
                        DiscoverSearchEvent searchEvent = (DiscoverSearchEvent)e;
                        String[] search = request.replaceFirst("discover.search.","").split("\\.",3);

                        searchEvent.setSourceId(search[0]);
                        searchEvent.setOffset(Integer.parseInt(search[1]));
                        try {
                            searchEvent.setQuery(search[2]);
                        } catch (Exception ex) {
                            searchEvent.setQuery("");
                        }
                        searchEvent.execute();
                    }
                }
            } else if(request.startsWith("modules.")) {
                String event = request.replace("modules.","");
            }
        } else if(request.startsWith("sync.")) {
            sync(request.replace("sync.", ""));
        } else if(request.startsWith("open.")) {
            open(request.replaceFirst("open.",""));
        } else if(request.startsWith("init.")) {
            init(request.replace("init.", ""));
        } else if(request.startsWith("load.")) {
            load(request.replace("load.", ""));
        }

        if(NexusApplication.getDownloadManager().getDownloads().isEmpty()) {
            frame.executeJavaScript("document.getElementById('downloads-button').style.display = 'none';");
        }
    }

    private void open(String request) {
        if(request.startsWith("url.")) {
            request = request.replaceFirst("url.", "");
            if(request.startsWith("decode.")) {
                request = URLDecoder.decode(request.replaceFirst("decode.", ""), StandardCharsets.UTF_8);
            }
            if(Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(request));
                } catch (Exception ignore) {}
            }
        }
    }

    private void load(String request) {
        switch (request) {
            case "drive" ->
                    open("url.https://cloud.nrfy.net");
            case "discover" ->
                    frame.getBrowser().loadURL(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html");
            case "downloads" ->
                    frame.getBrowser().loadURL(ApplicationStorage.urlBase + ApplicationStorage.language + "/downloads.html");
            case "library" ->
                    frame.getBrowser().loadURL(ApplicationStorage.urlBase + ApplicationStorage.language + "/library.html");
            case "settings" ->
                    frame.getBrowser().loadURL(ApplicationStorage.urlBase + ApplicationStorage.language + "/settings.html");
        }
    }

    private void init(String request) {
        frame.executeJavaScript("syncDesktop();");
        if(request.equals("discover")) {
            frame.executeJavaScript("setMenuPanel('','"+ ApplicationStorage.getApplicationVersion()+" ("+ApplicationStorage.getNexusVersion()+")','"+ ApplicationStorage.getApplicationName()+"',true);");
            if(frame.getBrowser().getURL().contains("&l=search")||frame.getBrowser().getURL().contains("?l=search")) {
                frame.executeJavaScript("deactivateMenu('menu',true);");
            } else {
                frame.executeJavaScript("activateMenu('menu',true); document.getElementById('search-bar').disabled = false; document.getElementById('search-bar').placeholder = searchTerm;");
                frame.executeJavaScript("document.querySelector('.fes').innerHTML = \"<i class='bx bxs-bug-alt'></i> Force disable search\"; document.querySelector('.fes').onclick = function () { document.getElementById('search-bar').disabled = true; document.getElementById('search-bar').placeholder = 'Search disabled'; }");
            }
        } else {
            frame.executeJavaScript("deactivateMenu('menu',true);");
        }
        if(request.equals("settings")) {
            frame.executeJavaScript("syncVersion(\""+ ApplicationStorage.getApplicationVersion().replace("\"","''")+"\",\" ("+ApplicationStorage.getNexusVersion()+")\");");
        } else if(request.equals("downloads")) {
            for(Download download:NexusApplication.getDownloadManager().getDownloads().values()) {
                frame.executeJavaScript("setDownload(\""+download.getName().replace("\"","''")+"\",'"+download.getState().toString()+"','"+download.getElapsedTime().getSeconds()+"','"+download.getSpeedMbps()+"','"+download.getEstimatedRemainingTime().getSeconds()+"','"+download.getFileSize()+"','"+download.getLastBytesRead()+"',\""+download.getPath().toString().replace("\\","/")+"\",\""+download.getUrl().toString().replace("\\","/")+"\",\""+download.getUuid()+"\",'"+download.getPercentString()+"','"+download.getPercent()+"');");
            }
        }
    }

    private void sync(String request) {
        if(request.startsWith("title.")) {
            if(ApplicationStorage.hasDriveAccess()) {
                frame.executeJavaScript("document.getElementById('drive-button').style.display = 'flex';");
            }
            String[] request_ = request.replace("title.","").split("-.-",2);
            Color background;
            Color foreground;
            String fReq = request_[0];
            if(request_[0].equalsIgnoreCase("../assets/cronos/css/app-colors-dark.css")) {
                background = Color.black;
                foreground = Color.white;
            } else if(request_[0].equalsIgnoreCase("../assets/cronos/css/app-colors-light.css")) {
                background = Color.white;
                foreground = Color.black;
            } else if(request_[0].equalsIgnoreCase("../assets/application/css/app-colors-oled.css")) {
                background = Color.black;
                foreground = Color.white;
            } else if(request_[0].startsWith("automatic-")) {
                request = request_[0].replaceFirst("automatic-","");
                if(request.equals("dark")) {
                    background = Color.black;
                    foreground = Color.white;
                } else {
                    background = Color.white;
                    foreground = Color.black;
                }
                fReq = "automatic";
            } else {
                background = Color.decode("#0a0310");
                foreground = Color.white;
            }
            if(!ApplicationStorage.theme.equalsIgnoreCase(fReq)) {
                ApplicationStorage.theme = fReq;
                ApplicationStorage.getSettings().set("settings.theme", ApplicationStorage.theme);
            }
            String title = request_[1];
            frame.setTitlebar(title,background,foreground);
        } else if(request.startsWith("firstrun.")) {
            request = request.replaceFirst("firstrun.","");
            if(request.equals("theme")) {
                frame.executeJavaScript("initThemeWizard();");
                if(ApplicationStorage.getOS().startsWith("Linux")) {
                    boolean customFrame = true;
                    if(ApplicationStorage.getSettings().get("settings.linux.customFrame")!=null) {
                        try {
                            customFrame = ApplicationStorage.getSettings().getBool("settings.linux.customFrame");
                        } catch (Exception ignore) {}
                    }
                    frame.executeJavaScript("initLinuxWizard('"+customFrame+"');");
                }
            } else if(request.startsWith("linuxFrame.")) {
                request = request.replace("linuxFrame.","");
                boolean frame = request.equals("on");
                ApplicationStorage.getSettings().set("settings.linux.customFrame",frame);
                ApplicationStorage.getSettings().set("cache.restartPage","firstrun.html?theme.colors="+ ApplicationStorage.theme+"#linux");
                application.restart(true);
            } else if(request.equals("finished")) {
                ApplicationStorage.getSettings().set("settings.setupFinished",true);
            }
        } else if(request.equals("exit")) {
            SwingUtilities.invokeLater(()->NexusApplication.stop(true));
        } else if(request.equals("refresh")) {
            frame.getBrowser().loadURL(ApplicationStorage.urlBase+ ApplicationStorage.language+"/"+ ApplicationStorage.startPage);
        } else if(request.equals("restart")) {
            SwingUtilities.invokeLater(()->application.restart(false));
        } else if(request.startsWith("settings.")) {
            syncSettings(request.replaceFirst("settings.",""));
        } else if(request.startsWith("autoUpdates.")) {
            request = request.replace("autoUpdates.","");
            boolean update = request.equals("on");
            ApplicationStorage.getUpdateSettings().set("bootstrapper.config.checkForUpdates",update);
        } else if(request.startsWith("linuxFrame.")) {
            request = request.replace("linuxFrame.","");
            boolean frame = request.equals("on");
            ApplicationStorage.getSettings().set("settings.linux.customFrame",frame);
            ApplicationStorage.getSettings().set("cache.restartPage","settings.html");
            application.restart(true);
        } else if(request.startsWith("discover.")) {
            syncDiscover(request.replaceFirst("discover.",""));
        } else if(request.startsWith("updateChannel.")) {
            request = request.replace("updateChannel.","");
            ApplicationStorage.getUpdateSettings().set("bootstrapper.data.channel",request);
        } else if(request.startsWith("startPage.")) {
            request = request.replaceFirst("startPage.","");
            ApplicationStorage.startPage = request;
            ApplicationStorage.getSettings().set("settings.startPage",request);
        } else if(request.startsWith("zoomLevel.")) {
            double d = Double.parseDouble(request.replaceFirst("zoomLevel.",""));
            ApplicationStorage.setZoomLevel(d);
        } else if(request.startsWith("language.")) {
            request = request.replaceFirst("language.","");
            ApplicationStorage.language = request;
            ApplicationStorage.getSettings().set("settings.language",request);
        }
    }

    private void syncSettings(String request) {
        switch (request) {
            case "general" -> {
                String channel = "stable";
                boolean autoUpdate = false;
                if (ApplicationStorage.getUpdateSettings().getBoolean("bootstrapper.config.checkForUpdates") != null) {
                    autoUpdate = ApplicationStorage.getUpdateSettings().getBool("bootstrapper.config.checkForUpdates");
                }
                if (ApplicationStorage.getUpdateSettings().getString("bootstrapper.data.channel") != null) {
                    channel = ApplicationStorage.getUpdateSettings().getString("bootstrapper.data.channel");
                }
                if (ApplicationStorage.getOS().startsWith("Linux")) {
                    boolean linuxCustomFrame = true;
                    if (ApplicationStorage.getSettings().get("settings.linux.customFrame") != null) {
                        linuxCustomFrame = ApplicationStorage.getSettings().getBool("settings.linux.customFrame");
                    }
                    frame.executeJavaScript("document.getElementById('linux-settings-custom-frame').style.display = 'inherit'; linuxFrame = " + linuxCustomFrame + "; document.getElementById('linux-settings-enable-custom-frame').checked = linuxFrame;");
                }
                if (autoUpdate) {
                    frame.executeJavaScript("document.getElementById('updater-settings-enable-updates').classList.add('active');");
                }
                frame.executeJavaScript("updates = " + autoUpdate + "; document.getElementById('updater-settings-update-channel').value = \"" + channel + "\"; document.getElementById('updater-settings').style.display = 'inherit'; document.getElementById('general-settings-start-page').value = '" + ApplicationStorage.startPage + "'; document.getElementById('updater-settings').style.display = 'inherit';");
            }
            case "indexes" ->
                    frame.executeJavaScript("if(!document.getElementById('indexes-group-default').innerHTML.includes('Zyneon NEX')) { document.getElementById('indexes-group-default').innerHTML += \"<h3>Zyneon NEX <span class='buttons'><span>Official</span><a onclick='openUrl(`https://github.com/zyneonstudios/nexus-nex`);'>GitHub</a><a onclick='openUrl(`https://nexus.zyneonstudios.net/nex/`);'>Open</a></span></h3>\"}");
            case "about" ->
                    frame.executeJavaScript("document.getElementById('settings-global-application-version').innerText = \"" + ApplicationStorage.getApplicationVersion() + "\"");
        }
    }

    private void syncDiscover(String request) {

    }

    @SuppressWarnings("all")
    public static String initDetails(String name, String id, String type, String version, String summary, String authors, boolean isHidden, String tags, String description, String changelog, String versions, String customInfoHTML, String customInfoCardHTML, String background, String icon, String logo, String thumbnail) {
        String url = ApplicationStorage.urlBase+ ApplicationStorage.language+"/sub-details.html";
        if(name!=null) {
            url = url+"?name="+formatForDetails(name);
        }
        if(id!=null) {
            url = url+"&id="+formatForDetails(id);
        }
        if(type!=null) {
            url = url+"&type="+formatForDetails(type);
        }
        if(version!=null) {
            url = url+"&version="+formatForDetails(version);
        }
        if(summary!=null) {
            url = url+"&summary="+formatForDetails(summary);
        }
        if(authors!=null) {
            url = url+"&authors="+formatForDetails(authors);
        }
        url = url+"&hidden="+isHidden;
        if(tags!=null) {
            url = url+"&tags="+formatForDetails(tags.replace("[","").replace("]",""));
        }
        if(description!=null) {
            url = url+"&description="+formatForDetails(description);
        } else {
            if(summary!=null) {
                url = url+"&description="+formatForDetails(summary);
            }
        }
        if(changelog!=null) {
            url = url+"&changelog="+formatForDetails(changelog.replace("[","").replace("]","").replace(", ",","));
        }
        if(versions!=null) {
            url = url+"&versions="+formatForDetails(versions.replace("[","").replace("]","").replace(", ",","));
        }
        if(customInfoHTML!=null) {
            url = url+"&c="+formatForDetails(customInfoHTML);
        }
        if(customInfoCardHTML!=null) {
            url = url+"&cc="+formatForDetails(customInfoCardHTML);
        }
        if(background!=null) {
            String b = "&background="+formatForDetails(background);
            url = url+b;
        }
        if(icon!=null) {
            url = url+"&icon="+formatForDetails(icon);
        }
        if(logo!=null) {
            url = url+"&logo="+formatForDetails(logo);
        }
        if(thumbnail!=null) {
            String t = "&thumbnail="+formatForDetails(thumbnail);
            url = url+t;
        }
        url = url.replace("\\","/");
        return url;
    }

    public static String formatForDetails(String input) {
        if(input!=null) {
            if(!input.isBlank()) {
                input = input.replace("\"","''");
                input = input.replace("\n","<br>").replace("\\n","<br>").replace("+","%plus%");
                return URLEncoder.encode(input, StandardCharsets.UTF_8).replace("+", "%20").replace("%plus%", "+");
            }
            return "";
        }
        return null;
    }
}
