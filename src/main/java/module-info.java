module com.example.trial {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.prefs;
    requires java.sql;

    opens com.example.trial to javafx.fxml;
    opens com.example.trial.Home_add_account to javafx.fxml, javafx.graphics;

    exports com.example.trial;
    exports com.example.trial.Home_add_account;
}
