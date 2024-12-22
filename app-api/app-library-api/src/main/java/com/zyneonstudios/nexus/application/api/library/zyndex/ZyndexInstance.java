package com.zyneonstudios.nexus.application.api.library.zyndex;

import com.zyneonstudios.nexus.application.api.library.LibraryInstance;
import com.zyneonstudios.nexus.instance.Zynstance;
import com.zyneonstudios.nexus.utilities.storage.JsonStorage;

import java.io.File;

public class ZyndexInstance extends Zynstance implements LibraryInstance {

    private boolean launchEnabled = false;

    public ZyndexInstance(File file) {
        super(file);
    }

    public ZyndexInstance(JsonStorage config) {
        super(config);
    }

    @Override
    public void enableLaunch(boolean enable) {
        this.launchEnabled = enable;
    }

    @Override
    public boolean isLaunchEnabled() {
        return launchEnabled;
    }

    @Override
    public void launch() {

    }
}
