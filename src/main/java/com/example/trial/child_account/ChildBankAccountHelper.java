package com.example.trial.child_account;

import com.example.trial.DB.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChildBankAccountHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static boolean isTableExist() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='child_accounts';";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking child_accounts table existence: " + e.getMessage());
            return false;
        }
    }

    public static void addChildAccount(String accountName, double budget,
                                       String bankName, String accountNumber,
                                       String bsb, String accountType,
                                       String userEmail) throws SQLException {
        int userId = getUserIdByEmail(userEmail);

        String sql = "INSERT INTO child_accounts (bank_name, account_name, account_number, bsb, account_type, budget, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bankName);
            pstmt.setString(2, accountName);
            pstmt.setString(3, accountNumber);
            pstmt.setString(4, bsb);
            pstmt.setString(5, accountType);
            pstmt.setDouble(6, budget);
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();
        }
    }

    public static List<ChildAccount> getAllChildAccounts(String email) throws SQLException {
        if (!isTableExist()) {
            DatabaseHelper.initializeDatabase();
        }

        List<ChildAccount> accounts = new ArrayList<>();
        int userId = getUserIdByEmail(email);

        String sql = "SELECT id, account_name, budget, balance, bank_name FROM child_accounts WHERE user_id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                double balance = rs.getObject("balance") != null ? rs.getDouble("balance") : 0.0;
                ChildAccount account = new ChildAccount(
                        rs.getInt("id"),
                        rs.getString("account_name"),
                        rs.getDouble("budget"),
                        balance,
                        rs.getString("bank_name")
                );
                accounts.add(account);
            }
        }

        return accounts;
    }

    public static int getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseHelper.getConnection();
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

    public static void updateBudget(int childId, double newBudget) throws SQLException {
        String sql = "UPDATE child_accounts SET budget = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBudget);
            pstmt.setInt(2, childId);
            pstmt.executeUpdate();
        }
    }

    public static void deleteChildAccount(int childId) throws SQLException {
        String sql = "DELETE FROM child_accounts WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, childId);
            pstmt.executeUpdate();
        }
    }
}
