package com.example.trial;

import com.example.trial.DB.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Mainlogin extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        DatabaseHelper.initializeDatabase();


        System.out.println("DB Location: " + new File("spentwise.db").getAbsolutePath());


        URL fxmlLocation = getClass().getResource("/com/example/views/landing.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();


        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("SpentWise");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
