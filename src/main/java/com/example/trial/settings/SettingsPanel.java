package com.example.trial.settings;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;



/**
 * Consolidated controller for the Settings Panel.
 * Manages all functionality for account, preferences, notifications, help, and about sections.
 */
public class SettingsPanel implements Initializable {

    //Main UI components
    @FXML private BorderPane mainContainer;
    @FXML private StackPane contentPane;

    //Navigation buttons
    @FXML private Button accountBtn;
    @FXML private Button preferencesBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button helpBtn;
    @FXML private Button aboutBtn;

    //Content panes
    @FXML private Pane accountPanel;
    @FXML private Pane preferencesPanel;
    @FXML private Pane notificationsPanel;
    @FXML private Pane helpPanel;
    @FXML private Pane aboutPanel;

    //Logo label
    @FXML private Label logoLabel;

    //Account panel fields
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private Label createdDateLabel;
    @FXML private ComboBox<String> dayCombo;
    @FXML private ComboBox<String> monthCombo;
    @FXML private ComboBox<String> yearCombo;
    @FXML private ComboBox<String> currencyCombo;
    @FXML private ToggleButton twoFactorToggle;

    //Preferences panel fields
    @FXML private ToggleButton darkModeToggle;
    @FXML private ComboBox<String> fontSizeCombo;
    @FXML private ComboBox<String> languageCombo;
    @FXML private ComboBox<String> dateFormatCombo;
    @FXML private ComboBox<String> timeFormatCombo;
    @FXML private CheckBox startupCheckbox;
    @FXML private CheckBox minimizedCheckbox;
    @FXML private CheckBox backupCheckbox;
    @FXML private ComboBox<String> defaultViewCombo;

    //Notifications panel fields
    @FXML private ToggleButton billsToggle;
    @FXML private ToggleButton subscriptionsToggle;
    @FXML private ToggleButton weeklyToggle;
    @FXML private ToggleButton overspendToggle;
    @FXML private ToggleButton motivationToggle;
    @FXML private RadioButton dailyRadio;
    @FXML private RadioButton weeklyRadio;
    @FXML private RadioButton monthlyRadio;
    @FXML private ComboBox<String> hourCombo;
    @FXML private ComboBox<String> minuteCombo;
    @FXML private ComboBox<String> ampmCombo;
    @FXML private CheckBox emailCheckbox;
    @FXML private CheckBox desktopCheckbox;
    @FXML private CheckBox pushCheckbox;
    @FXML private CheckBox quietHoursCheckbox;
    @FXML private ComboBox<String> fromCombo;
    @FXML private ComboBox<String> toCombo;

    //Help panel fields
    @FXML private Accordion faqAccordion;
    @FXML private ComboBox<String> issueTypeCombo;
    @FXML private TextField subjectField;
    @FXML private TextArea messageArea;

    //About panel fields
    @FXML private Label versionLabel;
    @FXML private Label aboutText;
    @FXML private Accordion sectionsAccordion;

    //Constants for styling
    private static final String BUTTON_DEFAULT_STYLE = "-fx-background-color: #1a5276; -fx-border-width: 0;";
    private static final String BUTTON_SELECTED_STYLE = "-fx-background-color: #2980b9; -fx-border-width: 0;";

    //Constants for options
    private static final String[] FONT_SIZES = new String[]{"Small", "Medium", "Large"};
    private static final String[] DATE_FORMATS = new String[]{"MM/DD/YYYY", "DD/MM/YYYY", "YYYY-MM-DD"};
    private static final String[] TIME_FORMATS = new String[]{"12-hour (AM/PM)", "24-hour"};
    private static final String[] LANGUAGES = new String[]{"English", "Spanish", "French", "German", "Chinese", "Japanese"};
    private static final String[] VIEW_TYPES = new String[]{"Monthly", "Weekly", "Yearly"};
    private static final String[] ISSUE_TYPES = new String[]{
            "Technical Problem",
            "Account Issue",
            "Billing Question",
            "Feature Request",
            "Other"
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up initial state
        highlightButton(accountBtn);
        showPanel(accountPanel);

        //Try to load logo image
        try {
            //In a real application, you would load the logo image here
            Image logoImage = new Image(new FileInputStream(new File("src/main/resources/images/spentwise_logo.png")));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitHeight(110);
            logoView.setFitWidth(110);
            logoView.setPreserveRatio(true);
            logoLabel.setGraphic(logoView);
            logoLabel.setText("");
        } catch (Exception e) {
            System.out.println("Could not load logo image: " + e.getMessage());
            logoLabel.setText("SpentWise");
        }

        //Initialize all panels
        initializeAccountPanel();
        initializePreferencesPanel();
        initializeNotificationsPanel();
        initializeHelpPanel();
        initializeAboutPanel();
    }

