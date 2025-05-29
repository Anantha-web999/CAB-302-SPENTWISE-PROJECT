module com.example.trial {
    requires java.desktop;
    requires javafx.fxml;
    requires java.prefs;
    requires java.sql;
    requires javafx.swing;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;
    requires org.json;

    opens com.example.trial to javafx.fxml;
    opens com.example.trial.Home_add_account to javafx.fxml, javafx.graphics;
    opens com.example.trial.logincontroller to javafx.fxml;
    opens com.example.trial.child_account to javafx.fxml;
    opens com.example.trial.insights_AI to javafx.fxml;
    opens com.example.trial.upcomingpayments to javafx.fxml;


    exports com.example.trial;
    exports com.example.trial.Home_add_account;
    exports com.example.trial.upcomingpayments;
    exports com.example.trial.child_account;
    exports com.example.trial.settings;
    exports com.example.trial.insights_AI;
}
