package com.zyneonstudios.nexus.application.frame.web;

import com.zyneonstudios.nexus.application.frame.FrameConnector;
import com.zyneonstudios.nexus.application.main.ApplicationStorage;
import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.events.AsyncWebFrameConnectorEvent;
import com.zyneonstudios.nexus.desktop.events.WebFrameConnectorEvent;
import com.zyneonstudios.nexus.desktop.frame.web.NexusWebFrame;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

@SuppressWarnings("unused")
public class ApplicationFrame extends NexusWebFrame implements ComponentListener {

    private final FrameConnector connector;
    private final Dimension minSize = new Dimension(640,360);

    public ApplicationFrame(NexusApplication application, String url, CefClient client) {
        super(client, url, true);
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/icon.png"))).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        } catch (Exception ignore) {}
        addComponentListener(this);
        this.connector = new FrameConnector(this,application);
        setAsyncWebFrameConnectorEvent(new AsyncWebFrameConnectorEvent(this,"initialize") {
            @Override
            public void resolveMessage(String message) {
                connector.resolveRequest(message);
            }
        });
        setWebFrameConnectorEvent(new WebFrameConnectorEvent(this,"initialize") {
            @Override
            public boolean resolveMessage(String message) {
                connector.resolveRequest(message);
                return true;
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                NexusApplication.stop(true);
            }
        });
        client.addLoadHandler(new CefLoadHandler() {
            @Override
            public void onLoadingStateChange(CefBrowser cefBrowser, boolean b, boolean b1, boolean b2) {

            }

            @Override
            public void onLoadStart(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest.TransitionType transitionType) {
                double zoomLevel = ApplicationStorage.getZoomLevel();
                if (getWidth() < 700 || getHeight() < 480) {
                    zoomLevel -= 2;
                } else if (getWidth() < 1080 || getHeight() < 720) {
                    zoomLevel -= 1;
                }
                getBrowser().setZoomLevel(zoomLevel);
            }

            @Override
            public void onLoadEnd(CefBrowser cefBrowser, CefFrame cefFrame, int i) {

            }

            @Override
            public void onLoadError(CefBrowser cefBrowser, CefFrame cefFrame, ErrorCode errorCode, String s, String s1) {

            }
        });
        setMinimumSize(minSize);
    }

    public Dimension getMinSize() {
        return minSize;
    }

    public void setTitlebar(String title, Color background, Color foreground) {
        setTitle("NEXUS App ("+title+")");
        setTitleBackground(background);
        setTitleForeground(foreground);
    }

    @Override
    public void setTitleColors(Color background, Color foreground) {
        setBackground(background);
        super.setTitleColors(background, foreground);
    }

    public void setTitleBackground(Color color) {
        setBackground(color);
        getRootPane().putClientProperty("JRootPane.titleBarBackground", color);
    }

    public void setTitleForeground(Color color) {
        getRootPane().putClientProperty("JRootPane.titleBarForeground", color);
    }

    public void openCustomPage(String title, String pageId, String url) {
        getBrowser().loadURL(ApplicationStorage.urlBase+ ApplicationStorage.language+"/custom.html?title="+title+"&id="+pageId+"&url="+url);
    }

    public void executeJavaScript(String script) {
        super.executeJavaScript(script);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        double zoomLevel = ApplicationStorage.getZoomLevel();
        if (getWidth() < 700 || getHeight() < 425) {
            zoomLevel -= 2;
        } else if (getWidth() < 1080 || getHeight() < 525) {
            zoomLevel -= 1;
        }
        getBrowser().setZoomLevel(zoomLevel);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