    //-------------------- Navigation Methods --------------------//

    /**
     * Shows the account panel.
     */
    @FXML
    private void showAccountPanel() {
        highlightButton(accountBtn);
        showPanel(accountPanel);
    }

    /**
     * Shows the preferences panel.
     */
    @FXML
    private void showPreferencesPanel() {
        highlightButton(preferencesBtn);
        showPanel(preferencesPanel);
    }

    /**
     * Shows the notifications panel.
     */
    @FXML
    private void showNotificationsPanel() {
        highlightButton(notificationsBtn);
        showPanel(notificationsPanel);
    }

    /**
     * Shows the help panel.
     */
    @FXML
    private void showHelpPanel() {
        highlightButton(helpBtn);
        showPanel(helpPanel);
    }

    /**
     * Shows the about panel.
     */
    @FXML
    private void showAboutPanel() {
        highlightButton(aboutBtn);
        showPanel(aboutPanel);
    }

    /**
     * Highlights the selected button and resets others.
     */
    private void highlightButton(Button selectedButton) {
        //Reset all buttons
        accountBtn.setStyle(BUTTON_DEFAULT_STYLE);
        preferencesBtn.setStyle(BUTTON_DEFAULT_STYLE);
        notificationsBtn.setStyle(BUTTON_DEFAULT_STYLE);
        helpBtn.setStyle(BUTTON_DEFAULT_STYLE);
        aboutBtn.setStyle(BUTTON_DEFAULT_STYLE);

        //Highlight the selected button
        selectedButton.setStyle(BUTTON_SELECTED_STYLE);
    }

    /**
     * Shows the selected panel and hides others.
     */
    private void showPanel(Pane panelToShow) {
        accountPanel.setVisible(false);
        preferencesPanel.setVisible(false);
        notificationsPanel.setVisible(false);
        helpPanel.setVisible(false);
        aboutPanel.setVisible(false);

        panelToShow.setVisible(true);
    }

    //-------------------- Account Panel Methods --------------------//

