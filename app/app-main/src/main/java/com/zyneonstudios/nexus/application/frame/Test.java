package com.zyneonstudios.nexus.application.frame;

import javax.swing.*;
import java.awt.*;

public class Test {

    private JWindow window = new JWindow();

    public Test() throws InterruptedException {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setSize(200,450);
        window.setSize(1000,1000);
        window.add(panel);
        window.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        new Test();
    }
}
