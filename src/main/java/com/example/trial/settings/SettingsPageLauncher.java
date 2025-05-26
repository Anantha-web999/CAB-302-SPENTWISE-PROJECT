package com.example.trial.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Standalone launcher for the Settings Page
 */
public class SettingsPageLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            //Print working directory for debugging
            System.out.println("Working directory: " + System.getProperty("user.dir"));

            //Try to load the FXML
            System.out.println("Trying to load FXML...");
            String fxmlPath = "/com/example/settingspanel/SettingsPanel.fxml";
            System.out.println("FXML path: " + fxmlPath);

            //Load the FXML
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            //Set up the scene with 1000x600 size
            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setTitle("Settings");
            primaryStage.setScene(scene);

            //Set minimum and preferred size
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);
            primaryStage.setWidth(1000);
            primaryStage.setHeight(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error launching settings page: " + e.getMessage());

            //Try to help diagnose the problem
            System.out.println("Checking available resources:");
            String[] paths = {
                    "/com/example/settingspanel/SettingsPanel.fxml",
                    "/SettingsPanel.fxml",
                    "SettingsPanel.fxml"
            };

            for (String path : paths) {
                System.out.println("  - " + path + ": " +
                        (getClass().getResource(path) != null ? "FOUND" : "NOT FOUND"));
            }
        }
    }

    /**
     * Main method to launch the settings page directly
     */
    public static void main(String[] args) {
        launch(args);
    }
}