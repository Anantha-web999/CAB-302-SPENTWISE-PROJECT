package com.example.trial.Home_add_account;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
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

    public Button dashbord_btn;
    public Button add_acc_btn;
    public Button Budget_btn;
    public Button Insights_btn;
    public Button reminders_btn;
    public Button child_acc_btn;
    public Button settingsButton;
    public Button logout_btn;
    public Text user_name;
    public Label login_date;
    public Label cheking_balance;
    public Label cheking_acc_num;
    public Label savings_balance;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
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

        // Account Number (Display full account number)
        String accountNum = account.getAccountNumber();
        Label accountNumberLabel = new Label(accountNum);  // Display full account number
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsPanel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Settings");
            Scene scene = new Scene(root);

            // Add CSS if needed
            scene.getStylesheets().add(getClass().getResource("/settingsPanel.css").toExternalForm());

            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading settings panel: " + e.getMessage());
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

    public void handlepayremind(ActionEvent event) throws IOException {
        Parent insightsView = FXMLLoader.load(getClass().getResource("/com/example/upcomingpayments/payment.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(insightsView);
    }
}
