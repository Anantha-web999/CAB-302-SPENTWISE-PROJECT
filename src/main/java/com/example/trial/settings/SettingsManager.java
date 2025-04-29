package com.example.trial.settings;
import java.util.prefs.Preferences;

/**
 * Manages settings and preferences for the SpentWise application.
 * Provides methods to save, retrieve, and reset user preferences.
 */
public class SettingsManager {
    //Preferences storage for persistent settings
    private static final Preferences prefs = Preferences.userNodeForPackage(SettingsManager.class);

    //Account settings keys
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_DOB = "dateOfBirth";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ACCOUNT_CREATED = "accountCreated";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_TWO_FACTOR = "twoFactorAuth";

    //App preferences keys
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_FONT_SIZE = "fontSize";
    private static final String KEY_THEME = "theme";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_DATE_FORMAT = "dateFormat";
    private static final String KEY_TIME_FORMAT = "timeFormat";
    private static final String KEY_STARTUP = "startOnBoot";
    private static final String KEY_START_MINIMIZED = "startMinimized";
    private static final String KEY_AUTO_BACKUP = "autoBackup";
    private static final String KEY_DEFAULT_VIEW = "defaultView";
    private static final String KEY_CHART_TYPE = "chartType";
    private static final String KEY_ANIMATIONS = "animationsLevel";

    //Notification settings keys
    private static final String KEY_NOTIFY_BILLS = "notifyBills";
    private static final String KEY_NOTIFY_SUBSCRIPTIONS = "notifySubscriptions";
    private static final String KEY_NOTIFY_WEEKLY = "notifyWeekly";
    private static final String KEY_NOTIFY_OVERSPEND = "notifyOverspend";
    private static final String KEY_NOTIFY_MOTIVATION = "notifyMotivation";
    private static final String KEY_NOTIFY_FREQUENCY = "notifyFrequency";
    private static final String KEY_NOTIFY_TIME = "notifyTime";
    private static final String KEY_NOTIFY_EMAIL = "notifyEmail";
    private static final String KEY_NOTIFY_DESKTOP = "notifyDesktop";
    private static final String KEY_NOTIFY_PUSH = "notifyPush";
    private static final String KEY_QUIET_HOURS = "quietHours";
    private static final String KEY_QUIET_FROM = "quietFrom";
    private static final String KEY_QUIET_TO = "quietTo";

    /**
     * Saves user account settings to persistent storage.
     */
    public static void saveAccountSettings(String fullName, String dob, String username,
                                           String email, String phone, String address,
                                           String currency, boolean twoFactor) {
        prefs.put(KEY_FULLNAME, fullName);
        prefs.put(KEY_DOB, dob);
        prefs.put(KEY_USERNAME, username);
        prefs.put(KEY_EMAIL, email);
        prefs.put(KEY_PHONE, phone);
        prefs.put(KEY_ADDRESS, address);
        prefs.put(KEY_CURRENCY, currency);
        prefs.putBoolean(KEY_TWO_FACTOR, twoFactor);
    }

    /**
     * Retrieves the user's full name.
     */
    public static String getFullName() {
        return prefs.get(KEY_FULLNAME, "");
    }

    /**
     * Retrieves the user's date of birth.
     */
    public static String getDateOfBirth() {
        return prefs.get(KEY_DOB, "");
    }

    /**
     * Retrieves the user's username.
     */
    public static String getUsername() {
        return prefs.get(KEY_USERNAME, "");
    }

    /**
     * Retrieves the user's email address.
     */
    public static String getEmail() {
        return prefs.get(KEY_EMAIL, "");
    }

    /**
     * Retrieves the user's phone number.
     */
    public static String getPhone() {
        return prefs.get(KEY_PHONE, "");
    }

    /**
     * Retrieves the user's address.
     */
    public static String getAddress() {
        return prefs.get(KEY_ADDRESS, "");
    }

    /**
     * Retrieves the date when the account was created.
     */
    public static String getAccountCreated() {
        return prefs.get(KEY_ACCOUNT_CREATED, "");
    }

    /**
     * Retrieves the user's preferred currency.
     */
    public static String getCurrency() {
        return prefs.get(KEY_CURRENCY, "USD ($)");
    }

    /**
     * Checks if two-factor authentication is enabled.
     */
    public static boolean getTwoFactorAuth() {
        return prefs.getBoolean(KEY_TWO_FACTOR, false);
    }

