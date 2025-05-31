package com.example.trial.upcomingpayments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainPayment extends Application {

    /**
     * This method is automatically called when the application starts.
     * It sets up the main window (Stage) and loads the layout from the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the payment screen layout from the payment.fxml file
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/upcomingpayments/payment.fxml"));
        // Set the window (stage) title at the top bar
        primaryStage.setTitle("Payment Reminder");
        // Set the size and content of the window
        primaryStage.setScene(new Scene(root, 1000, 600));
        // Show the window on the screen
        primaryStage.show();
    }

    /**
     * The main() method is needed to launch the JavaFX app from the command line or IDE.
     * @param args any command-line arguments (not used here)
     */
    public static void main(String[] args) {
        // This launches the JavaFX application, which will then call start()
        launch(args);
    }
}
