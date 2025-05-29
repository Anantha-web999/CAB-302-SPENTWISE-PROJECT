package com.example.trial.Home_add_account;

import com.example.trial.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    // Navigation Buttons
    public Button dashbord_btn, add_acc_btn, Budget_btn, Insights_btn, reminders_btn, child_acc_btn, settingsButton, logout_btn;

    // User Info Labels
    public Text user_name;
    public Label login_date, cheking_balance, cheking_acc_num, savings_balance, savings_acc_num, income_lbl, expense_lbl;

    // Account UI
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> dateColumn, descriptionColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private HBox accountsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUserInfo();
        setupDate();
        setupTransactionTable();
        loadBankAccounts();
        loadRecentTransactions();
    }

    private void setupUserInfo() {
        String email = Session.getInstance().getCurrentUserEmail();
        if (email != null) {
            try {
                String fullName = BankAccountHelper.getFullNameByEmail(email);
                user_name.setText("Welcome, " + fullName);
            } catch (SQLException e) {
                user_name.setText("Welcome, user");
                e.printStackTrace();
            }
        } else {
            user_name.setText("Session not found!");
        }
    }

    private void setupDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        login_date.setText(LocalDate.now().format(formatter));
    }

    private void setupTransactionTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadBankAccounts() {
        try {
            String email = Session.getInstance().getCurrentUserEmail();
            if (email == null) {
                showAlert(AlertType.ERROR, "Session Error", null, "No user logged in.");
                return;
            }

            List<BankAccount> accounts = BankAccountHelper.getAllBankAccounts(email);
            accountsContainer.getChildren().clear();

            for (BankAccount account : accounts) {
                accountsContainer.getChildren().add(createAccountCard(account));
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Failed to load accounts", e.getMessage());
            e.printStackTrace();
        }
    }

    private AnchorPane createAccountCard(BankAccount account) {
        AnchorPane accountPane = new AnchorPane();
        accountPane.setPrefSize(295, 150);
        accountPane.getStyleClass().addAll("account", "account_gradient");

        Label balanceLabel = new Label(account.getFormattedBalance());
        balanceLabel.getStyleClass().add("account_balance");
        AnchorPane.setLeftAnchor(balanceLabel, 14.0);
        AnchorPane.setTopAnchor(balanceLabel, 25.0);

        Label accountNumberLabel = new Label(account.getAccountNumber());
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
                "Bank: " + account.getBankName() + "\nType: " + account.getAccountType() + "\nBalance: " + account.getFormattedBalance()));
        return accountPane;
    }

    private void loadRecentTransactions() {
        try {
            List<Transaction> transactions = TransactionHelper.getRecentTransactions();
            transactionTable.getItems().setAll(transactions);
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Could not load transactions", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private void handleAddAccount(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/hellofx/add-account.fxml");
    }

    @FXML private void handleSettingsClick(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/settingspanel/SettingsPanel.fxml");
    }

    @FXML private void handleInsightsClick(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/trial/Insights.fxml");
    }

    @FXML private void openSpendingAdvisor(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/trial/SpendingAdvisor.fxml");
    }

    @FXML private void handlepayremind(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/upcomingpayments/payment.fxml");
    }

    @FXML private void handleChildAccount(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/child_control/manage_child_account.fxml");
    }

    @FXML private void handleLogout(ActionEvent event) {
        try {
            Session.getInstance().clearSession();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/landing.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent newView = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(newView);
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshAccounts() {
        loadBankAccounts();
    }
}
