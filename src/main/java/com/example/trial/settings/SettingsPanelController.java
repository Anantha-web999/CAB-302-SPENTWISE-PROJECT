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
    @FXML private ImageView logoImage;

    @FXML private Button accountButton;
    @FXML private Button helpSupportButton;
    @FXML private Button aboutUsButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;

    private final String activeStyle = "-fx-background-color: #17202a; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-weight: bold; -fx-font-size: 14px;";
    private final String inactiveStyle = "-fx-background-color: #1a5276; -fx-text-fill: #fbfcfc; -fx-font-family: 'Lato'; -fx-font-size: 14px;";

    //We're using user ID 1 as the default user for now
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Make the account button look active when we first load the page
        setActiveButton(accountButton);

        //Set up all the date dropdown options
        setupDateDropdowns();

        //Apply some nice rounded corners to the logo if it exists
        if (logoImage != null) {
            try {
                Rectangle clip = new Rectangle(
                        logoImage.getFitWidth(),
                        logoImage.getFitHeight()
                );
                clip.setArcWidth(30.0);
                clip.setArcHeight(30.0);
                logoImage.setClip(clip);

                //Make sure the clip resizes when the image does
                logoImage.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                    clip.setWidth(newValue.getWidth());
                    clip.setHeight(newValue.getHeight());
                });
            } catch (Exception e) {
                //If something goes wrong with the logo styling, just log it and keep going
                System.err.println("Couldn't apply rounded corners to logo: " + e.getMessage());
                e.printStackTrace();
            }
        }

        //Make sure we have our extended user table ready to go
        setupExtendedUserTable();

        //Load up the user's current data from the database
        loadUserData();
    }

    private void setupDateDropdowns() {
        //Add days 1 through 31
        for (int i = 1; i <= 31; i++) {
            dayComboBox.getItems().add(String.format("%02d", i));
        }

        //Add all the months in short form
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthComboBox.getItems().addAll(months);

        //Add years from 100 years ago up to now
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearComboBox.getItems().add(String.valueOf(i));
        }

        //Start with a default date so the dropdowns aren't empty
        dayComboBox.setValue("01");
        monthComboBox.setValue("Jan");
        yearComboBox.setValue("2025");
    }

    //This creates a new table to store extra user info that wasn't in the original design
    private void setupExtendedUserTable() {
        try {
            DatabaseHelper.initializeDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS user_profiles (" +
                    "user_id INTEGER PRIMARY KEY, " +
                    "phone_number TEXT, " +
                    "date_of_birth TEXT, " +
                    "address TEXT, " +
                    "username TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)";

            try (Connection conn = DatabaseHelper.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.execute(sql);

                //Make sure the current user has a profile entry
                createUserProfileIfNeeded(currentUserId);

                System.out.println("User profile table is ready to go");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Something went wrong setting up the user profile table: " + e.getMessage());
        }
    }

    private void createUserProfileIfNeeded(int userId) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM user_profiles WHERE user_id = ?";
        String insertSql = "INSERT INTO user_profiles (user_id, phone_number, date_of_birth, address, username) VALUES (?, '', '', '', '')";

        try (Connection conn = DatabaseHelper.getConnection()) {
            //See if this user already has a profile
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    //They don't have one yet, so let's create it
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.executeUpdate();
                        System.out.println("Created a new profile for user " + userId);
                    }
                }
            }
        }
    }

    private void loadUserData() {
        try {
            //Show what users we have in the database for debugging
            showAllUsers();

            UserAccount user = getUserFromDatabase(currentUserId);

            if (user != null) {
                //Fill in the basic info from the main users table
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

                //Now get the extra stuff from the profiles table
                loadExtraUserData(currentUserId);

                System.out.println("Successfully loaded data for: " + user.getFullName());
            } else {
                //Couldn't find this user, so clear everything
                clearAllTheFields();
                System.out.println("Couldn't find user " + currentUserId + " in the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Had trouble loading user data: " + e.getMessage());

            //Let the user know something went wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Problem");
            alert.setHeaderText("Couldn't load your data");
            alert.setContentText("There was an issue connecting to the database: " + e.getMessage());
            alert.showAndWait();

            //Clear everything as a fallback
            clearAllTheFields();
        }
    }

    private void loadExtraUserData(int userId) throws SQLException {
        String sql = "SELECT phone_number, date_of_birth, address, username FROM user_profiles WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                //Fill in the phone number
                String phone = rs.getString("phone_number");
                if (phone != null && !phone.isEmpty()) {
                    phoneField.setText(phone);
                } else {
                    phoneField.setText("");
                }

                //Set up the date of birth dropdowns
                String dateOfBirth = rs.getString("date_of_birth");
                if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                    parseDateAndSetDropdowns(dateOfBirth);
                } else {
                    //Keep the default date we set earlier
                    dayComboBox.setValue("01");
                    monthComboBox.setValue("Jan");
                    yearComboBox.setValue("2025");
                }

                //Fill in the address
                String address = rs.getString("address");
                if (address != null && !address.isEmpty()) {
                    addressField.setText(address);
                } else {
                    addressField.setText("");
                }

                //Fill in the username
                String username = rs.getString("username");
                if (username != null && !username.isEmpty()) {
                    usernameField.setText(username);
                } else {
                    usernameField.setText("");
                }

                System.out.println("Loaded all the extra user details");
            }
        }
    }

    private void parseDateAndSetDropdowns(String dateOfBirth) {
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
            System.err.println("Couldn't parse the date of birth: " + e.getMessage());
            //If we can't parse it, just leave the defaults
        }
    }

    //Get the basic user info from the main users table
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
        //First update the main user info
        String userSql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(userSql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, userId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " main user record");
        }

        //Then update all the extra profile stuff
        updateExtraUserData(userId, user);
    }

    private void updateExtraUserData(int userId, UserAccount user) throws SQLException {
        //Get what's currently in the form
        String phone = phoneField.getText().trim();
        String dateOfBirth = buildDateFromDropdowns();
        String address = addressField.getText().trim();
        String username = usernameField.getText().trim();

        String sql = "UPDATE user_profiles SET phone_number = ?, date_of_birth = ?, address = ?, username = ? WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, dateOfBirth);
            pstmt.setString(3, address);
            pstmt.setString(4, username);
            pstmt.setInt(5, userId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " profile record");
        }
    }

    private String buildDateFromDropdowns() {
        String day = dayComboBox.getValue();
        String month = monthComboBox.getValue();
        String year = yearComboBox.getValue();

        if (day != null && month != null && year != null) {
            return day + "/" + month + "/" + year;
        }
        return "";
    }

    private void showAllUsers() throws SQLException {
        String sql = "SELECT id, full_name, email FROM users";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Here are all the users we have:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("full_name") +
                        ", Email: " + rs.getString("email"));
            }
        }
    }

    //Clear out all the form fields
    private void clearAllTheFields() {
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        dayComboBox.setValue("01");
        monthComboBox.setValue("Jan");
        yearComboBox.setValue("2025");
    }

    private void setActiveButton(Button activeButton) {
        //Make all buttons look inactive first
        accountButton.setStyle(inactiveStyle);
        helpSupportButton.setStyle(inactiveStyle);
        aboutUsButton.setStyle(inactiveStyle);

        //Then make the one we clicked look active
        activeButton.setStyle(activeStyle);
    }

    @FXML
    private void handleAccountButton() {
        setActiveButton(accountButton);
        //This button is already active since we're on the account page
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
            System.err.println("Couldn't load the help and support page: " + e.getMessage());
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
            System.err.println("Couldn't load the about us page: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveButton() {
        try {
            //Make sure they filled in the required stuff
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();

            if (fullName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("You need to enter your full name.");
                alert.showAndWait();
                return;
            }

            if (email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops!");
                alert.setHeaderText(null);
                alert.setContentText("You need to enter your email address.");
                alert.showAndWait();
                return;
            }

            //Create a user object with the form data
            UserAccount user = new UserAccount();
            user.setFullName(fullName);
            user.setEmail(email);

            //Save everything to the database
            updateUserInDatabase(currentUserId, user);

            //Let them know it worked
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("All Done!");
            alert.setHeaderText(null);
            alert.setContentText("Your settings have been saved successfully.");
            alert.showAndWait();

            System.out.println("Everything saved successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Had trouble saving the data: " + e.getMessage());

            //Let them know something went wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Problem");
            alert.setHeaderText("Couldn't save your changes");
            alert.setContentText("There was a database error: " + e.getMessage());
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
     * This method lets other parts of the code set user data in the form
     * @param userAccount the user account data to display
     */
    public void setUserData(UserAccount userAccount) {
        //Fill in all the form fields with the provided data
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

        //Handle the date of birth if it's provided
        if (userAccount.getDateOfBirth() != null) {
            String[] dobParts = userAccount.getDateOfBirth().split("/");
            if (dobParts.length == 3) {
                int month = Integer.parseInt(dobParts[0]);
                dayComboBox.setValue(dobParts[1]);
                monthComboBox.setValue(getMonthName(month));
                yearComboBox.setValue(dobParts[2]);
            }
        }
    }

    /**
     * Convert a month number to the short month name we use
     */
    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month >= 1 && month <= 12) {
            return months[month - 1];
        }
        return "Jan"; //Default to January if something's wrong
    }
}