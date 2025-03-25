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
        application.getFrame().executeJavaScript("document.getElementById(\"select-game-module\").value = \""+LibraryAPI.getActiveLibrary().getLibraryId()+"\";");
        return true;
    }
}