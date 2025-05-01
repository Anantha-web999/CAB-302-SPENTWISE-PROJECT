package com.example.trial.logincontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password");
        } else {
            System.out.println("Login successful for: " + email);
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