    /**
     * Saves application preferences to persistent storage.
     */
    public static void saveAppPreferences(boolean darkMode, String fontSize, String language,
                                          String dateFormat, String timeFormat, boolean startOnBoot,
                                          boolean startMinimized, boolean autoBackup,
                                          String defaultView, String chartType, int animations) {
        prefs.putBoolean(KEY_DARK_MODE, darkMode);
        prefs.put(KEY_FONT_SIZE, fontSize);
        prefs.put(KEY_LANGUAGE, language);
        prefs.put(KEY_DATE_FORMAT, dateFormat);
        prefs.put(KEY_TIME_FORMAT, timeFormat);
        prefs.putBoolean(KEY_STARTUP, startOnBoot);
        prefs.putBoolean(KEY_START_MINIMIZED, startMinimized);
        prefs.putBoolean(KEY_AUTO_BACKUP, autoBackup);
        prefs.put(KEY_DEFAULT_VIEW, defaultView);
        prefs.put(KEY_CHART_TYPE, chartType);
        prefs.putInt(KEY_ANIMATIONS, animations);
    }

    /**
     * Checks if dark mode is enabled.
     */
    public static boolean getDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    /**
     * Retrieves the preferred font size.
     */
    public static String getFontSize() {
        return prefs.get(KEY_FONT_SIZE, "Medium");
    }

    /**
     * Retrieves the preferred interface language.
     */
    public static String getLanguage() {
        return prefs.get(KEY_LANGUAGE, "English");
    }

    /**
     * Retrieves the preferred date format.
     */
    public static String getDateFormat() {
        return prefs.get(KEY_DATE_FORMAT, "MM/DD/YYYY");
    }

    /**
     * Retrieves the preferred time format.
     */
    public static String getTimeFormat() {
        return prefs.get(KEY_TIME_FORMAT, "12-hour (AM/PM)");
    }

    /**
     * Checks if application should start on system boot.
     */
    public static boolean getStartOnBoot() {
        return prefs.getBoolean(KEY_STARTUP, false);
    }

    /**
     * Checks if application should start minimized.
     */
    public static boolean getStartMinimized() {
        return prefs.getBoolean(KEY_START_MINIMIZED, false);
    }

    /**
     * Checks if automatic data backup is enabled.
     */
    public static boolean getAutoBackup() {
        return prefs.getBoolean(KEY_AUTO_BACKUP, true);
    }

    /**
     * Retrieves the preferred default budget view.
     */
    public static String getDefaultView() {
        return prefs.get(KEY_DEFAULT_VIEW, "Monthly");
    }

    /**
     * Retrieves the preferred chart type.
     */
    public static String getChartType() {
        return prefs.get(KEY_CHART_TYPE, "Pie Chart");
    }

    /**
     * Retrieves the animations intensity level.
     */
    public static int getAnimationsLevel() {
        return prefs.getInt(KEY_ANIMATIONS, 50);
    }

    /**
     * Saves notification settings to persistent storage.
     */
    public static void saveNotificationSettings(boolean notifyBills, boolean notifySubscriptions,
                                                boolean notifyWeekly, boolean notifyOverspend,
                                                boolean notifyMotivation, String frequency,
                                                String notifyTime, boolean notifyEmail,
                                                boolean notifyDesktop, boolean notifyPush,
                                                boolean quietHours, String quietFrom, String quietTo) {
        prefs.putBoolean(KEY_NOTIFY_BILLS, notifyBills);
        prefs.putBoolean(KEY_NOTIFY_SUBSCRIPTIONS, notifySubscriptions);
        prefs.putBoolean(KEY_NOTIFY_WEEKLY, notifyWeekly);
        prefs.putBoolean(KEY_NOTIFY_OVERSPEND, notifyOverspend);
        prefs.putBoolean(KEY_NOTIFY_MOTIVATION, notifyMotivation);
        prefs.put(KEY_NOTIFY_FREQUENCY, frequency);
        prefs.put(KEY_NOTIFY_TIME, notifyTime);
        prefs.putBoolean(KEY_NOTIFY_EMAIL, notifyEmail);
        prefs.putBoolean(KEY_NOTIFY_DESKTOP, notifyDesktop);
        prefs.putBoolean(KEY_NOTIFY_PUSH, notifyPush);
        prefs.putBoolean(KEY_QUIET_HOURS, quietHours);
        prefs.put(KEY_QUIET_FROM, quietFrom);
        prefs.put(KEY_QUIET_TO, quietTo);
    }

    /**
     * Checks if bill due notifications are enabled.
     */
    public static boolean getNotifyBills() {
        return prefs.getBoolean(KEY_NOTIFY_BILLS, true);
    }

