package com.example.trial.child_account;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChildBankAccountHelperTest {

    private final String testEmail = "test@example.com";

    @BeforeEach
    void setup() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db")) {
            // Insert test user with required fields
            String insertUser = "INSERT OR IGNORE INTO users (full_name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertUser)) {
                stmt.setString(1, "Test User");
                stmt.setString(2, testEmail);
                stmt.setString(3, "testpass123");
                stmt.executeUpdate();
            }

            // Insert one child account for testing
            int userId = ChildBankAccountHelper.getUserIdByEmail(testEmail);
            String insertAccount = "INSERT INTO child_accounts (bank_name, account_name, account_number, bsb, account_type, budget, balance, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertAccount)) {
                stmt.setString(1, "Test Bank");
                stmt.setString(2, "Test Account");
                stmt.setString(3, "123456");
                stmt.setString(4, "000000");
                stmt.setString(5, "Savings");
                stmt.setDouble(6, 50.0);
                stmt.setDouble(7, 0.0);
                stmt.setInt(8, userId);
                stmt.executeUpdate();
            }
        }
    }


    @Test
    void testGetAllChildAccounts() throws SQLException {
        List<ChildAccount> accounts = ChildBankAccountHelper.getAllChildAccounts(testEmail);
        assertFalse(accounts.isEmpty());
        assertEquals("Test Bank", accounts.get(0).getBankName());
    }

    @Test
    void testUpdateBudget() throws SQLException {
        List<ChildAccount> accounts = ChildBankAccountHelper.getAllChildAccounts(testEmail);
        ChildAccount acc = accounts.get(0);
        double newBudget = 100.0;

        ChildBankAccountHelper.updateBudget(acc.getId(), newBudget);

        List<ChildAccount> updated = ChildBankAccountHelper.getAllChildAccounts(testEmail);
        assertEquals(newBudget, updated.get(0).getBudget());
    }

    @Test
    void testDeleteChildAccount() throws SQLException {
        List<ChildAccount> accounts = ChildBankAccountHelper.getAllChildAccounts(testEmail);
        int idToDelete = accounts.get(0).getId();

        ChildBankAccountHelper.deleteChildAccount(idToDelete);

        List<ChildAccount> remaining = ChildBankAccountHelper.getAllChildAccounts(testEmail);
        boolean stillExists = remaining.stream().anyMatch(a -> a.getId() == idToDelete);

        assertFalse(stillExists);
    }


    @AfterEach
    void cleanup() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db")) {
            // Delete the test account
            conn.createStatement().executeUpdate("DELETE FROM child_accounts");

            // Delete the test user
            conn.createStatement().executeUpdate("DELETE FROM users WHERE email = 'test@example.com'");
        }
    }

}
