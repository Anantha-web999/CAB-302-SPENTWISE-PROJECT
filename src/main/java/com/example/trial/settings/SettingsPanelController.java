package com.example.trial.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import javafx.scene.effect.DropShadow;
import java.time.LocalDate;
import java.util.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;

public class SettingsPanelController implements Initializable {

    @FXML private TextField fullNameField;
    @FXML private ComboBox<String> dayComboBox;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private ComboBox<String> yearComboBox;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private ComboBox<String> currencyComboBox;
    @FXML private ImageView logoImage;

    @FXML private Button accountButton;
    @FXML private Button appPreferencesButton;
    @FXML private Button helpSupportButton;
    @FXML private Button aboutUsButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    private final String activeStyle = "-fx-background-color: #17202a; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-weight: bold; -fx-font-size: 14px;";
    private final String inactiveStyle = "-fx-background-color: #1a5276; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-size: 14px;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set account button as active by default
        setActiveButton(accountButton);

        //Initialize date dropdowns
        initializeDateDropdowns();

        //Initialize currency dropdown
        initializeCurrencyDropdown();

        //Add rounded corners to the logo - make sure logoImage is properly declared with @FXML
        if (logoImage != null) {
            try {
                //Apply clip for rounded corners
                Rectangle clip = new Rectangle(
                        logoImage.getFitWidth(),
                        logoImage.getFitHeight()
                );
                clip.setArcWidth(30.0);
                clip.setArcHeight(30.0);
                logoImage.setClip(clip);

                //Make the clip resize with the image
                logoImage.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                    clip.setWidth(newValue.getWidth());
                    clip.setHeight(newValue.getHeight());
                });
            } catch (Exception e) {
                //Just log the error but continue execution
                System.err.println("Error applying effects to logo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        //Load user data
        loadUserData();
    }


    private void initializeDateDropdowns() {
        //Days 1-31
        for (int i = 1; i <= 31; i++) {
            dayComboBox.getItems().add(String.format("%02d", i));
        }

        //Months
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthComboBox.getItems().addAll(months);

        //Years (current year - 100 to current year)
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearComboBox.getItems().add(String.valueOf(i));
        }

        //Set default date (for example, January 1st, 2025)
        dayComboBox.setValue("01");
        monthComboBox.setValue("Jan");
        yearComboBox.setValue("2025");
    }

    private void initializeCurrencyDropdown() {
        //Common currencies
        Map<String, String> currencies = new LinkedHashMap<>();
        currencies.put("USD ($)", "US Dollar");
        currencies.put("EUR (€)", "Euro");
        currencies.put("GBP (£)", "British Pound");
        currencies.put("JPY (¥)", "Japanese Yen");
        currencies.put("AUD ($)", "Australian Dollar");
        currencies.put("CAD ($)", "Canadian Dollar");
        currencies.put("CHF (Fr)", "Swiss Franc");
        currencies.put("CNY (¥)", "Chinese Yuan");

        currencyComboBox.getItems().addAll(currencies.keySet());
        currencyComboBox.setValue("USD ($)");
    }

    private void loadUserData() {
        //In a real application, this would load data from a database or user service
        //For now, we'll just set some example data
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
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
        //Here you would load the account panel content
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

    @FXML
    private void handleSaveButton() {
        //Save user settings
        //In a real application, this would save to a database or user service

        //Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("Your settings have been successfully saved.");
        alert.showAndWait();
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }

    /**
     * Sets user data in the form fields
     * @param userAccount the user account data
     */
    public void setUserData(UserAccount userAccount) {
        //Populate fields with user data
        if (userAccount.getFullName() != null) {
            fullNameField.setText(userAccount.getFullName());
        }

        if (userAccount.getUsername() != null) {
            usernameField.setText(userAccount.getUsername());
        }

        if (userAccount.getEmail() != null) {
            emailField.setText(userAccount.getEmail());
        }

        if (userAccount.getPhone() != null) {
            phoneField.setText(userAccount.getPhone());
        }

        if (userAccount.getAddress() != null) {
            addressField.setText(userAccount.getAddress());
        }

        //Set date of birth if available
        if (userAccount.getDateOfBirth() != null) {
            String[] dobParts = userAccount.getDateOfBirth().split("/");
            if (dobParts.length == 3) {
                int month = Integer.parseInt(dobParts[0]);
                dayComboBox.setValue(dobParts[1]);
                monthComboBox.setValue(getMonthAbbreviation(month));
                yearComboBox.setValue(dobParts[2]);
            }
        }

        //Set currency
        if (userAccount.getPreferredCurrency() != null) {
            currencyComboBox.setValue(userAccount.getPreferredCurrency());
        }

    }

    /**
     * Gets month abbreviation from month number (1-12)
     */
    private String getMonthAbbreviation(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return "Jan"; //Default
    }
}