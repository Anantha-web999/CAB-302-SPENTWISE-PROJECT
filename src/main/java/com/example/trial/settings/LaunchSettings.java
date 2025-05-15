package com.example.trial.settings;

public class LaunchSettings {
    public static void main(String[] args) {
        // Set JavaFX modules for proper launch
        System.setProperty("javafx.verbose", "true");

        // Launch the actual JavaFX application
        SettingsPageLauncher.main(args);
    }
}