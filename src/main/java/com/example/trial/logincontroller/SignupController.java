package com.example.trial.logincontroller;

import com.example.trial.DB.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.regex.Pattern;

public class SignupController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;

    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Button togglePasswordButton;
    private boolean passwordVisible = false;

    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField visibleConfirmPasswordField;
    @FXML private Button toggleConfirmPasswordButton;
    private boolean confirmPasswordVisible = false;

    @FXML private Label signupErrorLabel;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$"
    );

    @FXML
    public void handleSignup(ActionEvent event) {
        signupErrorLabel.setVisible(false);

        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String pw = passwordVisible
                ? visiblePasswordField.getText()
                : passwordField.getText();
        String cpw = confirmPasswordVisible
                ? visibleConfirmPasswordField.getText()
                : confirmPasswordField.getText();

        if (fullName.isEmpty() || email.isEmpty() || pw.isEmpty() || cpw.isEmpty()) {
            showError("All fields are required.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Please enter a valid email address.");
            return;
        }
        if (!PASSWORD_PATTERN.matcher(pw).matches()) {
            showError("Password must be ≥8 chars, include 1 uppercase & 1 special char.");
            return;
        }
        if (!pw.equals(cpw)) {
            showError("Passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showError("An account with this email already exists.");
                return;
            }

            String hashed = hashPassword(pw);
            String insertSql = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, hashed);
            pstmt.executeUpdate();

            // on success, go back to login
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/login.fxml"));
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);

        } catch (Exception e) {
            showError("Unexpected error.");
            e.printStackTrace();
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
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
    private void toggleConfirmPasswordVisibility(ActionEvent event) {
        confirmPasswordVisible = !confirmPasswordVisible;
        if (confirmPasswordVisible) {
            visibleConfirmPasswordField.setText(confirmPasswordField.getText());
            visibleConfirmPasswordField.setVisible(true);
            visibleConfirmPasswordField.setManaged(true);
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            toggleConfirmPasswordButton.setText("Hide");
        } else {
            confirmPasswordField.setText(visibleConfirmPasswordField.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            visibleConfirmPasswordField.setVisible(false);
            visibleConfirmPasswordField.setManaged(false);
            toggleConfirmPasswordButton.setText("Show");
        }
    }

    private void showError(String msg) {
        signupErrorLabel.setText(msg);
        signupErrorLabel.setVisible(true);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @FXML
    public void navigateToLogin(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/login.fxml"));
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.setResizable(false);
    }
}
