package com.example.trial;

public class Session {
    private static String currentUserEmail;

    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static void clearSession() {
        currentUserEmail = null;
    }
}