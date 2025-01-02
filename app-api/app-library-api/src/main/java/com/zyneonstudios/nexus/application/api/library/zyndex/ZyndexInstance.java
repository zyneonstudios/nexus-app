package com.zyneonstudios.nexus.application.api.library.zyndex;

import com.zyneonstudios.nexus.application.api.library.LibraryInstance;
import com.zyneonstudios.nexus.instance.Zynstance;
import com.zyneonstudios.nexus.utilities.storage.JsonStorage;

import java.io.File;

public abstract class ZyndexInstance extends Zynstance implements LibraryInstance {

    public ZyndexInstance(File file) {
        super(file);
    }

    public ZyndexInstance(JsonStorage config) {
        super(config);
    }
}