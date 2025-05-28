//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial.insights_AI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {

        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/com/example/trial/Insights.fxml"));
        Image icon = new Image("Logo.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Insights App");
        primaryStage.setScene(new Scene(root, (double)1000.0F, (double)600.0F));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/// example
