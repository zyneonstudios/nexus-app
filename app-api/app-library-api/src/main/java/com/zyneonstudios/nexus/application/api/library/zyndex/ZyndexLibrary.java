package com.zyneonstudios.nexus.application.api.library.zyndex;

import com.zyneonstudios.nexus.application.api.library.Library;
import com.zyneonstudios.nexus.application.api.library.LibraryInstance;
import com.zyneonstudios.nexus.index.Zyndex;
import com.zyneonstudios.nexus.instance.ReadableZynstance;
import com.zyneonstudios.nexus.modules.ReadableModule;
import com.zyneonstudios.nexus.utilities.storage.JsonStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ZyndexLibrary extends Zyndex implements Library {

    private final HashMap<String, ZyndexInstance> instances = new HashMap<>();

    public ZyndexLibrary(File json) {
        super(json);
    }

    public ZyndexLibrary(JsonStorage config) {
        super(config);
    }

    @Override
    public void load() {
        Library.super.load();
    }

    @Override
    public void postLoad() {
        Library.super.postLoad();
    }

    @Override
    public void reload() {
        Library.super.reload();
    }

    @Override
    public LibraryInstance getLibraryInstance(String id) {
        return instances.get(id);
    }

    @Override
    public Collection<LibraryInstance> getLibraryInstances() {
        return new ArrayList<>(instances.values());
    }

    @Override
    public void removeLibraryInstance(String instanceID) {
        instances.remove(instanceID);
    }

    @Override
    public void removeLibraryInstance(LibraryInstance instance) {
        instances.remove(instance.getId());
    }

    @Override
    public void addLibraryInstance(LibraryInstance instance) {
        try {
            instances.put(instance.getId(), (ZyndexInstance)instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ReadableZynstance> getInstances() {
        return new ArrayList<>(instances.values());
    }

    @Override
    public HashMap<String, ReadableZynstance> getInstancesById() {
        HashMap<String, ReadableZynstance> instanceList = new HashMap<>();
        for(ZyndexInstance instance : instances.values()) {
            instanceList.put(instance.getId(), instance);
        }
        return instanceList;
    }

    @Override
    public void setInstances(ArrayList<ReadableZynstance> instances) {
        this.instances.clear();
        for(ReadableZynstance instance : instances) {
            try {
                addLibraryInstance((ZyndexInstance)instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addInstance(ReadableZynstance instance) {
        try {
            addLibraryInstance((ZyndexInstance)instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeInstance(ReadableZynstance instance) {
        removeLibraryInstance(instance.getId());
    }

    @Override
    public void addInstance(ReadableModule module) {
        super.addInstance(module);
    }
}