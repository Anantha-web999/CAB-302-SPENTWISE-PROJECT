package com.example.trial.Child_account;

import com.example.trial.DB.DatabaseHelper;

import java.sql.*;

public class ChildBankAccountHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static void addChildAccount(String bankName, String accountName,
                                       String accountNumber, String bsb,
                                       String accountType, String userEmail) throws SQLException {
        int userId = getUserIdByEmail(userEmail);

        String sql = "INSERT INTO child_accounts(user_id, bank_name, account_name, account_number, bsb, account_type) " +
                "VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, bankName);
            pstmt.setString(3, accountName);
            pstmt.setString(4, accountNumber);
            pstmt.setString(5, bsb);
            pstmt.setString(6, accountType);
            pstmt.executeUpdate();
        }
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
}
