package com.example.trial.settings;

import com.example.trial.DB.DatabaseHelper;
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
import java.sql.*;
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

    //Database integration field
    private int currentUserId = 1; //Using user ID 1 as default

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

        //Create extended user table on startup
        createExtendedUserTable();

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

    //Create extended user profile table for additional fields
    private void createExtendedUserTable() {
        try {
            DatabaseHelper.initializeDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS user_profiles (" +
                    "user_id INTEGER PRIMARY KEY, " +
                    "phone_number TEXT, " +
                    "date_of_birth TEXT, " +
                    "address TEXT, " +
                    "username TEXT, " +
                    "preferred_currency TEXT DEFAULT 'USD ($)', " +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)";

            try (Connection conn = DatabaseHelper.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.execute(sql);

                //Create profile entry for current user if it doesn't exist
                createUserProfileIfNotExists(currentUserId);

                System.out.println("Extended user profile table created/verified successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating extended user table: " + e.getMessage());
        }
    }

    private void createUserProfileIfNotExists(int userId) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM user_profiles WHERE user_id = ?";
        String insertSql = "INSERT INTO user_profiles (user_id, phone_number, date_of_birth, address, username, preferred_currency) VALUES (?, '', '', '', '', 'USD ($)')";

        try (Connection conn = DatabaseHelper.getConnection()) {
            //Check if profile exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    //Profile doesn't exist, create it
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.executeUpdate();
                        System.out.println("Created user profile for user ID: " + userId);
                    }
                }
            }
        }
    }

    private void loadUserData() {
        try {
            //Print all users for debugging
            printAllUsers();

            UserAccount user = getUserFromDatabase(currentUserId);

            if (user != null) {
                //Load basic data from users table
                if (user.getFullName() != null && !user.getFullName().isEmpty()) {
                    fullNameField.setText(user.getFullName());
                } else {
                    fullNameField.setText("");
                }

                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    emailField.setText(user.getEmail());
                } else {
                    emailField.setText("");
                }

                //Load extended data from user_profiles table
                loadExtendedUserData(currentUserId);

                System.out.println("Successfully loaded user data from database: " + user.getFullName());
            } else {
                //User not found in database, clear fields
                clearAllFields();
                System.out.println("User with ID " + currentUserId + " not found in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading user data from database: " + e.getMessage());

            //Show error to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to load user data");
            alert.setContentText("Could not connect to database: " + e.getMessage());
            alert.showAndWait();

            //Fallback to empty fields
            clearAllFields();
        }
    }

    private void loadExtendedUserData(int userId) throws SQLException {
        String sql = "SELECT phone_number, date_of_birth, address, username, preferred_currency FROM user_profiles WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                //Load phone number
                String phone = rs.getString("phone_number");
                if (phone != null && !phone.isEmpty()) {
                    phoneField.setText(phone);
                } else {
                    phoneField.setText("");
                }

                //Load date of birth
                String dateOfBirth = rs.getString("date_of_birth");
                if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                    setDateOfBirthFromString(dateOfBirth);
                } else {
                    //Keep default values
                    dayComboBox.setValue("01");
                    monthComboBox.setValue("Jan");
                    yearComboBox.setValue("2025");
                }

                //Load address
                String address = rs.getString("address");
                if (address != null && !address.isEmpty()) {
                    addressField.setText(address);
                } else {
                    addressField.setText("");
                }

                //Load username
                String username = rs.getString("username");
                if (username != null && !username.isEmpty()) {
                    usernameField.setText(username);
                } else {
                    usernameField.setText("");
                }

                //Load preferred currency
                String currency = rs.getString("preferred_currency");
                if (currency != null && !currency.isEmpty()) {
                    currencyComboBox.setValue(currency);
                } else {
                    currencyComboBox.setValue("USD ($)");
                }

                System.out.println("Loaded extended user data successfully");
            }
        }
    }

    private void setDateOfBirthFromString(String dateOfBirth) {
        try {
            String[] parts = dateOfBirth.split("/");
            if (parts.length == 3) {
                String day = parts[0];
                String month = parts[1];
                String year = parts[2];

                dayComboBox.setValue(day);
                monthComboBox.setValue(month);
                yearComboBox.setValue(year);
            }
        } catch (Exception e) {
            System.err.println("Error parsing date of birth: " + e.getMessage());
            //Keep default values if parsing fails
        }
    }

    //Database methods built into controller
    private UserAccount getUserFromDatabase(int userId) throws SQLException {
        String sql = "SELECT id, full_name, email FROM users WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UserAccount user = new UserAccount();
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        }
        return null;
    }

    private void updateUserInDatabase(int userId, UserAccount user) throws SQLException {
        //Update basic user info in users table
        String userSql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(userSql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " user record(s)");
        }

        //Update extended user info in user_profiles table
        updateExtendedUserData(userId, user);
    }

    private void updateExtendedUserData(int userId, UserAccount user) throws SQLException {
        //Get current form values
        String phone = phoneField.getText().trim();
        String dateOfBirth = getDateOfBirthString();
        String address = addressField.getText().trim();
        String username = usernameField.getText().trim();
        String currency = currencyComboBox.getValue();

        String sql = "UPDATE user_profiles SET phone_number = ?, date_of_birth = ?, address = ?, username = ?, preferred_currency = ? WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, dateOfBirth);
            pstmt.setString(3, address);
            pstmt.setString(4, username);
            pstmt.setString(5, currency);
            pstmt.setInt(6, userId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " user profile record(s)");
        }
    }

    private String getDateOfBirthString() {
        String day = dayComboBox.getValue();
        String month = monthComboBox.getValue();
        String year = yearComboBox.getValue();

        if (day != null && month != null && year != null) {
            return day + "/" + month + "/" + year;
        }
        return "";
    }

    private void printAllUsers() throws SQLException {
        String sql = "SELECT id, full_name, email FROM users";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("All users in database:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("full_name") +
                        ", Email: " + rs.getString("email"));
            }
        }
    }

    //Helper method to clear all fields
    private void clearAllFields() {
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        dayComboBox.setValue("01");
        monthComboBox.setValue("Jan");
        yearComboBox.setValue("2025");
        currencyComboBox.setValue("USD ($)");
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
        try {
            //Validate input
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();

            if (fullName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText("Full name cannot be empty.");
                alert.showAndWait();
                return;
            }

            if (email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText("Email cannot be empty.");
                alert.showAndWait();
                return;
            }

            //Create UserAccount with form data
            UserAccount user = new UserAccount();
            user.setFullName(fullName);
            user.setEmail(email);

            //Save to database (both basic and extended data)
            updateUserInDatabase(currentUserId, user);

            //Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your settings have been successfully saved to the database.");
            alert.showAndWait();

            System.out.println("User data saved to database successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving user data to database: " + e.getMessage());

            //Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Failed to save settings to database: " + e.getMessage());
            alert.showAndWait();
        }
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