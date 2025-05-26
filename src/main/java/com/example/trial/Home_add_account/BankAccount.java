package com.example.trial.Home_add_account;

import java.text.NumberFormat;
import java.util.Locale;

public class BankAccount {
    private final int id;
    private final String bankName;
    private final String accountName;
    private final String accountNumber;
    private final String bsb;
    private final String accountType;
    private double balance;

    public BankAccount(int id, String bankName, String accountName, String accountNumber, String bsb, String accountType, double balance) {
        this.id = id;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.bsb = bsb;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBsb() {
        return bsb;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFormattedBalance() {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(balance);
    }
}
