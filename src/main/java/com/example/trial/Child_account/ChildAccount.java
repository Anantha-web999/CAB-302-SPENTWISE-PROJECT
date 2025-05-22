package com.example.trial.Child_account;

public class ChildAccount {
    private String accountName;
    private double budget;
    private String details;

    public ChildAccount(String accountName, double budget, String details) {
        this.accountName = accountName;
        this.budget = budget;
        this.details = details;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBudget() {
        return budget;
    }

    public String getDetails() {
        return details;
    }
}
