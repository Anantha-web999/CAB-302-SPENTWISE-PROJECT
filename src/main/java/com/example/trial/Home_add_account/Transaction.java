package com.example.trial.Home_add_account;

public class Transaction {
    private final String date;
    private final String description;
    private final double amount;

    public Transaction(String date, String description, double amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}
