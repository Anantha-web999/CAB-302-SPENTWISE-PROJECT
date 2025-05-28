package com.example.trial.logincontroller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LandingControllerTest {

    @Test
    void testNavigateToLoginMethodExists() {
        try {
            assertNotNull(
                    LandingController.class.getDeclaredMethod("navigateToLogin", javafx.event.ActionEvent.class)
            );
        } catch (NoSuchMethodException e) {
            fail("Method navigateToLogin not found in LandingController");
        }
    }
}
