package com.example.trial.Home_add_account;

import com.example.hellofx.BankAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
                VBox accountCard = createAccountCard(account);
                accountsContainer.getChildren().add(accountCard);
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error",
                    "Failed to load accounts",
                    "Could not load bank accounts from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createAccountCard(BankAccount account) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setPrefWidth(200);

        card.setPadding(new javafx.geometry.Insets(15));

        Label bankNameLabel = new Label(account.getBankName());
        bankNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label accountTypeLabel = new Label(account.getAccountType());
        accountTypeLabel.setStyle("-fx-text-fill: gray;");

        // Use the actual balance from the account
        Label balanceLabel = new Label(account.getFormattedBalance());
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        card.getChildren().addAll(bankNameLabel, accountTypeLabel, balanceLabel);
        return card;
    }

    public void handleAddAccount(ActionEvent event) throws IOException {
        Parent addAccountView = FXMLLoader.load(getClass().getResource("/com/example/hellofx/add-account.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(addAccountView);
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