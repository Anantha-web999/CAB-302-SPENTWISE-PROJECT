package com.example.trial.Home_add_account;

import com.example.trial.Home_add_account.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database
        DatabaseHelper.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(HomePage.class.getResource("/com/example/hellofx/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image icon = new Image("Logo.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}