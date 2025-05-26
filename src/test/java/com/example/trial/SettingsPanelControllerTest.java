package com.example.trial;

import com.example.trial.settings.SettingsPanelController;
import com.example.trial.settings.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsPanelControllerTest {

    private SettingsPanelController controller;

    @BeforeEach
    void setUp() {
        controller = new SettingsPanelController();
    }

    @Test
    void testControllerInstantiation() {
        SettingsPanelController controller = new SettingsPanelController();
        assertNotNull(controller);
    }

    @Test
    void testGetMonthAbbreviationValidInputs() throws Exception {
        //Use reflection to test private method
        var method = SettingsPanelController.class.getDeclaredMethod("getMonthAbbreviation", int.class);
        method.setAccessible(true);

        //Test valid months
        assertEquals("Jan", method.invoke(controller, 1));
        assertEquals("Feb", method.invoke(controller, 2));
        assertEquals("Dec", method.invoke(controller, 12));
    }

    @Test
    void testGetMonthAbbreviationInvalidInputs() throws Exception {
        //Use reflection to test private method
        var method = SettingsPanelController.class.getDeclaredMethod("getMonthAbbreviation", int.class);
        method.setAccessible(true);

        //Test invalid inputs return default "Jan"
        assertEquals("Jan", method.invoke(controller, 0));
        assertEquals("Jan", method.invoke(controller, 13));
        assertEquals("Jan", method.invoke(controller, -1));
    }


    @Test
    void testUserAccountSettersGetters() {
        //Test UserAccount basic functionality
        UserAccount account = new UserAccount();

        account.setFullName("Test User");
        account.setEmail("test@example.com");

        assertEquals("Test User", account.getFullName());
        assertEquals("test@example.com", account.getEmail());
    }
}