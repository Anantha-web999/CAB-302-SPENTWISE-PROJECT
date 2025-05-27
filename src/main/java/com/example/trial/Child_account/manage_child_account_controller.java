package com.example.trial.Child_account;


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

    public Button add_acc_btn;
    public Button add_child_acc_btn1;
    @FXML
    private TableView<ChildAccount> tableView;

    @FXML
    private TableColumn<ChildAccount, String> childName;

    @FXML
    private TableColumn<ChildAccount, Double> budget;

    @FXML
    private TableColumn<ChildAccount, String> details;

    @FXML
    private TableColumn<ChildAccount, Void> detailsColumn;
    @FXML
    private TableColumn<ChildAccount, Void> editColumn;
    @FXML
    private TableColumn<ChildAccount, Void> deleteColumn;

    @FXML
    private TableColumn<ChildAccount, String> bankNameColumn;

    @FXML
    private TableColumn<ChildAccount, Double> budgetColumn;

    @FXML
    private TableColumn<ChildAccount, Double> balanceColumn;




    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load columns from ChildAccount properties
        childName.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        childName.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        bankNameColumn.setCellValueFactory(new PropertyValueFactory<>("bankName"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        // Set up "Info" button for details
        detailsColumn.setCellFactory(getButtonCellFactory("Info", (child) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Child Account Details");
            alert.setHeaderText("Details for: " + child.getAccountName());
            alert.setContentText("Budget: $" + child.getBudget());
            alert.showAndWait();
        }));

        // Set up "Update" button
        editColumn.setCellFactory(getButtonCellFactory("Update", (child) -> {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(child.getBudget()));
            dialog.setTitle("Update Budget");
            dialog.setHeaderText("Updating budget for: " + child.getAccountName());
            dialog.setContentText("Enter new budget:");

            dialog.showAndWait().ifPresent(newBudgetStr -> {
                try {
                    double newBudget = Double.parseDouble(newBudgetStr);
                    child.setBudget(newBudget);


                    try {
                        ChildBankAccountHelper.updateBudget(child.getId(), newBudget);
                        tableView.refresh(); // Refresh UI after successful update
                    } catch (SQLException e) {
                        Alert sqlError = new Alert(Alert.AlertType.ERROR);
                        sqlError.setTitle("Database Error");
                        sqlError.setHeaderText("Unable to update budget.");
                        sqlError.setContentText(e.getMessage());
                        sqlError.showAndWait();
                    }

                } catch (NumberFormatException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Input Error");
                    error.setHeaderText("Invalid Budget");
                    error.setContentText("Please enter a valid number.");
                    error.showAndWait();
                }
            });
        }));


        deleteColumn.setCellFactory(getButtonCellFactory("Delete", (child) -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Are you sure you want to delete: " + child.getAccountName() + "?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ChildBankAccountHelper.deleteChildAccount(child.getId());
                        tableView.getItems().remove(child);
                    } catch (SQLException e) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Database Error");
                        error.setHeaderText("Unable to delete child account.");
                        error.setContentText(e.getMessage());
                        error.showAndWait();
                    }
                }
            });
        }));

        loadChildAccounts();


    }


    private void loadChildAccounts() {
        ObservableList<ChildAccount> accounts = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db")) {
            String email = Session.getCurrentUserEmail();
            if (email == null) return;

            // ✅ This line might throw SQLException – it must be inside try
            int userId = ChildBankAccountHelper.getUserIdByEmail(email);

            String sql = "SELECT id, account_name, budget, balance, bank_name FROM child_accounts WHERE user_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accounts.add(new ChildAccount(
                        rs.getInt("id"),
                        rs.getString("account_name"),
                        rs.getDouble("budget"),
                        rs.getDouble("balance"),
                        rs.getString("bank_name")
                ));
            }

            tableView.setItems(accounts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void handleAddChildAccount(ActionEvent event) throws IOException {
        Parent insightsView = FXMLLoader.load(getClass().getResource("/com/example/child_control/Add_child_account.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.setRoot(insightsView);
    }

    public void handleBack(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }

    private Callback<TableColumn<ChildAccount, Void>, TableCell<ChildAccount, Void>> getButtonCellFactory(
            String label, java.util.function.Consumer<ChildAccount> handler) {
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

}
