package LOGINsignupLANDING;

import com.example.trial.logincontroller.LandingController;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class LandingControllerTest {

    @Test
    void navigateToLogin_methodExists() throws NoSuchMethodException {
        // This will fail at compile time if LandingController isn't visible
        Method m = LandingController.class.getDeclaredMethod("navigateToLogin", ActionEvent.class);
        assertNotNull(m, "LandingController should define navigateToLogin(ActionEvent)");
    }
}
