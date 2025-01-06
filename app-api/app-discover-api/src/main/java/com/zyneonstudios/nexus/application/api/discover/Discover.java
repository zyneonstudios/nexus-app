package com.zyneonstudios.nexus.application.api.discover;

import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.discover.body.elements.DiscoverPage;
import com.zyneonstudios.nexus.application.api.discover.exceptions.PageAlreadyExistsException;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.Collection;
import java.util.HashMap;

public class Discover {

    private final HashMap<String, DiscoverPage> pages = new HashMap<>();
    private final NexusApplication application;
    private String query = "";

    public Discover(NexusApplication application) {
        this.application = application;
    }

    public Collection<DiscoverPage> getPages() {
        return pages.values();
    }

    public void addPage(DiscoverPage page) throws PageAlreadyExistsException {
        if(pages.containsKey(page.getID())) {
            throw new PageAlreadyExistsException(page.getID(),"");
        }
        pages.put(page.getID(),page);
    }

    public void removePage(DiscoverPage page) {
        removePage(page.getID());
    }

    public void removePage(String pageId) {
        pages.remove(pageId);
    }

    public boolean load() {
        for(DiscoverPage page : pages.values()) {
            initPage(page);
        }
        return true;
    }

    private void initPage(DiscoverPage page) {
        application.getFrame().executeJavaScript("document.getElementById(\"discover-start\").innerHTML += \""+page.getHTML()+"\"","document.getElementById(\"discover-buttons\").innerHTML += \"<h3 id='"+page.getID()+"-button' class='discover-button' onclick='showTab(`"+page.getID()+"`);'>"+page.getTitle()+"</h3>\";");
        if(page.isActive()) {
            application.getFrame().executeJavaScript("showTab('"+page.getID()+"');");
        }
    }

    public void open() {
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html");
    }

    public void open(String parameters) {
        if(!parameters.startsWith("?")) {
            if(parameters.startsWith("&")) {
                parameters = parameters.replaceFirst("&","?");
            } else {
                parameters = "?" + parameters;
            }
        }
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html"+parameters);
    }

    public void open(Object... parameters) {
        StringBuilder url = new StringBuilder(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html");
        String spacer = "?";
        for(Object parameter : parameters) {
            url.append(spacer).append(parameter.toString().replace("&", "").replace("?", ""));
            spacer = "&";
        }
        SharedAPI.openFrameUrl(url.toString());
    }

    public void openSearch() {
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search");
    }

    public void openSearch(String query) {
        SharedAPI.openFrameUrl(ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search&s="+query);
        this.query = query;
    }

    public void openSearch(String query, String searchTypeId) {
        String url = ApplicationStorage.urlBase + ApplicationStorage.language + "/discover.html?l=search&moduleId="+searchTypeId;
        if(query!=null&&!query.isEmpty()) {
            url = url+"&s="+query;
            this.query = query;
        }
        SharedAPI.openFrameUrl(url);
    }
}
