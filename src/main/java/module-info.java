module com.example.trial {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.prefs;
    opens com.example.trial to javafx.fxml;
    exports com.example.trial;
}