package com.example.trial.Child_account;

import java.sql.*;

public class ChildBankAccountHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static void addChildAccount(String bankName, String accountName,
                                       String accountNumber, String bsb,
                                       String accountType) throws SQLException {
        String sql = "INSERT INTO child_accounts(bank_name, account_name, account_number, bsb, account_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bankName);
            pstmt.setString(2, accountName);
            pstmt.setString(3, accountNumber);
            pstmt.setString(4, bsb);
            pstmt.setString(5, accountType);
            pstmt.executeUpdate();
        }
    }
}
