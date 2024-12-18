package com.zyneonstudios.nexus.application.bootstrapper.forms;

import javax.swing.*;
import java.awt.*;

public class ProgressFrame extends JFrame {

    private JPanel contentPane;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JPanel progressPanel;
    private JPanel buttonPanel;
    private JLabel progressLabel;

    public ProgressFrame() {
        setTitle("Calculating...");
        add(contentPane);
        contentPane.setBackground(Color.BLACK);
        progressPanel.setBackground(Color.BLACK);
        buttonPanel.setBackground(Color.BLACK);
        progressBar.setForeground(Color.decode("#a458ff"));
        setSize(380, 126);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitleBackground(Color.BLACK);
        setTitleForeground(Color.WHITE);

        cancelButton.addActionListener(e -> dispose());
    }

    public void setLabel(String text) {
        progressLabel.setText(text);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    public int getProgress() {
        return progressBar.getValue();
    }

    public void setIndeterminate(boolean indeterminate) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(indeterminate);
            progressBar.setStringPainted(!indeterminate);
        });
    }

    public void setTitleBackground(Color color) {
        setBackground(color);
        getRootPane().putClientProperty("JRootPane.titleBarBackground", color);
    }

    public void setTitleForeground(Color color) {
        getRootPane().putClientProperty("JRootPane.titleBarForeground", color);
    }
}
