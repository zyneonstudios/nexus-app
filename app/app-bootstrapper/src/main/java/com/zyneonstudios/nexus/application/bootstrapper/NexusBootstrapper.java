package com.zyneonstudios.nexus.application.bootstrapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zyneonstudios.nexus.Main;
import com.zyneonstudios.nexus.application.bootstrapper.forms.ProgressFrame;
import com.zyneonstudios.nexus.utilities.NexusUtilities;
import com.zyneonstudios.nexus.utilities.json.GsonUtility;
import com.zyneonstudios.nexus.utilities.storage.JsonStorage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class NexusBootstrapper {

    private final String path;
    private JsonStorage config;
    private final ProgressFrame frame = new ProgressFrame();
    boolean updateDownloaded = false;
    private String version = "";

    public NexusBootstrapper(String path) {
        if(path==null) {
            path = "";
        }
        this.path = path;
        this.config = new JsonStorage(path+"config/bootstrapper.json");
        initConfig();
        version = config.getString("bootstrapper.data.version");

        File app = new File(path+"app.jar");
        if(config.getBool("bootstrapper.config.checkForUpdates")||!app.exists()) {
            checkForUpdates(!app.exists());
        }

        frame.setLabel("Launching application version "+version+"...");
        frame.setIndeterminate(true);
        System.gc();

        updateDownloaded = true;
        if(!launch()) {
            updateDownloaded = false;
            checkForUpdates(true);
            updateDownloaded = true;
            if(!launch()) {
                System.err.println("Failed to launch application version "+version+"!");
            }
        }
    }

    private void initConfig() {
        try {
            config = new JsonStorage(new File(path+"config/bootstrapper.json"));
            config.set("bootstrapper.data.validated",true);
            config.ensure("bootstrapper.data.channel","stable");
            config.ensure("bootstrapper.data.version","0");
            config.ensure("bootstrapper.config.checkForUpdates",true);
        } catch (Exception e) {
            config = null;
            System.gc();
            if(!new File("config/bootstrapper.json").delete()) {
                throw new RuntimeException(e);
            } else {
                initConfig();
            }
        }
    }

    private void checkForUpdates(boolean force) {
        String updateChannel = config.getString("bootstrapper.data.channel");
        frame.setTitle("NEXUS App bootstrapper");
        frame.setLabel("Checking for updates...");
        frame.setIndeterminate(true);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                close();
            }
        });

        String updateLink = null;
        try {
            boolean channelFound = false;
            for(JsonElement e:GsonUtility.getObject("https://zyneonstudios.github.io/nexus-nex/application/index.json").getAsJsonArray("versions")) {
                JsonObject channelInfo = e.getAsJsonObject().getAsJsonObject("info");
                JsonObject channelMeta = e.getAsJsonObject().getAsJsonObject("meta");
                if(channelMeta.get("id").getAsString().equalsIgnoreCase(updateChannel)) {
                    channelFound = true;
                    if(force||!version.equals(channelInfo.get("version").getAsString())) {
                        updateLink = channelMeta.get("download").getAsString();
                        version = channelInfo.get("version").getAsString();
                        break;
                    }
                }
            }
            if(!channelFound) {
                config.set("bootstrapper.data.channel", "stable");
                checkForUpdates(force);
            }
        } catch (Exception e) {
            NexusUtilities.getLogger().printErr("NEXUS App","Update failed","Oops! The updater ran into an error...",e.getMessage(),e.getStackTrace(),"Try to check your internet connection.","Try to check your app bootstrapper configuration.");
        }

        if(updateLink!=null) {
            frame.setIndeterminate(false);
            frame.setLabel("Downloading new update...");
            downloadFile(updateLink);
            frame.setIndeterminate(true);
            frame.setLabel("Update downloaded!");
            config.set("bootstrapper.data.version",version);
        }
    }

    private void close() {
        if (!updateDownloaded) {
            System.err.println("Update cancelled - stopping...");
            System.exit(0);
        }
    }

    private void downloadFile(String fileUrl) {
        try {
            URL url = new URI(fileUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int fileLength = connection.getContentLength();
            File downloadFile = new File(path+"app.jar");
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(downloadFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                int progress = (int) (totalBytesRead * 100 / fileLength);
                frame.setProgress(progress);
            }

            outputStream.close();
            inputStream.close();
            updateDownloaded = true;
        } catch (Exception e) {
            checkForUpdates(true);
        }
    }

    @SuppressWarnings("all")
    private boolean launch() {
        try {
            File appFolder = Main.getDefaultFolder(false);
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED", "--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED", "--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED", path+"app.jar", "application", "--add-opens java.desktop/sun.awt=ALL-UNNAMED --add-opens java.desktop/sun.lwawt=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED","--path:"+appFolder.getAbsolutePath().replace("\\","/")+"/");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            frame.dispose();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.exit(process.waitFor());
            return true;
        } catch (Exception e) {
            NexusUtilities.getLogger().printErr("NEXUS App","Failed to launch","Oops! The bootstrapper ran into an error...",e.getMessage(),e.getStackTrace(),"Try to manually update the app.","Try to reinstall the app.");
            return false;
        }
    }
}