package com.zyneonstudios.nexus.application.frame;

import com.zyneonstudios.nexus.application.main.NexusApplication;
import com.zyneonstudios.nexus.desktop.frame.web.NWebFrame;
import com.zyneonstudios.nexus.desktop.frame.web.WebFrame;
import org.cef.CefClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

@SuppressWarnings("unused")
public class ApplicationFrame extends NWebFrame implements ComponentListener, WebFrame {

    private final Dimension minSize = new Dimension(640,360);

    public ApplicationFrame(NexusApplication application, String url, CefClient client, boolean decorated) {
        super(client, url, decorated);
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("/icon.png"))).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        } catch (Exception ignore) {}
        addComponentListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
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
    }

    public void setTitleBackground(Color color) {
        setBackground(color);
        getRootPane().putClientProperty("JRootPane.titleBarBackground", color);
    }

    public void setTitleForeground(Color color) {
        getRootPane().putClientProperty("JRootPane.titleBarForeground", color);
    }

    public void executeJavaScript(String script) {
        super.executeJavaScript(script);
    }


    @Override
    public JFrame getAsJFrame() {
        return this;
    }

    @Override
    public void componentResized(ComponentEvent e) {

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
