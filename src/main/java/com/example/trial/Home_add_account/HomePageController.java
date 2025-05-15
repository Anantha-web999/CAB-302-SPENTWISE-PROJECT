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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
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



    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private HBox accountsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBankAccounts();
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        loadRecentTransactions();

    }

    private void loadBankAccounts() {
        try {
            List<BankAccount> accounts = BankAccountHelper.getAllBankAccounts();
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
        AnchorPane accountPane = new AnchorPane();
        accountPane.setPrefHeight(150);
        accountPane.setPrefWidth(295);
        accountPane.getStyleClass().addAll("account", "account_gradient");
        Label balanceLabel = new Label(account.getFormattedBalance());
        balanceLabel.getStyleClass().add("account_balance");
        AnchorPane.setLeftAnchor(balanceLabel, 14.0);
        AnchorPane.setTopAnchor(balanceLabel, 25.0);
        String accountNum = account.getAccountNumber();
        Label accountNumberLabel = new Label(accountNum);
        accountNumberLabel.getStyleClass().add("account_number");
        AnchorPane.setLeftAnchor(accountNumberLabel, 14.0);
        AnchorPane.setBottomAnchor(accountNumberLabel, 45.0);
        FontAwesomeIconView bankIcon = new FontAwesomeIconView();
        bankIcon.setGlyphName("BANK");
        bankIcon.setSize("30");
        AnchorPane.setRightAnchor(bankIcon, 14.0);
        AnchorPane.setTopAnchor(bankIcon, 7.0);
        Text bankNameText = new Text(account.getBankName());
        AnchorPane.setLeftAnchor(bankNameText, 14.0);
        AnchorPane.setBottomAnchor(bankNameText, 10.0);
        accountPane.getChildren().addAll(balanceLabel, accountNumberLabel, bankIcon, bankNameText);
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
        SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            SettingsPanel settingsPanel = new SettingsPanel();
            swingNode.setContent(settingsPanel);
        });

        VBox root = new VBox(swingNode);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
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

    private void loadRecentTransactions() {
        try {
            List<Transaction> transactions = TransactionHelper.getRecentTransactions();
            transactionTable.getItems().clear();
            transactionTable.getItems().addAll(transactions);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load transactions", e.getMessage());
            e.printStackTrace();
        }
    }


    public void handleChildAccount(ActionEvent event) throws IOException {
        Parent insightsView = FXMLLoader.load(getClass().getResource("/com/example/child_control/manage_child_account.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(insightsView);
    }
}

