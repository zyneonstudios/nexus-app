package com.zyneonstudios.nexus.application.api.discover;

import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.shared.body.elements.BodyPage;
import com.zyneonstudios.nexus.application.api.discover.exceptions.PageAlreadyExistsException;
import com.zyneonstudios.nexus.application.api.discover.search.DiscoverSearch;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.util.HashMap;

public class Discover {

    private final HashMap<String, BodyPage> pages = new HashMap<>();
    private final NexusApplication application;
    private final DiscoverSearch search;

    public Discover(NexusApplication application) {
        this.application = application;
        this.search =new DiscoverSearch(application.getFrame());
    }

    public BodyPage[] getPages() {
        return pages.values().toArray(new BodyPage[0]);
    }

    public void addPage(BodyPage page) throws PageAlreadyExistsException {
        if(pages.containsKey(page.getID())) {
            throw new PageAlreadyExistsException(page.getID(),"");
        }
        pages.put(page.getID(),page);
    }

    public void removePage(BodyPage page) {
        removePage(page.getID());
    }

    public void removePage(String pageId) {
        pages.remove(pageId);
    }

    public boolean load() {
        for(BodyPage page : pages.values()) {
            initPage(page);
        }
        return true;
    }

    private void initPage(BodyPage page) {
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

    public DiscoverSearch getSearch() {
        return search;
    }
}
