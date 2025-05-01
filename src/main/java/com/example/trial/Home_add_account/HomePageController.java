package com.example.trial.Home_add_account;

import com.example.trial.settings.SettingsPanel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private HBox accountsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBankAccounts();
    }

    private void loadBankAccounts() {
        try {
            List<BankAccount> accounts = DatabaseHelper.getAllBankAccounts();
            accountsContainer.getChildren().clear();

            for (BankAccount account : accounts) {
                AnchorPane accountCard = createAccountCard(account);
                accountsContainer.getChildren().add(accountCard);
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error",
                    "Failed to load accounts",
                    "Could not load bank accounts from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private AnchorPane createAccountCard(BankAccount account) {
        // Create an AnchorPane for the account card
        AnchorPane accountPane = new AnchorPane();
        accountPane.setPrefHeight(150);
        accountPane.setPrefWidth(295);
        accountPane.getStyleClass().addAll("account", "account_gradient");

        // Balance Label
        Label balanceLabel = new Label(account.getFormattedBalance());
        balanceLabel.getStyleClass().add("account_balance");
        AnchorPane.setLeftAnchor(balanceLabel, 14.0);
        AnchorPane.setTopAnchor(balanceLabel, 25.0);

        // Account Number (Masked + Last 4 Digits)
        String accountNum = account.getAccountNumber();
        String maskedAccount = "**** **** **** ";  // Mask the first part
        String lastFourDigits = accountNum.substring(accountNum.length() - 4);  // Extract the last 4 digits
        Label accountNumberLabel = new Label(maskedAccount + lastFourDigits);  // Combine both parts
        accountNumberLabel.getStyleClass().add("account_number");
        AnchorPane.setLeftAnchor(accountNumberLabel, 14.0);
        AnchorPane.setBottomAnchor(accountNumberLabel, 45.0);

        // Bank Icon (FontAwesome)
        FontAwesomeIconView bankIcon = new FontAwesomeIconView();
        bankIcon.setGlyphName("BANK");
        bankIcon.setSize("30");
        AnchorPane.setRightAnchor(bankIcon, 14.0);
        AnchorPane.setTopAnchor(bankIcon, 7.0);

        // Bank Name (instead of "Checking Account")
        Text bankNameText = new Text(account.getBankName());  // Display bank name here
        AnchorPane.setLeftAnchor(bankNameText, 14.0);
        AnchorPane.setBottomAnchor(bankNameText, 10.0);  // Position at the bottom left

        // Add all elements to the AnchorPane
        accountPane.getChildren().addAll(balanceLabel, accountNumberLabel, bankIcon, bankNameText);

        // Set click event for the AnchorPane to show account details
        accountPane.setOnMouseClicked(e -> showAlert(AlertType.INFORMATION, "Account Info", null,
                "Bank: " + account.getBankName() + "\n" +
                        "Type: " + account.getAccountType() + "\n" +
                        "Balance: " + account.getFormattedBalance()));

        return accountPane;
    }





    @FXML
    private void handleAddAccount(ActionEvent event) throws IOException {
        Parent addAccountView = FXMLLoader.load(getClass().getResource("/com/example/hellofx/add-account.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(addAccountView);
    }

    @FXML
    private void handleSettingsClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hellofx/SettingsPanel.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInsightsClick(ActionEvent event) throws IOException {
        Parent insightsView = FXMLLoader.load(getClass().getResource("/com/example/trial/Insights.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(insightsView);
    }

    public void refreshAccounts() {
        loadBankAccounts();
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
