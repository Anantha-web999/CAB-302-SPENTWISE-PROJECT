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
        String sql = "SELECT account_name, account_type FROM child_accounts WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String userEmail = Session.getCurrentUserEmail();  // Your session-based method

            if (userEmail == null) {
                System.out.println("No user is currently logged in.");
                return;
            }

            pstmt.setString(1, userEmail);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("account_name");
                String details = rs.getString("account_type");
                accounts.add(new ChildAccount(name, 0.0, details));  // Still using dummy budget
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
