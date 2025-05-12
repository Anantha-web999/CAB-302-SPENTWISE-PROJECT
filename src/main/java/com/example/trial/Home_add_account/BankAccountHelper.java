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

    public static void addBankAccount(String bankName, String accountName,
                                      String accountNumber, String bsb,
                                      String accountType) throws SQLException {
        String sql = "INSERT INTO bank_accounts(bank_name, account_name, account_number, bsb, account_type, balance) VALUES(?,?,?,?,?,?)";

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

    public static List<BankAccount> getAllBankAccounts() throws SQLException {
        if (!isTableExist()) {
            DatabaseHelper.initializeDatabase();
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
}
