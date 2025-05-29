package com.example.trial.upcomingpayments;

import com.example.trial.DB.DatabaseHelper;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDBHelper {

    // Get user ID by email (if you want, later move this to a UserDBHelper)
    public static int getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
        }
        return -1; // Not found
    }

    // Insert payment with user ID
    public static void insertPayment(String name, double amount, LocalDate dueDate, boolean paid, int userId) {
        String sql = "INSERT INTO upcoming_payments (name, amount, due_date, paid, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, dueDate.toString());
            pstmt.setBoolean(4, paid);
            pstmt.setInt(5, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting payment: " + e.getMessage());
        }
    }

}