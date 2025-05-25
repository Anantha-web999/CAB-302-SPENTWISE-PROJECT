package com.example.trial.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsPanelController implements Initializable {

    @FXML private Label versionLabel;
    @FXML private Label buildDateLabel;
    @FXML private Label javaVersionLabel;
    @FXML private ImageView logoImage;
    @FXML private ImageView centerLogoImage;

    @FXML private Button accountButton;
    @FXML private Button appPreferencesButton;
    @FXML private Button helpSupportButton;
    @FXML private Button aboutUsButton;
    @FXML private Button homeButton;

    private final String activeStyle = "-fx-background-color: #17202a; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-weight: bold; -fx-font-size: 14px;";
    private final String inactiveStyle = "-fx-background-color: #1a5276; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-size: 14px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Add rounded corners to the logos
        if (logoImage != null) {
            try {
                Rectangle clip = new Rectangle(
                        logoImage.getFitWidth(),
                        logoImage.getFitHeight()
                );
                clip.setArcWidth(30.0);
                clip.setArcHeight(30.0);
                logoImage.setClip(clip);

                logoImage.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                    clip.setWidth(newValue.getWidth());
                    clip.setHeight(newValue.getHeight());
                });
            } catch (Exception e) {
                System.err.println("Error applying effects to sidebar logo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (centerLogoImage != null) {
            try {
                Rectangle centerClip = new Rectangle(
                        centerLogoImage.getFitWidth(),
                        centerLogoImage.getFitHeight()
                );
                centerClip.setArcWidth(20.0);
                centerClip.setArcHeight(20.0);
                centerLogoImage.setClip(centerClip);

                centerLogoImage.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                    centerClip.setWidth(newValue.getWidth());
                    centerClip.setHeight(newValue.getHeight());
                });
            } catch (Exception e) {
                System.err.println("Error applying effects to center logo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        //Set about us button as active
        setActiveButton(aboutUsButton);

        //Set system information
        javaVersionLabel.setText(System.getProperty("java.version"));
    }

    private void setActiveButton(Button activeButton) {
        //Reset all buttons to inactive style
        accountButton.setStyle(inactiveStyle);
        appPreferencesButton.setStyle(inactiveStyle);
        helpSupportButton.setStyle(inactiveStyle);
        aboutUsButton.setStyle(inactiveStyle);

        //Set active button
        activeButton.setStyle(activeStyle);
    }

    @FXML
    private void handleAccountButton() {
        setActiveButton(accountButton);
        try {
            Parent accountRoot = FXMLLoader.load(getClass().getResource("/com/example/settingspanel/SettingsPanel.fxml"));
            Stage stage = (Stage) accountButton.getScene().getWindow();
            stage.setScene(new Scene(accountRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading account panel: " + e.getMessage());
        }
    }

    @FXML
    private void handleAppPreferencesButton() {
        setActiveButton(appPreferencesButton);
        try {
            Parent appPreferencesRoot = FXMLLoader.load(getClass().getResource("/com/example/settingspanel/AppPreferencesPanel.fxml"));
            Stage stage = (Stage) appPreferencesButton.getScene().getWindow();
            stage.setScene(new Scene(appPreferencesRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading app preferences panel: " + e.getMessage());
        }
    }

    @FXML
    private void handleHelpSupportButton() {
        setActiveButton(helpSupportButton);
        try {
            Parent helpSupportRoot = FXMLLoader.load(getClass().getResource("/com/example/settingspanel/HelpSupportPanel.fxml"));
            Stage stage = (Stage) helpSupportButton.getScene().getWindow();
            stage.setScene(new Scene(helpSupportRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading help support panel: " + e.getMessage());
        }
    }

    @FXML
    private void handleAboutUsButton() {
        setActiveButton(aboutUsButton);
        //Already on this page
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }
}