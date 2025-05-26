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

public class HelpSupportPanelController implements Initializable {

    @FXML private TextField emailField;
    @FXML private TextArea messageArea;
    @FXML private ImageView logoImage;

    @FXML private Button accountButton;
    @FXML private Button appPreferencesButton;
    @FXML private Button helpSupportButton;
    @FXML private Button aboutUsButton;
    @FXML private Button homeButton;

    private final String activeStyle = "-fx-background-color: #17202a; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-weight: bold; -fx-font-size: 14px;";
    private final String inactiveStyle = "-fx-background-color: #1a5276; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-size: 14px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Add rounded corners to the logo
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
                System.err.println("Error applying effects to logo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        //Set help support button as active
        setActiveButton(helpSupportButton);
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
        //Already on this page
    }

    @FXML
    private void handleAboutUsButton() {
        setActiveButton(aboutUsButton);
        try {
            Parent aboutUsRoot = FXMLLoader.load(getClass().getResource("/com/example/settingspanel/AboutUsPanel.fxml"));
            Stage stage = (Stage) aboutUsButton.getScene().getWindow();
            stage.setScene(new Scene(aboutUsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading about us panel: " + e.getMessage());
        }
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }
}