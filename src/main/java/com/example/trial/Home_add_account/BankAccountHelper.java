package com.example.trial.Home_add_account;

import com.example.trial.DB.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";


    public static boolean isTableExist() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='bank_accounts';";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    public static void addBankAccount(int userId, String bankName, String accountName,
                                      String accountNumber, String bsb,
                                      String accountType) throws SQLException {
        String sql = "INSERT INTO bank_accounts(user_id, bank_name, account_name, account_number, bsb, account_type, balance) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, bankName);
            pstmt.setString(3, accountName);
            pstmt.setString(4, accountNumber);
            pstmt.setString(5, bsb);
            pstmt.setString(6, accountType);
            pstmt.setDouble(7, 0.0);
            pstmt.executeUpdate();
        }
    }


    public static List<BankAccount> getAllBankAccounts(String email) throws SQLException {
        if (!isTableExist()) {
            DatabaseHelper.initializeDatabase();
        }

        List<BankAccount> accounts = new ArrayList<>();
        int userId = getUserIdByEmail(email);
        String sql = "SELECT * FROM bank_accounts WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Set the userId here

            try (ResultSet rs = pstmt.executeQuery()) {
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
        }

        return accounts;
    }


    public static int getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:spentwise.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("User not found for email: " + email);
            }
        }
    }

}
