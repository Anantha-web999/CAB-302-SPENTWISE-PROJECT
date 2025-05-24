package com.example.trial.logincontroller;

import com.example.trial.DB.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password");
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String query = "SELECT password FROM users WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String inputHashedPassword = hashPassword(password);

                if (storedHashedPassword.equals(inputHashedPassword)) {
                    //  Login success
                    loadHomePage(event);
                } else {
                    showAlert("Login Failed", "Incorrect password.");
                }
            } else {
                showAlert("Login Failed", "No user found with this email.");
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private void loadHomePage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load homepage");
        }
    }

    @FXML
    public void navigateToSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/signup.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load signup page");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
