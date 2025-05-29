package com.example.trial.child_account;

import com.example.trial.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class manage_child_account_controller implements Initializable {

    @FXML private TableView<ChildAccount> tableView;
    @FXML private TableColumn<ChildAccount, String> childName;
    @FXML private TableColumn<ChildAccount, String> bankNameColumn;
    @FXML private TableColumn<ChildAccount, Double> budgetColumn;
    @FXML private TableColumn<ChildAccount, Double> balanceColumn;
    @FXML private TableColumn<ChildAccount, Void> detailsColumn;
    @FXML private TableColumn<ChildAccount, Void> editColumn;
    @FXML private TableColumn<ChildAccount, Void> deleteColumn;
    @FXML public Button add_acc_btn;
    @FXML public Button add_child_acc_btn1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        childName.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        bankNameColumn.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        detailsColumn.setCellFactory(getButtonCellFactory("Info", this::showAccountDetails));
        editColumn.setCellFactory(getButtonCellFactory("Update", this::updateAccountBudget));
        deleteColumn.setCellFactory(getButtonCellFactory("Delete", this::deleteChildAccount));

        loadChildAccounts();
    }

    private void loadChildAccounts() {
        ObservableList<ChildAccount> accounts = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db")) {
            String email = Session.getInstance().getCurrentUserEmail();
            if (email == null) return;

            int userId = ChildBankAccountHelper.getUserIdByEmail(email);
            String sql = "SELECT id, account_name, budget, balance, bank_name FROM child_accounts WHERE user_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        accounts.add(new ChildAccount(
                                rs.getInt("id"),
                                rs.getString("account_name"),
                                rs.getDouble("budget"),
                                rs.getDouble("balance"),
                                rs.getString("bank_name")
                        ));
                    }
                }
            }

            tableView.setItems(accounts);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private Callback<TableColumn<ChildAccount, Void>, TableCell<ChildAccount, Void>> getButtonCellFactory(String label, java.util.function.Consumer<ChildAccount> handler) {
        return col -> new TableCell<>() {
            private final Button btn = new Button(label);

            {
                btn.setOnAction(event -> {
                    ChildAccount data = getTableView().getItems().get(getIndex());
                    handler.accept(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };
    }

    private void showAccountDetails(ChildAccount account) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Child Account Details");
        alert.setHeaderText(null);
        alert.setContentText("Account Name: " + account.getAccountName()
                + "\nBank: " + account.getBankName()
                + "\nBudget: $" + account.getBudget()
                + "\nBalance: $" + account.getBalance());
        alert.showAndWait();
    }

    private void updateAccountBudget(ChildAccount account) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(account.getBudget()));
        dialog.setTitle("Update Budget");
        dialog.setHeaderText("Updating budget for: " + account.getAccountName());
        dialog.setContentText("Enter new budget:");

        dialog.showAndWait().ifPresent(newBudgetStr -> {
            try {
                double newBudget = Double.parseDouble(newBudgetStr);
                account.setBudget(newBudget);
                ChildBankAccountHelper.updateBudget(account.getId(), newBudget);
                tableView.refresh();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to update budget: " + e.getMessage());
            }
        });
    }

    private void deleteChildAccount(ChildAccount account) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete: " + account.getAccountName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ChildBankAccountHelper.deleteChildAccount(account.getId());
                    tableView.getItems().remove(account);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete child account: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleAddChildAccount(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/child_control/Add_child_account.fxml");
    }

    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/hellofx/homepage.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
