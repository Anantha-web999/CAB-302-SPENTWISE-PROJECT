package com.example.trial.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseHelper handles creating the SQLite database file,
 * initializing its tables, and providing connections for queries.
 */
public class DatabaseHelper {
    // URL to the local SQLite file—will be created if it doesn’t exist
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    /**
     * Initializes the database schema on first run.
     * Creates tables for users, bank accounts, transactions, and child accounts.
     */
    public static void initializeDatabase() {
        // Try-with-resources ensures the connection and statement are closed automatically
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Enforce foreign key constraints in SQLite
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create 'users' table if it doesn't already exist
            String usersSql = ""
                    + "CREATE TABLE IF NOT EXISTS users ("
                    +   "id INTEGER PRIMARY KEY AUTOINCREMENT, "      // Unique user ID
                    +   "full_name TEXT NOT NULL, "                   // User’s full name
                    +   "email TEXT NOT NULL UNIQUE, "                // User’s email address (must be unique)
                    +   "password TEXT NOT NULL"                      // Hashed password
                    + ");";
            stmt.execute(usersSql);

            // Create 'bank_accounts' table to store each user’s bank accounts
            String bankAccountsSql = ""
                    + "CREATE TABLE IF NOT EXISTS bank_accounts ("
                    +   "id INTEGER PRIMARY KEY AUTOINCREMENT, "     // Unique account ID
                    +   "bank_name TEXT NOT NULL, "                  // Name of the bank
                    +   "account_name TEXT NOT NULL, "               // User-defined name for this account
                    +   "account_number TEXT NOT NULL, "             // Bank account number
                    +   "bsb TEXT NOT NULL, "                        // Bank-state-branch code
                    +   "account_type TEXT NOT NULL, "               // Type (e.g., Checking, Savings)
                    +   "balance REAL DEFAULT 0.0"                   // Current balance, defaults to zero
                    + ");";
            stmt.execute(bankAccountsSql);

            // Create 'transactions' table linked to bank_accounts via a foreign key
            String transactionsSql = ""
                    + "CREATE TABLE IF NOT EXISTS transactions ("
                    +   "id INTEGER PRIMARY KEY AUTOINCREMENT, "     // Unique transaction ID
                    +   "account_id INTEGER NOT NULL, "              // References bank_accounts.id
                    +   "amount REAL NOT NULL, "                     // Transaction amount
                    +   "description TEXT, "                         // Optional description or note
                    +   "date TEXT NOT NULL, "                       // Date of transaction
                    +   "FOREIGN KEY(account_id) REFERENCES bank_accounts(id)"
                    + ");";
            stmt.execute(transactionsSql);

            // Create 'child_accounts' table if needed for sub-accounts or external accounts
            String childAccountsSql = ""
                    + "CREATE TABLE IF NOT EXISTS child_accounts ("
                    +   "id INTEGER PRIMARY KEY AUTOINCREMENT, "     // Unique child account ID
                    +   "bank_name TEXT NOT NULL, "                  // Bank name
                    +   "account_name TEXT NOT NULL, "               // User-defined name
                    +   "account_number TEXT NOT NULL, "             // Account number
                    +   "bsb TEXT NOT NULL, "                        // Branch code
                    +   "account_type TEXT NOT NULL"                 // Type of account
                    + ");";
            stmt.execute(childAccountsSql);

        } catch (SQLException e) {
            // Print any errors that occur during initialization
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Returns a new Connection to the SQLite database.
     * Caller is responsible for closing the connection when done.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
