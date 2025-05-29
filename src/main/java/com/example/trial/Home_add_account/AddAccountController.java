package com.example.trial.Home_add_account;

import com.example.trial.Session;
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

    @FXML private TextField bankNameField;
    @FXML private TextField accountNameField;
    @FXML private TextField accountNumberField;
    @FXML private TextField bsbField;
    @FXML private ComboBox<String> accountTypeCombo;

    @FXML
    public void initialize() {
        accountTypeCombo.getItems().addAll("Savings", "Checking", "Credit", "Loan");
    }

    @FXML
    private void handleSaveAccount() {
        String bankName = bankNameField.getText().trim();
        String accountName = accountNameField.getText().trim();
        String accountNumber = accountNumberField.getText().trim();
        String bsb = bsbField.getText().trim();
        String accountType = accountTypeCombo.getValue();

        if (bankName.isEmpty() || accountName.isEmpty() ||
                accountNumber.isEmpty() || bsb.isEmpty() || accountType == null) {
            showAlert(AlertType.ERROR, "Error", "Missing Information", "Please fill in all the fields.");
            return;
        }

        if (!accountNumber.matches("\\d{8}")) {
            showAlert(AlertType.ERROR, "Invalid Input", "Invalid Account Number", "Account number must be exactly 8 digits.");
            return;
        }

        if (!bsb.matches("\\d{6}")) {
            showAlert(AlertType.ERROR, "Invalid Input", "Invalid BSB Number", "BSB must be exactly 6 digits.");
            return;
        }

        try {
            String email = Session.getInstance().getCurrentUserEmail();
            if (email == null) {
                showAlert(AlertType.ERROR, "Session Error", null, "User is not logged in.");
                return;
            }

            int userId = BankAccountHelper.getUserIdByEmail(email);
            BankAccountHelper.addBankAccount(userId, bankName, accountName, accountNumber, bsb, accountType);
            showAlert(AlertType.INFORMATION, "Success", "Account Saved", "The bank account has been successfully saved.");

            clearFields();
            returnToMainView();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    private void returnToMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Parent root = loader.load();

        // Only call refresh if controller was loaded correctly
        Object controller = loader.getController();
        if (controller instanceof HomePageController homepageController) {
            homepageController.refreshAccounts();
        }

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

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }
}
