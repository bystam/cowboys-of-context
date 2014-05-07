package gui;
/*
 Downloaded from http://docs.oracle.com/javafx/2/swing/SimpleSwingBrowser.java.htm
*/

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

import static javafx.concurrent.Worker.State.FAILED;

public class SimpleSwingBrowser extends JFrame {

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;

    private final JPanel panel = new JPanel(new BorderLayout());
    private final JLabel lblStatus = new JLabel();

    private final JProgressBar progressBar = new JProgressBar();

    public SimpleSwingBrowser() {
        super();
        initComponents();
    }


    private void initComponents() {
        createScene();

        progressBar.setPreferredSize(new Dimension(150, 18));
        progressBar.setStringPainted(true);

        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
        statusBar.add(progressBar, BorderLayout.EAST);

        panel.add(jfxPanel, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);

        getContentPane().add(panel);

        setPreferredSize(new Dimension(1024, 600));
        pack();

    }

    private void createScene() {

        Platform.runLater(() -> {

            WebView view = new WebView();
            engine = view.getEngine();

            engine.titleProperty().addListener((observable, oldValue, newValue) -> {
                SwingUtilities.invokeLater(() -> {
                    SimpleSwingBrowser.this.setTitle(newValue);
                });
            });

            engine.setOnStatusChanged(event -> {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(event.getData());
                });
            });

            engine.getLoadWorker().workDoneProperty().addListener((observableValue, oldValue, newValue) -> {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(newValue.intValue());
                });
            });

            engine.getLoadWorker()
                    .exceptionProperty()
                    .addListener((o, old, value) -> {
                        if (engine.getLoadWorker().getState() == FAILED) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(panel,
                                        (value != null) ?
                                                engine.getLocation() + "\n" + value.getMessage() :
                                                engine.getLocation() + "\nUnexpected error.",
                                        "Loading error...",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            });
                        }
                    });

            jfxPanel.setScene(new Scene(view));
        });
    }

    public void loadURL(final String url) {
        Platform.runLater(() -> {
            String tmp = toURL(url);

            if (tmp == null) {
                tmp = toURL("http://" + url);
            }

            engine.load(tmp);
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleSwingBrowser browser = new SimpleSwingBrowser();
            browser.setVisible(true);
            browser.loadURL("http://facebook.com");
        });
    }
}