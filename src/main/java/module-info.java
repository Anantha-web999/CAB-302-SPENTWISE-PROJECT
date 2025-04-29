module com.example.cab302spentwiseproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cab302spentwiseproject to javafx.fxml;
    exports com.example.cab302spentwiseproject;
}