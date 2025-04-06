package com.zyneonstudios.nexus.application.modules;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.application.main.NexusApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class ModuleLoader {


    private HashMap<String, NexusModule> modules = new HashMap<>();
    private final NexusApplication application;

    public ModuleLoader(NexusApplication application) {
        this.application = application;
        if (application.getSettings().has("settings.modules.uninstall")) {
            ArrayList<String> uninstallModules = (ArrayList<String>) application.getSettings().get("settings.modules.uninstall");
            for (String module : uninstallModules) {
                new File(module).delete();
            }
            application.getSettings().delete("settings.modules.uninstall");
        }
    }

    public Collection<NexusModule> getNexusModules() {
        return modules.values();
    }

    public HashMap<String, NexusModule> getModules() {
        return modules;
    }

    public Set<String> getModuleIds() {
        return modules.keySet();
    }

    public HashMap<String, String> moduleJars = new HashMap<>();

    @SuppressWarnings("all")
    public NexusModule readModule(File moduleJar) {
        try {
            String mainPath;
            String id;
            String name;
            String version;
            String authors;
            try (JarFile jarFile = new JarFile(moduleJar.getAbsolutePath())) {
                InputStream is = jarFile.getInputStream(jarFile.getJarEntry("nexus.json"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                JsonArray array = new Gson().fromJson(reader, JsonObject.class).getAsJsonArray("modules");
                mainPath = array.get(0).getAsJsonObject().get("main").getAsString();
                id = array.get(0).getAsJsonObject().get("id").getAsString();
                if (moduleJars.keySet().contains(id)) {
                    return null;
                }
                name = array.get(0).getAsJsonObject().get("name").getAsString();
                version = array.get(0).getAsJsonObject().get("version").getAsString();
                authors = array.get(0).getAsJsonObject().get("authors").getAsJsonArray().toString();
            } catch (Exception e) {
                NexusApplication.getLogger().err("Couldn't read module " + moduleJar.getPath() + ": " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            URLClassLoader classLoader = new URLClassLoader(new URL[]{moduleJar.toURI().toURL()});
            Class<?> module = classLoader.loadClass(mainPath);
            Constructor<?> constructor = module.getConstructor();
            NexusModule module_ = (NexusModule) constructor.newInstance();
            moduleJars.put(id, moduleJar.getAbsolutePath().replace("\\", "/"));
            return module_;
        } catch (Exception e) {
            NexusApplication.getLogger().err("Couldn't read module " + moduleJar.getPath() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void loadModule(NexusModule module) {
        if (!modules.containsKey(module.getModuleId())) {
            NexusApplication.getLogger().log("[MODULES] Loading module " + module.getModuleId() + " v" + module.getModuleVersion() + " by " + Arrays.toString(module.getModuleAuthors()) + "...");
            modules.put(module.getModuleId(), module);
            module.onLoad();
        }
    }

    public void activateModules() {
        for (NexusModule module : modules.values()) {
            module.onEnable();
        }
    }

    public void deactivateModules() {
        for (NexusModule module : modules.values()) {
            module.onDisable();
        }
    }

    public void unloadModule(NexusModule module) {
        modules.remove(module.getModuleId());
    }

    @SuppressWarnings("unused")
    public void unloadModules() {
        for (NexusModule module : modules.values()) {
            unloadModule(module);
        }
        modules = new HashMap<>();
        System.gc();
    }
}