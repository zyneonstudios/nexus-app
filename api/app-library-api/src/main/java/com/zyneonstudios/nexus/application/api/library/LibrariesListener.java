package com.zyneonstudios.nexus.application.api.library;

import com.zyneonstudios.nexus.application.api.LibraryAPI;
import com.zyneonstudios.nexus.application.api.library.events.LibrariesLoadEvent;
import com.zyneonstudios.nexus.application.main.NexusApplication;

public class LibrariesListener extends LibrariesLoadEvent {

    private final NexusApplication application;

    public LibrariesListener(NexusApplication application) {
        this.application = application;
    }

    @Override
    public boolean onLoad() {
        for(Library library : LibraryAPI.getLibraries().values()) {
            application.getFrame().executeJavaScript("addLibrary(\""+library.getLibraryName()+"\",\""+library.getLibraryId()+"\",null);");
            if(LibraryAPI.getActiveLibrary()==null) {
                LibraryAPI.setActiveLibrary(library);
            }
        }
        if(LibraryAPI.getActiveLibrary()!=null) {
            application.getFrame().executeJavaScript("document.getElementById(\"select-game-module\").value = \""+LibraryAPI.getActiveLibrary().getLibraryId()+"\";");
            Library library = LibraryAPI.getActiveLibrary();
            application.getFrame().executeJavaScript("addAction('Refresh','bx bx-refresh','location.reload();','refresh-"+library.getLibraryId()+"');","addGroup('Instances','"+library.getLibraryId()+"');");
            for(LibraryInstance instances:library.getLibraryInstances()) {
                if(instances.getIconUrl()!=null) {
                    application.getFrame().executeJavaScript("addGroupEntry('"+library.getLibraryId()+"',\""+instances.getName().replace("\"","''")+"\",\""+instances.getId().replace("\"","''")+"\",\""+instances.getIconUrl()+"\");");
                } else {
                    application.getFrame().executeJavaScript("addGroupEntry('"+library.getLibraryId()+"',\"" + instances.getName().replace("\"", "''") + "\",\"" + instances.getId().replace("\"", "''") + "\",);");
                }
            }
        }
        return true;
    }
}