package com.example.trial.logincontroller;

import com.example.trial.DB.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Button togglePasswordButton;
    @FXML private Label errorLabel;
    @FXML private Label capsLockLabel;

    private boolean passwordVisible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Watch for key events to detect Caps Lock
        passwordField.setOnKeyReleased(e -> checkCapsLock());
        visiblePasswordField.setOnKeyReleased(e -> checkCapsLock());
    }

    private void checkCapsLock() {
        boolean capsOn = Toolkit.getDefaultToolkit()
                .getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        capsLockLabel.setVisible(capsOn);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email    = emailField.getText().trim();
        String password = passwordVisible
                ? visiblePasswordField.getText()
                : passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT password FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String inputHash  = hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    loadHomePage(event);
                } else {
                    showError("Incorrect password.");
                }
            } else {
                showError("No user found with this email.");
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            togglePasswordButton.setText("Hide");
        } else {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            togglePasswordButton.setText("Show");
        }
    }

    @FXML
    public void navigateToSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/views/signup.fxml")
            );
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (Exception e) {
            showError("Cannot load signup page");
            e.printStackTrace();
        }
    }

    private void loadHomePage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hellofx/homepage.fxml")
            );
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (Exception e) {
            showError("Cannot load homepage");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
