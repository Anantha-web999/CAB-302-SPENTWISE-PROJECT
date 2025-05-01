module com.example.trial {
    requires java.desktop;
    requires javafx.fxml;
    requires java.prefs;
    requires java.sql;
    requires javafx.swing;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;

    opens com.example.trial to javafx.fxml;
    opens com.example.trial.Home_add_account to javafx.fxml, javafx.graphics;

    exports com.example.trial;
    exports com.example.trial.Home_add_account;
}
