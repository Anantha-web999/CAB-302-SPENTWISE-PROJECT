package com.example.trial.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create users table
            String usersSql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "full_name TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL);";
            stmt.execute(usersSql);

            // Create bank_accounts table
            String bankAccountsSql = "CREATE TABLE IF NOT EXISTS bank_accounts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bank_name TEXT NOT NULL, " +
                    "account_name TEXT NOT NULL, " +
                    "account_number TEXT NOT NULL, " +
                    "bsb TEXT NOT NULL, " +
                    "account_type TEXT NOT NULL, " +
                    "balance REAL DEFAULT 0.0, " +
                    "user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);";
            stmt.execute(bankAccountsSql);

            // Create transactions table
            String transactionsSql = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account_id INTEGER NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "description TEXT, " +
                    "date TEXT NOT NULL, " +
                    "FOREIGN KEY(account_id) REFERENCES bank_accounts(id));";
            stmt.execute(transactionsSql);

            // Create child_accounts table
            String childAccountsSql = "CREATE TABLE IF NOT EXISTS child_accounts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bank_name TEXT NOT NULL, " +
                    "account_name TEXT NOT NULL, " +
                    "account_number TEXT NOT NULL, " +
                    "bsb TEXT NOT NULL, " +
                    "account_type TEXT NOT NULL, " +
                    "budget REAL DEFAULT 0.0, " +
                    "balance REAL DEFAULT 0.0, " +
                    "user_id INTEGER NOT NULL, " +
                    "details TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ");";

            stmt.execute(childAccountsSql);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
