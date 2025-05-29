package LOGINsignupLANDING;

import com.example.trial.logincontroller.LoginController;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController controller;
    private PasswordField pwField;
    private TextField visibleField;
    private Button toggleBtn;

    @BeforeAll
    static void initToolkit() {
        // initialize JavaFX toolkit
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new LoginController();

        // create the two fields and the toggle button
        pwField       = new PasswordField();
        visibleField  = new TextField();
        toggleBtn     = new Button("Show");

        // **IMPORTANT**: set the initial state exactly as your UI should start
        pwField.setVisible(true);
        pwField.setManaged(true);
        visibleField.setVisible(false);
        visibleField.setManaged(false);
        toggleBtn.setText("Show");

        // inject into the private fields of the controller
        Field fPw     = LoginController.class.getDeclaredField("passwordField");
        Field fVis    = LoginController.class.getDeclaredField("visiblePasswordField");
        Field fToggle = LoginController.class.getDeclaredField("togglePasswordButton");
        fPw.setAccessible(true);
        fVis.setAccessible(true);
        fToggle.setAccessible(true);
        fPw.set(controller, pwField);
        fVis.set(controller, visibleField);
        fToggle.set(controller, toggleBtn);
    }

    @Test
    void testTogglePasswordVisibility() throws Exception {
        // initial assertions
        assertTrue(pwField.isVisible(),    "PasswordField should start visible");
        assertFalse(visibleField.isVisible(),"Visible TextField should start hidden");
        assertEquals("Show", toggleBtn.getText());

        // grab the private toggle method
        Method toggle = LoginController.class
                .getDeclaredMethod("togglePasswordVisibility", ActionEvent.class);
        toggle.setAccessible(true);

        // first toggle → should hide pwField, show visibleField, label becomes "Hide"
        pwField.setText("secret");
        toggle.invoke(controller, (ActionEvent) null);
        assertFalse(pwField.isVisible());
        assertTrue(visibleField.isVisible());
        assertEquals("Hide", toggleBtn.getText());

        // second toggle → back to masked pwField and "Show"
        toggle.invoke(controller, (ActionEvent) null);
        assertTrue(pwField.isVisible());
        assertFalse(visibleField.isVisible());
        assertEquals("Show", toggleBtn.getText());
    }
}
