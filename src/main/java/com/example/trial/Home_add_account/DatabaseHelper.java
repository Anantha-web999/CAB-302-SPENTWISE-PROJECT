package com.example.trial.Home_add_account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS bank_accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bank_name TEXT NOT NULL," +
                "account_name TEXT NOT NULL," +
                "account_number TEXT NOT NULL," +
                "bsb TEXT NOT NULL," +
                "account_type TEXT NOT NULL," +  // <-- COMMA HERE
                "balance REAL DEFAULT 0.0" +
                ");";


        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

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
            pstmt.setDouble(6, 0.0);
            pstmt.executeUpdate();
        }
    }

    public static List<com.example.trial.Home_add_account.BankAccount> getAllBankAccounts() throws SQLException {
        List<com.example.trial.Home_add_account.BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM bank_accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                com.example.trial.Home_add_account.BankAccount account = new com.example.trial.Home_add_account.BankAccount(
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
}
