package com.example.trial.Home_add_account;

import com.example.trial.DB.DatabaseHelper;
import com.example.trial.Session;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML private Button dashbord_btn, add_acc_btn, Budget_btn, Insights_btn, reminders_btn, child_acc_btn, settingsButton, logout_btn;
    @FXML private Text user_name;
    @FXML private Label login_date, cheking_balance, cheking_acc_num, savings_balance, savings_acc_num, income_lbl, expense_lbl;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> dateColumn, descriptionColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private HBox accountsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBankAccounts();
        setWelcomeMessage();
        setLoginDate();
        setupTransactionTable();
        loadRecentTransactions();
    }

    private void setWelcomeMessage() {
        String email = Session.getCurrentUserEmail();
        if (email != null) {
            try {
                String fullName = BankAccountHelper.getFullNameByEmail(email);
                user_name.setText(fullName != null ? "Welcome, " + fullName : "Welcome, user");
            } catch (SQLException e) {
                user_name.setText("Welcome");
                e.printStackTrace();
            }
        } else {
            user_name.setText("Session not found!");
        }
    }

    private void setLoginDate() {
        login_date.setText("Today, " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")));
    }

    private void setupTransactionTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadBankAccounts() {
        try {
            String email = Session.getCurrentUserEmail();
            if (email == null) {
                showAlert(Alert.AlertType.ERROR, "Session Error", null, "No user logged in.");
                return;
            }

            List<BankAccount> accounts = BankAccountHelper.getAllBankAccounts(email);
            accountsContainer.getChildren().clear();

            for (BankAccount account : accounts) {
                accountsContainer.getChildren().add(createAccountCard(account));
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load accounts", e.getMessage());
            e.printStackTrace();
        }
    }

    private AnchorPane createAccountCard(BankAccount account) {
        AnchorPane accountPane = new AnchorPane();
        accountPane.setPrefSize(295, 150);
        accountPane.getStyleClass().addAll("account", "account_gradient");

        Label balanceLabel = createLabel(account.getFormattedBalance(), "account_balance", 14.0, 25.0);
        Label accountNumberLabel = createLabel(account.getAccountNumber(), "account_number", 14.0, null);
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
        accountPane.setOnMouseClicked(e -> showAlert(Alert.AlertType.INFORMATION, "Account Info", null,
                "Bank: " + account.getBankName() + "\n" +
                        "Type: " + account.getAccountType() + "\n" +
                        "Balance: " + account.getFormattedBalance()));

        return accountPane;
    }

    private Label createLabel(String text, String styleClass, Double leftAnchor, Double topAnchor) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        if (leftAnchor != null) AnchorPane.setLeftAnchor(label, leftAnchor);
        if (topAnchor != null) AnchorPane.setTopAnchor(label, topAnchor);
        return label;
    }

    @FXML private void handleAddAccount(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/hellofx/add-account.fxml");
    }

    @FXML private void handleSettingsClick(ActionEvent event) {
        SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> swingNode.setContent(new SettingsPanel()));
        VBox root = new VBox(swingNode);
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML private void handleInsightsClick(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/trial/Insights.fxml");
    }

    @FXML private void handlepayremind(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/upcomingpayments/payment.fxml");
    }

    @FXML private void handleChildAccount(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/child_control/manage_child_account.fxml");
    }

    @FXML private void handleLogout(ActionEvent event) {
        try {
            Session.clearSession();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/views/landing.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecentTransactions() {
        try {
            List<Transaction> transactions = TransactionHelper.getRecentTransactions();
            transactionTable.getItems().setAll(transactions);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load transactions", e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshAccounts() {
        loadBankAccounts();
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(view);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
