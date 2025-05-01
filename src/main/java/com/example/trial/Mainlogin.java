package com.example.trial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Mainlogin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/com/example/views/landing.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 600); // Set fixed size here
        stage.setTitle("SpentWise");
        stage.setScene(scene);
        stage.setResizable(false); // Prevent resizing
        stage.show();
    }

    

    public static void main(String[] args) {
        launch();
    }
}