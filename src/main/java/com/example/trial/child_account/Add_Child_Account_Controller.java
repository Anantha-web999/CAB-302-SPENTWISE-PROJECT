package com.example.trial.child_account;

import com.example.trial.DB.DatabaseHelper;
import com.example.trial.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Add_Child_Account_Controller {

    @FXML private TextField bankNameField;
    @FXML private TextField accountNameField;
    @FXML private TextField accountNumberField;
    @FXML private TextField bsbField;
    @FXML private ComboBox<String> accountTypeCombo;

    @FXML
    public void initialize() {
        DatabaseHelper.initializeDatabase();
        accountTypeCombo.getItems().addAll("Savings", "Checking", "Credit", "Loan");
    }

    @FXML
    private void handleSaveAccount() {
        String bankName = bankNameField.getText().trim();
        String accountName = accountNameField.getText().trim();
        String accountNumber = accountNumberField.getText().trim();
        String bsb = bsbField.getText().trim();
        String accountType = accountTypeCombo.getValue();

        if (!isValidInput(bankName, accountName, accountNumber, bsb, accountType)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            String userEmail = Session.getCurrentUserEmail();
            ChildBankAccountHelper.addChildAccount(accountName, 0.0, bankName, accountNumber, bsb, accountType, userEmail);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Child account saved successfully!");
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save account: " + e.getMessage());
        }
    }

    private boolean isValidInput(String bank, String name, String number, String bsb, String type) {
        return !bank.isEmpty() && !name.isEmpty() && !number.isEmpty() && !bsb.isEmpty() && type != null;
    }

    private void clearFields() {
        bankNameField.clear();
        accountNameField.clear();
        accountNumberField.clear();
        bsbField.clear();
        accountTypeCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/com/example/child_control/manage_child_account.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(view));
        stage.show();
    }
}
