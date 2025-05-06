package com.example.trial.Home_add_account;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db"; // Ensure this path is correct

    // Method to initialize the database (create table if not exists)
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS bank_accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bank_name TEXT NOT NULL, " +
                "account_name TEXT NOT NULL, " +
                "account_number TEXT NOT NULL, " +
                "bsb TEXT NOT NULL, " +
                "account_type TEXT NOT NULL, " +
                "balance REAL DEFAULT 0.0 " +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    // Method to check if the table exists
    public static boolean isTableExist() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='bank_accounts';";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next(); // True if table exists
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    // Method to add a new bank account to the table
    public static void addBankAccount(String bankName, String accountName,
                                      String accountNumber, String bsb,
                                      String accountType) throws SQLException {
        String sql = "INSERT INTO bank_accounts(bank_name, account_name, account_number, bsb, account_type, balance) " +
                "VALUES(?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bankName);
            pstmt.setString(2, accountName);
            pstmt.setString(3, accountNumber);
            pstmt.setString(4, bsb);
            pstmt.setString(5, accountType);
            pstmt.setDouble(6, 0.0);  // Default balance value
            pstmt.executeUpdate();
        }
    }

    // Method to fetch all bank accounts from the table
    public static List<BankAccount> getAllBankAccounts() throws SQLException {
        // Check if the table exists and initialize if not
        if (!isTableExist()) {
            initializeDatabase(); // Create the table
        }

        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM bank_accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BankAccount account = new BankAccount(
                        rs.getInt("id"),
                        rs.getString("bank_name"),
                        rs.getString("account_name"),
                        rs.getString("account_number"),
                        rs.getString("bsb"),
                        rs.getString("account_type"),
                        rs.getDouble("balance")
                );
                accounts.add(account);
            }
        }

        return accounts;
    }

    // Call this in your app startup or controller init
    public static void initializeTransactionsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_id INTEGER NOT NULL, " +
                "amount REAL NOT NULL, " +
                "description TEXT, " +
                "date TEXT NOT NULL, " +
                "FOREIGN KEY(account_id) REFERENCES bank_accounts(id)" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating transactions table: " + e.getMessage());
        }
    }


    public static List<Transaction> getRecentTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY date DESC LIMIT 10";  // Example query

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Retrieve the date as a string and then parse it
                String dateString = rs.getString("date");

                // Parse the date string into a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = null;

                // Handle ParseException
                try {
                    parsedDate = dateFormat.parse(dateString);
                } catch (java.text.ParseException e) {
                    System.err.println("Date format error: " + e.getMessage());
                    continue;  // Skip this transaction if date parsing fails
                }

                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");

                // Format the date to your desired format
                String formattedDate = dateFormat.format(sqlDate);

                transactions.add(new Transaction(formattedDate, description, amount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Re-throwing the SQLException for the caller to handle
        }

        return transactions;
    }
}
