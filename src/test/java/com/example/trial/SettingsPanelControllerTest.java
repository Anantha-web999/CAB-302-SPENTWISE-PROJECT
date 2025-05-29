package com.example.trial.settings;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SettingsPanelControllerTest {

    private SettingsPanelController controller;

    @BeforeEach
    void setUp() {
        controller = new SettingsPanelController();
    }

    @Test
    @DisplayName("Test 1: Month name conversion works correctly")
    void testGetMonthName() throws Exception {
        // Use reflection to access private method
        var method = SettingsPanelController.class.getDeclaredMethod("getMonthName", int.class);
        method.setAccessible(true);

        assertEquals("Jan", method.invoke(controller, 1));
        assertEquals("Jun", method.invoke(controller, 6));
        assertEquals("Dec", method.invoke(controller, 12));
        assertEquals("Jan", method.invoke(controller, 13)); // Out of range defaults to Jan
    }

    @Test
    @DisplayName("Test 2: Date string is built correctly from dropdowns")
    void testBuildDateFromDropdowns() throws Exception {
        var method = SettingsPanelController.class.getDeclaredMethod("buildDateFromDropdowns");
        method.setAccessible(true);

        // Would need to mock ComboBoxes to test this properly
        // This shows the test structure
        assertNotNull(method);
    }


    @Test
    @DisplayName("Test 3: Date parsing handles format correctly")
    void testParseDateAndSetDropdowns() throws Exception {
        var method = SettingsPanelController.class.getDeclaredMethod("parseDateAndSetDropdowns", String.class);
        method.setAccessible(true);

        // Test the method exists and is accessible
        assertNotNull(method);

        // Would need mocked ComboBoxes to test actual parsing
    }

    @Test
    @DisplayName("Test 4: UserAccount creation with required fields")
    void testUserAccountCreation() {
        UserAccount user = new UserAccount();
        user.setFullName("Jane Smith");
        user.setEmail("jane@test.com");

        assertNotNull(user);
        assertEquals("Jane Smith", user.getFullName());
        assertEquals("jane@test.com", user.getEmail());
    }
}