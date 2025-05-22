package com.example.trial.Home_add_account;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionHelper {
    private static final String DB_URL = "jdbc:sqlite:spentwise.db";

    public static List<Transaction> getRecentTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY date DESC LIMIT 10";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String dateString = rs.getString("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                java.util.Date parsedDate;
                try {
                    parsedDate = dateFormat.parse(dateString);
                } catch (java.text.ParseException e) {
                    System.err.println("Date format error: " + e.getMessage());
                    continue;
                }

                String formattedDate = dateFormat.format(parsedDate);
                transactions.add(new Transaction(
                        formattedDate,
                        rs.getString("description"),
                        rs.getDouble("amount")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return transactions;
    }
}