    /**
     * Checks if subscription tracking notifications are enabled.
     */
    public static boolean getNotifySubscriptions() {
        return prefs.getBoolean(KEY_NOTIFY_SUBSCRIPTIONS, true);
    }

    /**
     * Checks if weekly summary notifications are enabled.
     */
    public static boolean getNotifyWeekly() {
        return prefs.getBoolean(KEY_NOTIFY_WEEKLY, true);
    }

    /**
     * Checks if overspending alert notifications are enabled.
     */
    public static boolean getNotifyOverspend() {
        return prefs.getBoolean(KEY_NOTIFY_OVERSPEND, true);
    }

    /**
     * Checks if motivational notifications are enabled.
     */
    public static boolean getNotifyMotivation() {
        return prefs.getBoolean(KEY_NOTIFY_MOTIVATION, true);
    }

    /**
     * Retrieves the notification frequency setting.
     */
    public static String getNotifyFrequency() {
        return prefs.get(KEY_NOTIFY_FREQUENCY, "Weekly");
    }

    /**
     * Retrieves the preferred notification time.
     */
    public static String getNotifyTime() {
        return prefs.get(KEY_NOTIFY_TIME, "9:00 AM");
    }

    /**
     * Checks if email notifications are enabled.
     */
    public static boolean getNotifyEmail() {
        return prefs.getBoolean(KEY_NOTIFY_EMAIL, true);
    }

    /**
     * Checks if desktop notifications are enabled.
     */
    public static boolean getNotifyDesktop() {
        return prefs.getBoolean(KEY_NOTIFY_DESKTOP, true);
    }

    /**
     * Checks if mobile push notifications are enabled.
     */
    public static boolean getNotifyPush() {
        return prefs.getBoolean(KEY_NOTIFY_PUSH, false);
    }

    /**
     * Checks if quiet hours are enabled.
     */
    public static boolean getQuietHours() {
        return prefs.getBoolean(KEY_QUIET_HOURS, false);
    }

    /**
     * Retrieves the quiet hours start time.
     */
    public static String getQuietFrom() {
        return prefs.get(KEY_QUIET_FROM, "22:00");
    }

    /**
     * Retrieves the quiet hours end time.
     */
    public static String getQuietTo() {
        return prefs.get(KEY_QUIET_TO, "07:00");
    }

    /**
     * Resets all settings to default values.
     */
    public static void resetAllSettings() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets only account settings to default values.
     */
    public static void resetAccountSettings() {
        try {
            prefs.remove(KEY_FULLNAME);
            prefs.remove(KEY_DOB);
            prefs.remove(KEY_USERNAME);
            prefs.remove(KEY_EMAIL);
            prefs.remove(KEY_PHONE);
            prefs.remove(KEY_ADDRESS);
            prefs.remove(KEY_CURRENCY);
            prefs.remove(KEY_TWO_FACTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets only application preferences to default values.
     */
    public static void resetAppPreferences() {
        try {
            prefs.remove(KEY_DARK_MODE);
            prefs.remove(KEY_FONT_SIZE);
            prefs.remove(KEY_LANGUAGE);
            prefs.remove(KEY_DATE_FORMAT);
            prefs.remove(KEY_TIME_FORMAT);
            prefs.remove(KEY_STARTUP);
            prefs.remove(KEY_START_MINIMIZED);
            prefs.remove(KEY_AUTO_BACKUP);
            prefs.remove(KEY_DEFAULT_VIEW);
            prefs.remove(KEY_CHART_TYPE);
            prefs.remove(KEY_ANIMATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets only notification settings to default values.
     */
    public static void resetNotificationSettings() {
        try {
            prefs.remove(KEY_NOTIFY_BILLS);
            prefs.remove(KEY_NOTIFY_SUBSCRIPTIONS);
            prefs.remove(KEY_NOTIFY_WEEKLY);
            prefs.remove(KEY_NOTIFY_OVERSPEND);
            prefs.remove(KEY_NOTIFY_MOTIVATION);
            prefs.remove(KEY_NOTIFY_FREQUENCY);
            prefs.remove(KEY_NOTIFY_TIME);
            prefs.remove(KEY_NOTIFY_EMAIL);
            prefs.remove(KEY_NOTIFY_DESKTOP);
            prefs.remove(KEY_NOTIFY_PUSH);
            prefs.remove(KEY_QUIET_HOURS);
            prefs.remove(KEY_QUIET_FROM);
            prefs.remove(KEY_QUIET_TO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}