    /**
     * Initializes the account panel.
     */
    private void initializeAccountPanel() {
        //Initialize date of birth dropdowns
        initializeDateDropdowns();

        //Initialize currency dropdown
        initializeCurrencyDropdown();

        //Set account created date
        createdDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));

        //Load user data if available
        loadAccountData();
    }

    /**
     * Initializes the date of birth dropdown selectors.
     */
    private void initializeDateDropdowns() {
        //Days
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.format("%02d", i + 1);
        }
        dayCombo.setItems(FXCollections.observableArrayList(days));
        dayCombo.getSelectionModel().select(0);

        //Months
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthCombo.setItems(FXCollections.observableArrayList(months));
        monthCombo.getSelectionModel().select(0);

        //Years
        String[] years = new String[100];
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 100; i++) {
            years[i] = String.valueOf(currentYear - i);
        }
        yearCombo.setItems(FXCollections.observableArrayList(years));
        yearCombo.getSelectionModel().select(0);
    }

    /**
     * Initializes the currency dropdown selector.
     */
    private void initializeCurrencyDropdown() {
        String[] currencies = new String[]{"USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)", "CAD ($)", "AUD ($)", "INR (₹)"};
        currencyCombo.setItems(FXCollections.observableArrayList(currencies));
        currencyCombo.getSelectionModel().select(0);
    }

    /**
     * Loads user data from SettingsManager if available.
     */
    private void loadAccountData() {
        //Get user data from SettingsManager
        String fullName = SettingsManager.getFullName();
        String username = SettingsManager.getUsername();
        String email = SettingsManager.getEmail();
        String phone = SettingsManager.getPhone();
        String address = SettingsManager.getAddress();
        String dob = SettingsManager.getDateOfBirth();
        String currency = SettingsManager.getCurrency();
        boolean twoFactor = SettingsManager.getTwoFactorAuth();

        //Populate form fields if data exists
        if (!fullName.isEmpty()) nameField.setText(fullName);
        if (!username.isEmpty()) usernameField.setText(username);
        if (!email.isEmpty()) emailField.setText(email);
        if (!phone.isEmpty()) phoneField.setText(phone);
        if (!address.isEmpty()) addressArea.setText(address);

        //Set currency
        if (!currency.isEmpty()) {
            for (int i = 0; i < currencyCombo.getItems().size(); i++) {
                if (currencyCombo.getItems().get(i).equals(currency)) {
                    currencyCombo.getSelectionModel().select(i);
                    break;
                }
            }
        }

        //Set two-factor authentication
        twoFactorToggle.setSelected(twoFactor);

        //Parse and set date of birth if available
        if (!dob.isEmpty()) {
            //In a real app, you would parse the date string and set the dropdowns
            //For simplicity, we'll skip this here
        }
    }

    /**
     * Handles the change password button click.
     */
    @FXML
    private void onChangePassword() {
        //In a real app, this would open a change password dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText("Change Password Feature");
        alert.setContentText("This would open a dialog to change your password.");
        alert.showAndWait();
    }

    /**
     * Handles the cancel button click on account panel.
     */
    @FXML
    private void onAccountCancel() {
        //Reset form to original values
        loadAccountData();
    }

    /**
     * Handles the save button click on account panel.
     */
    @FXML
    private void onAccountSave() {
        //Validate form data
        if (!validateAccountForm()) {
            return;
        }

        //Get form data
        String fullName = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressArea.getText();
        String currency = currencyCombo.getValue();
        boolean twoFactor = twoFactorToggle.isSelected();

        //Construct date of birth string
        String dob = dayCombo.getValue() + "/" + monthCombo.getValue() + "/" + yearCombo.getValue();

        //Save to SettingsManager
        SettingsManager.saveAccountSettings(fullName, dob, username, email, phone, address, currency, twoFactor);

        //Show success message
        showSuccessAlert("Account Settings", "Your account settings have been saved successfully.");
    }

    /**
     * Validates the account form data.
     *
     * @return true if the form data is valid, false otherwise
     */
    private boolean validateAccountForm() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("- Full Name is required\n");
        }

        if (usernameField.getText().trim().isEmpty()) {
            errors.append("- Username is required\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errors.append("- Email is required\n");
        } else if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.append("- Email format is invalid\n");
        }

        if (errors.length() > 0) {
            showErrorAlert("Validation Error", "Please correct the following errors:", errors.toString());
            return false;
        }

        return true;
    }

    //-------------------- Preferences Panel Methods --------------------//

    /**
     * Initializes the preferences panel.
     */
    private void initializePreferencesPanel() {
        //Initialize combo boxes
        initializePreferencesComboBoxes();

        //Load saved preferences
        loadPreferencesData();
    }

    /**
     * Initializes all combo boxes with their respective option lists.
     */
    private void initializePreferencesComboBoxes() {
        //Font size options
        fontSizeCombo.setItems(FXCollections.observableArrayList(FONT_SIZES));
        fontSizeCombo.getSelectionModel().select("Medium");

        //Language options
        languageCombo.setItems(FXCollections.observableArrayList(LANGUAGES));
        languageCombo.getSelectionModel().select("English");

        //Date format options
        dateFormatCombo.setItems(FXCollections.observableArrayList(DATE_FORMATS));
        dateFormatCombo.getSelectionModel().select("MM/DD/YYYY");

        //Time format options
        timeFormatCombo.setItems(FXCollections.observableArrayList(TIME_FORMATS));
        timeFormatCombo.getSelectionModel().select("12-hour (AM/PM)");

        //Default view options
        defaultViewCombo.setItems(FXCollections.observableArrayList(VIEW_TYPES));
        defaultViewCombo.getSelectionModel().select("Monthly");
    }

    /**
     * Loads saved preferences from SettingsManager.
     */
    private void loadPreferencesData() {
        //Get saved preferences
        boolean darkMode = SettingsManager.getDarkMode();
        String fontSize = SettingsManager.getFontSize();
        String language = SettingsManager.getLanguage();
        String dateFormat = SettingsManager.getDateFormat();
        String timeFormat = SettingsManager.getTimeFormat();
        boolean startOnBoot = SettingsManager.getStartOnBoot();
        boolean startMinimized = SettingsManager.getStartMinimized();
        boolean autoBackup = SettingsManager.getAutoBackup();
        String defaultView = SettingsManager.getDefaultView();

        //Set UI controls based on saved preferences
        darkModeToggle.setSelected(darkMode);
        startupCheckbox.setSelected(startOnBoot);
        minimizedCheckbox.setSelected(startMinimized);
        backupCheckbox.setSelected(autoBackup);

        //Set combo box selections
        selectComboBoxItem(fontSizeCombo, fontSize);
        selectComboBoxItem(languageCombo, language);
        selectComboBoxItem(dateFormatCombo, dateFormat);
        selectComboBoxItem(timeFormatCombo, timeFormat);
        selectComboBoxItem(defaultViewCombo, defaultView);
    }

    /**
     * Selects an item in a combo box if it exists.
     */
    private void selectComboBoxItem(ComboBox<String> comboBox, String item) {
        for (int i = 0; i < comboBox.getItems().size(); i++) {
            if (comboBox.getItems().get(i).equals(item)) {
                comboBox.getSelectionModel().select(i);
                break;
            }
        }
    }

    /**
     * Handles the Reset to Defaults button click.
     */
    @FXML
    private void onPreferencesReset() {
        //Reset all settings to defaults
        darkModeToggle.setSelected(false);
        fontSizeCombo.getSelectionModel().select("Medium");
        languageCombo.getSelectionModel().select("English");
        dateFormatCombo.getSelectionModel().select("MM/DD/YYYY");
        timeFormatCombo.getSelectionModel().select("12-hour (AM/PM)");
        startupCheckbox.setSelected(false);
        minimizedCheckbox.setSelected(false);
        backupCheckbox.setSelected(true);
        defaultViewCombo.getSelectionModel().select("Monthly");

        //Show confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Preferences");
        alert.setHeaderText("Preferences Reset");
        alert.setContentText("All preferences have been reset to default values. Click Save to apply these changes.");
        alert.showAndWait();
    }

    /**
     * Handles the Save Preferences button click.
     */
    @FXML
    private void onPreferencesSave() {
        //Get current settings from UI
        boolean darkMode = darkModeToggle.isSelected();
        String fontSize = fontSizeCombo.getValue();
        String language = languageCombo.getValue();
        String dateFormat = dateFormatCombo.getValue();
        String timeFormat = timeFormatCombo.getValue();
        boolean startOnBoot = startupCheckbox.isSelected();
        boolean startMinimized = minimizedCheckbox.isSelected();
        boolean autoBackup = backupCheckbox.isSelected();
        String defaultView = defaultViewCombo.getValue();

        //Save to SettingsManager
        SettingsManager.saveAppPreferences(
                darkMode,
                fontSize,
                language,
                dateFormat,
                timeFormat,
                startOnBoot,
                startMinimized,
                autoBackup,
                defaultView,
                "Pie Chart", // Default chart type
                50 // Default animations level
        );

        //Show success message
        showSuccessAlert("App Preferences", "Your application preferences have been saved successfully.");
    }

    //-------------------- Notifications Panel Methods --------------------//

    /**
     * Initializes the notifications panel.
     */
    private void initializeNotificationsPanel() {
        //Initialize combo boxes
        initializeTimeSelectors();

        //Load saved settings
        loadNotificationsData();
    }

    /**
     * Initializes time selection combo boxes.
     */
    private void initializeTimeSelectors() {
        //Hours (1-12)
        String[] hours = new String[12];
        for (int i = 0; i < 12; i++) {
            hours[i] = String.format("%d", i + 1);
        }
        hourCombo.setItems(FXCollections.observableArrayList(hours));
        hourCombo.getSelectionModel().select(8); // 9 AM default

        //Minutes (00, 15, 30, 45)
        String[] minutes = new String[4];
        for (int i = 0; i < 4; i++) {
            minutes[i] = String.format("%02d", i * 15);
        }
        minuteCombo.setItems(FXCollections.observableArrayList(minutes));
        minuteCombo.getSelectionModel().select(0);

        //AM/PM
        ampmCombo.setItems(FXCollections.observableArrayList("AM", "PM"));
        ampmCombo.getSelectionModel().select(0);

        //24-hour format for quiet hours
        String[] fullHours = new String[24];
        for (int i = 0; i < 24; i++) {
            fullHours[i] = String.format("%02d:00", i);
        }

        fromCombo.setItems(FXCollections.observableArrayList(fullHours));
        fromCombo.getSelectionModel().select(22); // 10 PM

        toCombo.setItems(FXCollections.observableArrayList(fullHours));
        toCombo.getSelectionModel().select(7); // 7 AM
    }

    /**
     * Loads saved notification settings from SettingsManager.
     */
    private void loadNotificationsData() {
        //Get notification settings
        boolean notifyBills = SettingsManager.getNotifyBills();
        boolean notifySubscriptions = SettingsManager.getNotifySubscriptions();
        boolean notifyWeekly = SettingsManager.getNotifyWeekly();
        boolean notifyOverspend = SettingsManager.getNotifyOverspend();
        boolean notifyMotivation = SettingsManager.getNotifyMotivation();
        String frequency = SettingsManager.getNotifyFrequency();
        String notifyTime = SettingsManager.getNotifyTime();
        boolean notifyEmail = SettingsManager.getNotifyEmail();
        boolean notifyDesktop = SettingsManager.getNotifyDesktop();
        boolean notifyPush = SettingsManager.getNotifyPush();
        boolean quietHours = SettingsManager.getQuietHours();
        String quietFrom = SettingsManager.getQuietFrom();
        String quietTo = SettingsManager.getQuietTo();

        //Set toggle buttons
        billsToggle.setSelected(notifyBills);
        subscriptionsToggle.setSelected(notifySubscriptions);
        weeklyToggle.setSelected(notifyWeekly);
        overspendToggle.setSelected(notifyOverspend);
        motivationToggle.setSelected(notifyMotivation);

        //Set frequency radio buttons
        if (frequency.equals("Daily")) {
            dailyRadio.setSelected(true);
        } else if (frequency.equals("Weekly")) {
            weeklyRadio.setSelected(true);
        } else if (frequency.equals("Monthly")) {
            monthlyRadio.setSelected(true);
        }

        //Set notification methods
        emailCheckbox.setSelected(notifyEmail);
        desktopCheckbox.setSelected(notifyDesktop);
        pushCheckbox.setSelected(notifyPush);

        //Set quiet hours
        quietHoursCheckbox.setSelected(quietHours);

        //Set quiet hours time
        if (!quietFrom.isEmpty()) {
            selectComboBoxItemStartsWith(fromCombo, quietFrom);
        }

        if (!quietTo.isEmpty()) {
            selectComboBoxItemStartsWith(toCombo, quietTo);
        }

        //Set time selectors
        if (!notifyTime.isEmpty()) {
            //Parse time (e.g., "9:00 AM")
            try {
                String[] parts = notifyTime.split(":");
                String hourStr = parts[0];
                String minuteAmPm = parts[1];
                String minute = minuteAmPm.substring(0, 2);
                String ampm = minuteAmPm.substring(3);

                selectComboBoxItem(hourCombo, hourStr);
                selectComboBoxItem(minuteCombo, minute);
                selectComboBoxItem(ampmCombo, ampm);
            } catch (Exception e) {
                System.out.println("Error parsing time: " + e.getMessage());
            }
        }
    }

    /**
     * Selects an item in a combo box if it starts with the given prefix.
     */
    private void selectComboBoxItemStartsWith(ComboBox<String> comboBox, String prefix) {
        for (int i = 0; i < comboBox.getItems().size(); i++) {
            if (comboBox.getItems().get(i).startsWith(prefix)) {
                comboBox.getSelectionModel().select(i);
                break;
            }
        }
    }

    /**
     * Handles the Notifications Cancel button click.
     */
    @FXML
    private void onNotificationsCancel() {
        //Reset to saved settings
        loadNotificationsData();
    }

    /**
     * Handles the Notifications Save button click.
     */
    @FXML
    private void onNotificationsSave() {
        //Get settings from UI controls
        boolean notifyBills = billsToggle.isSelected();
        boolean notifySubscriptions = subscriptionsToggle.isSelected();
        boolean notifyWeekly = weeklyToggle.isSelected();
        boolean notifyOverspend = overspendToggle.isSelected();
        boolean notifyMotivation = motivationToggle.isSelected();

        //Get frequency
        String frequency = "Weekly"; // Default
        if (dailyRadio.isSelected()) {
            frequency = "Daily";
        } else if (weeklyRadio.isSelected()) {
            frequency = "Weekly";
        } else if (monthlyRadio.isSelected()) {
            frequency = "Monthly";
        }

        //Get notification methods
        boolean notifyEmail = emailCheckbox.isSelected();
        boolean notifyDesktop = desktopCheckbox.isSelected();
        boolean notifyPush = pushCheckbox.isSelected();

        //Get quiet hours settings
        boolean quietHours = quietHoursCheckbox.isSelected();
        String quietFrom = fromCombo.getValue().substring(0, 5); // e.g., "22:00"
        String quietTo = toCombo.getValue().substring(0, 5);     // e.g., "07:00"

        //Construct notification time string
        String notifyTime = String.format("%s:%s %s",
                hourCombo.getValue(),
                minuteCombo.getValue(),
                ampmCombo.getValue());

        //Save to SettingsManager
        SettingsManager.saveNotificationSettings(
                notifyBills,
                notifySubscriptions,
                notifyWeekly,
                notifyOverspend,
                notifyMotivation,
                frequency,
                notifyTime,
                notifyEmail,
                notifyDesktop,
                notifyPush,
                quietHours,
                quietFrom,
                quietTo
        );

        //Show success message
        showSuccessAlert("Notification Settings", "Your notification settings have been saved successfully.");
    }

    //-------------------- Help Panel Methods --------------------//

    /**
     * Initializes the help panel.
     */
    private void initializeHelpPanel() {
        //Initialize issue type combo box
        initializeIssueTypes();
    }

    /**
     * Initializes the issue type combo box.
     */
    private void initializeIssueTypes() {
        issueTypeCombo.setItems(FXCollections.observableArrayList(ISSUE_TYPES));
        issueTypeCombo.getSelectionModel().select(0);
    }

    /**
     * Handles the attachment button click.
     */
    @FXML
    private void onAttachScreenshot() {
        //In a real app, this would open a file chooser
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attach Screenshot");
        alert.setHeaderText("Attachment Feature");
        alert.setContentText("This would open a file chooser to select a screenshot.");
        alert.showAndWait();
    }

    /**
     * Handles the submit support request button click.
     */
    @FXML
    private void onSubmitRequest() {
        //Validate form
        if (!validateHelpForm()) {
            return;
        }

        //Get form data
        String issueType = issueTypeCombo.getValue();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        //In a real app, this would send the support request
        //For now, just show a confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Support Request Submitted");
        alert.setHeaderText("Thank You");
        alert.setContentText("Your support request has been submitted. We'll get back to you within 24 hours.");
        alert.showAndWait();

        //Clear the form
        subjectField.clear();
        messageArea.clear();
        issueTypeCombo.getSelectionModel().select(0);
    }

    /**
     * Validates the help form data.
     *
     * @return true if the form data is valid, false otherwise
     */
    private boolean validateHelpForm() {
        StringBuilder errors = new StringBuilder();

        if (subjectField.getText().trim().isEmpty()) {
            errors.append("- Subject is required\n");
        }

        if (messageArea.getText().trim().isEmpty()) {
            errors.append("- Message is required\n");
        }

        if (errors.length() > 0) {
            showErrorAlert("Validation Error", "Please correct the following errors:", errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Opens a tutorial video.
     *
     * @param tutorialName the name of the tutorial to open
     */
    @FXML
    private void openTutorial(String tutorialName) {
        //In a real app, this would play the tutorial video
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tutorial");
        alert.setHeaderText(tutorialName);
        alert.setContentText("This would play the tutorial video: " + tutorialName);
        alert.showAndWait();
    }

    //-------------------- About Panel Methods --------------------//

    /**
     * Initializes the about panel.
     */
    private void initializeAboutPanel() {
        //Set version
        if (versionLabel != null) {
            versionLabel.setText("Version 1.2.0");
        }

        //Set about text
        if (aboutText != null) {
            aboutText.setText("SpentWise is a personal budgeting application that we designed to help you take control " +
                    "of your finances. Whether you're saving for a big purchase, trying to pay off a debt, or " +
                    "just want to know where your money is going, SpentWise provides the tools you need to " +
                    "achieve success.");
        }
    }

    /**
     * Opens the SpentWise website.
     */
    @FXML
    private void openWebsite() {
        showInfoAlert("Website", "This would open www.spentwise.com in the default browser.");
    }

    /**
     * Sends an email to support.
     */
    @FXML
    private void sendEmail() {
        showInfoAlert("Email", "This would open your default email client with recipient: support@spentwise.com");
    }

    /**
     * Opens social media profiles.
     */
    @FXML
    private void openSocial() {
        showInfoAlert("Social Media", "This would open @SpentWise social media profile.");
    }

    //-------------------- Helper Methods --------------------//

    /**
     * Shows a success alert.
     */
    private void showSuccessAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows an error alert.
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows an information alert.
     */
    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}