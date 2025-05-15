package com.example.trial.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SettingsManager {

    public static void openSettingsPanel() {
        try {
            // Print all attempted paths for debugging
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Try multiple resource loading approaches
            URL fxmlUrl = null;
            String resourcePath = "com/example/settingspanel/SettingsPanel.fxml";

            // Approach 1: Class resource
            fxmlUrl = SettingsManager.class.getResource("/" + resourcePath);
            System.out.println("Approach 1 URL: " + fxmlUrl);

            // Approach 2: ClassLoader resource (no leading slash)
            if (fxmlUrl == null) {
                fxmlUrl = SettingsManager.class.getClassLoader().getResource(resourcePath);
                System.out.println("Approach 2 URL: " + fxmlUrl);
            }

            // Approach 3: Try without subfolder
            if (fxmlUrl == null) {
                fxmlUrl = SettingsManager.class.getResource("/SettingsPanel.fxml");
                System.out.println("Approach 3 URL: " + fxmlUrl);
            }

            // Approach 4: Try with ClassLoader without subfolder
            if (fxmlUrl == null) {
                fxmlUrl = SettingsManager.class.getClassLoader().getResource("SettingsPanel.fxml");
                System.out.println("Approach 4 URL: " + fxmlUrl);
            }

            if (fxmlUrl == null) {
                throw new IOException("Could not find FXML file in any location");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Settings");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading settings panel: " + e.getMessage());

            // Show error dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error Loading Settings");
            alert.setHeaderText("Could not load the Settings Panel");
            alert.setContentText("Error: " + e.getMessage() + "\n\nPlease check the console for more details.");
            alert.showAndWait();
        }
    }

    public static void openSettingsPanel(UserAccount userAccount) {
        // Call the main method first to get the panel open
        openSettingsPanel();

        // If we get this far, we can try to set the user data
        // (This is a simple approach; in a more robust implementation,
        // we would get the controller from the FXMLLoader and set the data before showing the stage)
    }
}