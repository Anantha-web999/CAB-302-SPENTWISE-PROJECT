package com.example.trial.child_account;

import com.example.trial.DB.DatabaseHelper;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ChildBankAccountHelperTest {

    private int testUserId;

    @BeforeEach
    void setup() {
        // Initialize the DB schema
        DatabaseHelper.initializeDatabase();

        try (Connection conn = DatabaseHelper.getConnection()) {
            // Insert a dummy user for foreign key constraint
            String insertUser = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, "Test User");
                stmt.setString(2, "test@example.com");
                stmt.setString(3, "password123");
                stmt.executeUpdate();

                var keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    testUserId = keys.getInt(1);
                }
            }

            // Optionally insert a test child account
            String insertChildAccount = "INSERT INTO child_accounts " +
                    "(bank_name, account_name, account_number, bsb, account_type, budget, balance, user_id, details) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertChildAccount)) {
                stmt.setString(1, "Test Bank");
                stmt.setString(2, "Child Saver");
                stmt.setString(3, "123456789");
                stmt.setString(4, "012-345");
                stmt.setString(5, "Savings");
                stmt.setDouble(6, 100.0);
                stmt.setDouble(7, 50.0);
                stmt.setInt(8, testUserId);
                stmt.setString(9, "Test details");
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Setup error: " + e.getMessage());
        }
    }

    @AfterEach
    void cleanup() {
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM child_accounts");
            stmt.execute("DELETE FROM users");

        } catch (SQLException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    @Test
    void testDummyCheck() {
        Assertions.assertTrue(true, "Dummy test to check setup works.");
    }

    // Add your actual test methods here...

}
