package com.example.trial.Home_add_account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddAccountController {

    @FXML
    private TextField bankNameField;
    @FXML
    private TextField accountNameField;
    @FXML
    private TextField accountNumberField;
    @FXML
    private TextField bsbField;
    @FXML
    private ComboBox<String> accountTypeCombo;

    @FXML
    private void handleSaveAccount() {
        // Validate input fields
        String bankName = bankNameField.getText();
        String accountName = accountNameField.getText();
        String accountNumber = accountNumberField.getText();
        String bsb = bsbField.getText();
        String accountType = accountTypeCombo.getValue();

        if (bankName.isEmpty() || accountName.isEmpty() ||
                accountNumber.isEmpty() || bsb.isEmpty() || accountType == null) {
            showAlert(AlertType.ERROR, "Error", "Missing Information",
                    "Please fill in all the fields.");
            return;
        }

        try {
            DatabaseHelper.addBankAccount(bankName, accountName, accountNumber, bsb, accountType);
            showAlert(AlertType.INFORMATION, "Success", "Account Saved",
                    "The bank account has been successfully saved.");
            clearFields();

            // Return to main view
            returnToMainView();

        } catch (SQLException | IOException e) {
            showAlert(AlertType.ERROR, "Error", "Database Error",
                    "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void returnToMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hellofx/hello-view.fxml"));
        Parent root = loader.load();

        // Get the controller and refresh accounts
        HomePageController controller = loader.getController();
        controller.refreshAccounts();

        // Get current stage
        Stage stage = (Stage) bankNameField.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        bankNameField.clear();
        accountNameField.clear();
        accountNumberField.clear();
        bsbField.clear();
        accountTypeCombo.getSelectionModel().clearSelection();
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }
}