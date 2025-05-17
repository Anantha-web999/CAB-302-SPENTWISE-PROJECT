package com.example.trial.Child_account;

import com.example.trial.DB.DatabaseHelper;
import com.example.trial.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Add_Child_Account_Controller {

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
    public void initialize() {
        DatabaseHelper.initializeDatabase();
        accountTypeCombo.getItems().addAll("Savings", "Checking", "Credit", "Loan");
    }

    @FXML
    private void handleSaveAccount() {
        String bankName = bankNameField.getText();
        String accountName = accountNameField.getText();
        String accountNumber = accountNumberField.getText();
        String bsb = bsbField.getText();
        String accountType = accountTypeCombo.getValue();

        if (bankName.isEmpty() || accountName.isEmpty() || accountNumber.isEmpty() ||
                bsb.isEmpty() || accountType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {
            String email = Session.getCurrentUserEmail();
            ChildBankAccountHelper.addChildAccount(
                    accountName, 0.0, bankName, accountNumber, bsb, accountType, email
            );
            showAlert(Alert.AlertType.INFORMATION, "Success", "Child account saved successfully!");
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save account: " + e.getMessage());
        }
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
        alert.setContentText(message);
        alert.showAndWait();
    }




    public void handleBack(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/child_control/manage_child_account.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }


}
