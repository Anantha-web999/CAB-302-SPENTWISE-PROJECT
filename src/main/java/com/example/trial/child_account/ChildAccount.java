package com.example.trial.child_account;

public class ChildAccount {
    private int id;
    private String accountName;
    private double budget;
    private double balance;
    private String bankName;
    private String accountNumber;
    private String bsb;
    private String accountType;
    private String userEmail;

    // Full constructor (used for adding full account)
    public ChildAccount(int id, String accountName, double budget, String bankName,
                        String accountNumber, String bsb, String accountType, String userEmail) {
        this.id = id;
        this.accountName = accountName;
        this.budget = budget;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.bsb = bsb;
        this.accountType = accountType;
        this.userEmail = userEmail;
    }

    // Display constructor (used in TableView)
    public ChildAccount(int id, String accountName, double budget, double balance, String bankName) {
        this.id = id;
        this.accountName = accountName;
        this.budget = budget;
        this.balance = balance;
        this.bankName = bankName;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
