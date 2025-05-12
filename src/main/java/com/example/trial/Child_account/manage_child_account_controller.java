package com.example.trial.Child_account;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class manage_child_account_controller {
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
