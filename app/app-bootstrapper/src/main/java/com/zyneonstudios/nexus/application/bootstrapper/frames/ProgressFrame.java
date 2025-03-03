package com.zyneonstudios.nexus.application.bootstrapper.frames;

import javax.swing.*;
import java.awt.*;

public class ProgressFrame extends JFrame {

    private final JPanel contentPane = new JPanel(new BorderLayout());
    private final JLabel progressLabel = new JLabel("Progress: ");
    private final JProgressBar progressBar = new JProgressBar();

    public ProgressFrame() {
        initContentPane();
        setTitle("Calculating...");
        setSize(380, 126);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitleBackground(Color.BLACK);
        setTitleForeground(Color.WHITE);
    }

    private void initContentPane() {
        contentPane.setBackground(Color.BLACK);
        contentPane.setForeground(Color.WHITE);

        initLabel();
        initProgressbar();
        initCancelButton();

        add(contentPane, BorderLayout.CENTER);
    }

    private void initLabel() {
        progressLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 0));
        contentPane.add(progressLabel, BorderLayout.NORTH);
    }

    private void initProgressbar() {
        progressBar.setForeground(Color.decode("#a458ff"));
        progressBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        progressBar.setSize(progressBar.getWidth(),1);
        contentPane.add(progressBar, BorderLayout.CENTER);
    }

    private void initCancelButton() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton, BorderLayout.EAST);
        cancelButton.addActionListener(e -> dispose());

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
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