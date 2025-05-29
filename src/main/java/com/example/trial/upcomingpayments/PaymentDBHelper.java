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

    // Fetch all payments for a user
    public static List<payment> getAllPaymentsForUser(int userId) {
        List<payment> payments = new ArrayList<>();
        String sql = "SELECT name, amount, due_date, paid FROM upcoming_payments WHERE user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double amount = rs.getDouble("amount");
                LocalDate dueDate = LocalDate.parse(rs.getString("due_date"));
                boolean paid = rs.getBoolean("paid");
                payment pay = new payment(name, amount, dueDate);
                pay.setPaid(paid);
                payments.add(pay);
            }
        } catch (SQLException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
        return payments;
    }

    // Delete a payment by name, amount, date, and user (to ensure correct record)
    public static void deletePayment(String name, double amount, LocalDate dueDate, int userId) {
        String sql = "DELETE FROM upcoming_payments WHERE name = ? AND amount = ? AND due_date = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, dueDate.toString());
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
        }
    }

}
