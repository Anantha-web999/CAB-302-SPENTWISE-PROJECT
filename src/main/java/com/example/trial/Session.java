package com.example.trial;

public class Session {
    private static Session instance;
    private String currentUserEmail;

    // Private constructor prevents instantiation from outside
    private Session() {}

    // Public method to provide access to the single instance
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Methods to manage session data
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void clearSession() {
        currentUserEmail = null;
    }
}
