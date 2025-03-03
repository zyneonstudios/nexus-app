package com.zyneonstudios.nexus.application.api.discover.search.zyndex;

import com.zyneonstudios.nexus.application.api.SharedAPI;
import com.zyneonstudios.nexus.application.api.discover.events.DiscoverInstallEvent;
import com.zyneonstudios.nexus.application.download.Download;
import com.zyneonstudios.nexus.application.events.DownloadFinishEvent;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.index.ReadableZyndex;
import com.zyneonstudios.nexus.modules.ReadableModule;

import java.net.URI;
import java.nio.file.Path;

public class ModuleInstallHandler extends DiscoverInstallEvent {

    @Override
    public boolean onInstall() {
        ModuleSearch source = (ModuleSearch)getSource();
        ReadableZyndex zyndex = source.getZyndex();
        String requestedVersion = super.getVersion();
        if(zyndex!=null) {
            if(zyndex.getModulesById()!=null) {
                if(zyndex.getModulesById().get(getId())!=null) {
                    ReadableModule module = zyndex.getModulesById().get(getId());
                    if(requestedVersion==null) {
                        requestedVersion = module.getVersion();
                    }
                    if(requestedVersion.equals(module.getVersion())) {
                        try {
                            Download download = new Download(getUUID(), getName()+" (Module@"+zyndex.getName()+")", new URI(getDownloadUrl()).toURL(), Path.of(ApplicationStorage.getApplicationPath()+"modules/"+getId()+".jar"));
                            download.setFinishEvent(new DownloadFinishEvent(download) {
                                @Override
                                public boolean onFinish() {
                                    //TODO module activation
                                    return false;
                                }
                            });
                            SharedAPI.openAppPage(SharedAPI.AppPage.DOWNLOADS);

                            NexusApplication.getDownloadManager().addDownload(download);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new NullPointerException("The source zyndex instance ("+zyndex.getName()+"@"+zyndex.getUrl()+") doesn't contain the requested module version ("+getId()+":"+requestedVersion+")!");
                    }
                } else {
                    throw new NullPointerException("The source zyndex instance ("+zyndex.getName()+"@"+zyndex.getUrl()+") doesn't contain the requested module ("+getId()+")!");
                }
            } else {
                throw new NullPointerException("The source zyndex instance ("+zyndex.getName()+"@"+zyndex.getUrl()+") doesn't contain modules!");
            }
        } else {
            throw new NullPointerException("The source zyndex instance can't be null!");
        }
        return false;
    }
}
