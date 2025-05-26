package com.example.trial.settings;

/**
 * User account data model
 */
public class UserAccount {
    private String fullName;
    private String dateOfBirth;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String preferredCurrency;

    //Default constructor
    public UserAccount() {
        this.preferredCurrency = "USD ($)";
    }

    //Constructor with all fields
    public UserAccount(String fullName, String dateOfBirth, String username, String email,
                       String phone, String address,
                       String preferredCurrency) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.preferredCurrency = preferredCurrency;
    }

    //Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }
}