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
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label signupErrorLabel;  // new

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    // At least one uppercase, one special, min 8 chars
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$"
    );

    @FXML
    public void handleSignup(ActionEvent event) {
        signupErrorLabel.setVisible(false); // reset

        String fullName = fullNameField.getText().trim();
        String email    = emailField.getText().trim();
        String pw       = passwordField.getText();
        String cpw      = confirmPasswordField.getText();

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

            // success → go to login
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/login.fxml"));
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            showError("Encryption error: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error.");
            e.printStackTrace();
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
