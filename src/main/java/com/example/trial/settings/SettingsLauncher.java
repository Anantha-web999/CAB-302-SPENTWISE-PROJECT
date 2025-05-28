package com.example.trial.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(SettingsLauncher.class.getResource("/com/example/trial/settings/SettingsPanel.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}