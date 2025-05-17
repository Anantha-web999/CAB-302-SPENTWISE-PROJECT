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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        childName.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        budget.setCellValueFactory(new PropertyValueFactory<>("budget"));
        details.setCellValueFactory(new PropertyValueFactory<>("details"));

        loadChildAccounts();
    }

    private void loadChildAccounts() {
        ObservableList<ChildAccount> accounts = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db")) {
            String email = Session.getCurrentUserEmail();
            if (email == null) return;

            int userId = ChildBankAccountHelper.getUserIdByEmail(email);

            String sql = "SELECT account_name, budget, details FROM child_accounts WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accounts.add(new ChildAccount(
                        rs.getString("account_name"),
                        rs.getDouble("budget")
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
}
