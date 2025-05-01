package com.example.trial.settings;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Settings panel for the SpentWise budgeting application.
 * Provides an interface for users to manage account settings, app preferences,
 * notifications, and access help and information.
 */
public class SettingsPanel extends JPanel {
    //Color palette for the application
    private static final Color LIGHT_BLUE = new Color(251, 252, 252); // #fbfcfc
    private static final Color ACCENT_BLUE = new Color(41, 128, 185); // #2980b9
    private static final Color DARK_BLUE = new Color(23, 32, 42);    // #17202a
    private static final Color BLACK = new Color(26, 82, 118);       // #1a5276
    private static final Color LIGHT_GRAY = new Color(245, 245, 245); // Keep light gray for contrast
    private static final Color TEXT_COLOR = new Color(31, 97, 141);  // #1f618d

    //Typography definitions
    private static Font HEADER_FONT;
    private static Font SUBHEADER_FONT;
    private static Font REGULAR_FONT;

    //User preferences storage
    private Preferences prefs = Preferences.userNodeForPackage(SettingsPanel.class);

    //UI Components
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton accountBtn;
    private JButton preferencesBtn;
    private JButton notificationsBtn;
    private JButton helpBtn;
    private JButton aboutBtn;

    //Setting options
    private static final String[] FONT_SIZES = new String[]{"Small", "Medium", "Large"};
    private static final String[] DATE_FORMATS = new String[]{"MM/DD/YYYY", "DD/MM/YYYY", "YYYY-MM-DD"};
    private static final String[] TIME_FORMATS = new String[]{"12-hour (AM/PM)", "24-hour"};
    private static final String[] LANGUAGES = new String[]{"English", "Spanish", "French", "German", "Chinese", "Japanese"};

    /**
     * Creates a new settings panel with default configuration.
     */
    public SettingsPanel() {
        prefs = Preferences.userNodeForPackage(SettingsPanel.class);
        loadCustomFonts(); // Add this line before initializeUI
        initializeUI();
    }

    /**
     * Sets up the main UI components and layout.
     */
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(LIGHT_BLUE);
        JPanel sidebarPanel = this.createSidebar();
        this.createContentPanel();
        this.add(sidebarPanel, "West");
        this.add(this.contentPanel, "Center");
    }

    /**
     * Loads and registers the Lato font family from resources.
     */
    private void loadCustomFonts() {
        try {
            //Font file paths
            String fontPath = "src/main/resources/fonts/";

            //Load Lato font files
            Font latoRegular = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "Lato-Regular.ttf"));
            Font latoBold = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "Lato-Bold.ttf"));
            Font latoLight = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "Lato-Light.ttf"));

            //Register with the Graphics Environment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(latoRegular);
            ge.registerFont(latoBold);
            ge.registerFont(latoLight);

            //Update font constants
            HEADER_FONT = latoBold.deriveFont(24f);
            SUBHEADER_FONT = latoBold.deriveFont(16f);
            REGULAR_FONT = latoRegular.deriveFont(14f);

            System.out.println("Lato fonts loaded successfully");

        } catch (IOException | FontFormatException e) {
            System.out.println("Error loading Lato fonts: " + e.getMessage());
            //Fall back to system fonts if Lato can't be loaded
            HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
            SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
            REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
        }
    }

    /**
     * Creates the sidebar navigation panel with logo and menu buttons.
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 1));
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBackground(BLACK);
        sidebar.add(this.createLogoPanel(), "North");
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, 1));
        navPanel.setBackground(BLACK);
        ImageIcon accountIcon = this.createIcon("user", 16);
        ImageIcon preferencesIcon = this.createIcon("settings", 16);
        ImageIcon notificationsIcon = this.createIcon("bell", 16);
        ImageIcon helpIcon = this.createIcon("help-circle", 16);
        ImageIcon aboutIcon = this.createIcon("info", 16);
        this.accountBtn = this.createNavButton("Account", "account", accountIcon);
        this.preferencesBtn = this.createNavButton("App Preferences", "preferences", preferencesIcon);
        this.notificationsBtn = this.createNavButton("Notifications", "notifications", notificationsIcon);
        this.helpBtn = this.createNavButton("Help & Support", "help", helpIcon);
        this.aboutBtn = this.createNavButton("About Us", "about", aboutIcon);
        navPanel.add(this.accountBtn);
        navPanel.add(this.preferencesBtn);
        navPanel.add(this.notificationsBtn);
        navPanel.add(this.helpBtn);
        navPanel.add(this.aboutBtn);
        navPanel.add(Box.createVerticalGlue());
        sidebar.add(navPanel, "Center");
        return sidebar;
    }

    /**
     * Creates the logo panel with the SpentWise brand logo.
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(BLACK);
        logoPanel.setBorder(new EmptyBorder(15, 10, 10, 10));
        JLabel logoLabel = new JLabel("SpentWise");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Segoe UI", 1, 18));
        logoLabel.setHorizontalAlignment(0);

        try {
            ImageIcon logoIcon = new ImageIcon("spentwise_logo.png");
            Image img = logoIcon.getImage();
            BufferedImage roundedImage = new BufferedImage(110, 110, 2);
            Graphics2D g2 = roundedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new RoundRectangle2D.Float(0.0F, 0.0F, 110.0F, 110.0F, 20.0F, 20.0F));
            g2.drawImage(img, 0, 0, 110, 110, (ImageObserver)null);
            g2.dispose();
            logoIcon = new ImageIcon(roundedImage);
            logoLabel.setIcon(logoIcon);
            logoLabel.setText("");
        } catch (Exception e) {
            System.out.println("Could not load logo image: " + e.getMessage());
        }

        logoPanel.add(logoLabel, "Center");
        return logoPanel;
    }

    /**
     * Creates an icon for the navigation buttons.
     */
    private ImageIcon createIcon(String name, int size) {
        return new ImageIcon();
    }

    /**
     * Creates a styled navigation button for the sidebar.
     */
    private JButton createNavButton(String text, String cardName, ImageIcon icon) {
        final JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(2);
        button.setIcon(icon);
        button.setIconTextGap(10);
        button.setMaximumSize(new Dimension(200, 40));
        button.setCursor(new Cursor(12));
        button.setBorder(new EmptyBorder(10, 20, 10, 10));
        button.addActionListener((e) -> {
            this.highlightButton(button);
            this.cardLayout.show(this.contentPanel, cardName);
        });
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(60, 60, 60));
                }

            }

            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(SettingsPanel.BLACK);
                }

            }
        });
        return button;
    }

    /**
     * Highlights the selected navigation button and resets others.
     */
    private void highlightButton(JButton selectedButton) {
        this.accountBtn.setBackground(BLACK);
        this.accountBtn.setForeground(Color.WHITE);
        this.preferencesBtn.setBackground(BLACK);
        this.preferencesBtn.setForeground(Color.WHITE);
        this.notificationsBtn.setBackground(BLACK);
        this.notificationsBtn.setForeground(Color.WHITE);
        this.helpBtn.setBackground(BLACK);
        this.helpBtn.setForeground(Color.WHITE);
        this.aboutBtn.setBackground(BLACK);
        this.aboutBtn.setForeground(Color.WHITE);
        selectedButton.setBackground(ACCENT_BLUE);
        selectedButton.setForeground(Color.WHITE);
    }

    /**
     * Creates the main content panel with card layout for different sections.
     */
    private void createContentPanel() {
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(this.cardLayout);
        this.contentPanel.setBackground(LIGHT_BLUE);
        JPanel accountPanel = this.createAccountPanel();
        JPanel preferencesPanel = this.createPreferencesPanel();
        JPanel notificationsPanel = this.createNotificationsPanel();
        JPanel helpPanel = this.createHelpPanel();
        JPanel aboutPanel = this.createAboutPanel();
        this.contentPanel.add(accountPanel, "account");
        this.contentPanel.add(preferencesPanel, "preferences");
        this.contentPanel.add(notificationsPanel, "notifications");
        this.contentPanel.add(helpPanel, "help");
        this.contentPanel.add(aboutPanel, "about");
        this.cardLayout.show(this.contentPanel, "account");
        this.highlightButton(this.accountBtn);
    }

    /**
     * Creates the account settings panel with personal information fields.
     */
    private JPanel createAccountPanel() {
        JPanel panel = this.createPanelWithHeader("Account Settings", "Manage your personal information and preferences");
        JPanel formPanelContainer = new JPanel(new BorderLayout());
        formPanelContainer.setBackground(LIGHT_BLUE);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(LIGHT_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 2;
        gbc.insets = new Insets(8, 10, 8, 10);
        JLabel sectionLabel = new JLabel("Personal Information");
        sectionLabel.setFont(SUBHEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(sectionLabel, gbc);
        String[] labels = new String[]{"Full Name:", "Date of Birth:", "Username:", "Email Address:", "Phone Number:", "Address:"};
        JComponent[] fields = new JComponent[labels.length];
        fields[0] = new JTextField(20);
        JPanel dobPanel = new JPanel(new FlowLayout(0, 0, 0));
        dobPanel.setBackground(LIGHT_BLUE);
        String[] days = new String[31];

        for(int i = 0; i < 31; ++i) {
            days[i] = String.format("%02d", i + 1);
        }

        JComboBox<String> dayCombo = new JComboBox(days);
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        JComboBox<String> monthCombo = new JComboBox(months);
        String[] years = new String[100];
        int currentYear = LocalDate.now().getYear();

        for(int i = 0; i < 100; ++i) {
            years[i] = String.valueOf(currentYear - i);
        }

        JComboBox<String> yearCombo = new JComboBox(years);
        dobPanel.add(dayCombo);
        dobPanel.add(new JLabel(" / "));
        dobPanel.add(monthCombo);
        dobPanel.add(new JLabel(" / "));
        dobPanel.add(yearCombo);
        fields[1] = dobPanel;
        fields[2] = new JTextField(20);
        fields[3] = new JTextField(20);
        fields[4] = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        fields[5] = addressScrollPane;
        int gridy = 1;

        for(int i = 0; i < labels.length; ++i) {
            gbc.gridx = 0;
            gbc.gridy = gridy;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            JLabel fieldLabel = new JLabel(labels[i]);
            fieldLabel.setFont(REGULAR_FONT);
            formPanel.add(fieldLabel, gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            if (fields[i] instanceof JTextField) {
                ((JTextField)fields[i]).setFont(REGULAR_FONT);
            }

            formPanel.add(fields[i], gbc);
            ++gridy;
        }

        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel createdLabel = new JLabel("Account Created:");
        createdLabel.setFont(REGULAR_FONT);
        formPanel.add(createdLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel createdDateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        createdDateLabel.setFont(REGULAR_FONT);
        formPanel.add(createdDateLabel, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 2;
        JLabel currencyLabel = new JLabel("Currency Settings");
        currencyLabel.setFont(SUBHEADER_FONT);
        formPanel.add(currencyLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel currencyFieldLabel = new JLabel("Default Currency:");
        currencyFieldLabel.setFont(REGULAR_FONT);
        formPanel.add(currencyFieldLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        String[] currencies = new String[]{"USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)", "CAD ($)", "AUD ($)", "INR (₹)"};
        JComboBox<String> currencyCombo = new JComboBox(currencies);
        currencyCombo.setFont(REGULAR_FONT);
        formPanel.add(currencyCombo, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 2;
        JLabel securityLabel = new JLabel("Security Settings");
        securityLabel.setFont(SUBHEADER_FONT);
        securityLabel.setBorder(new EmptyBorder(15, 0, 5, 0));
        formPanel.add(securityLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 2;
        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.setFont(REGULAR_FONT);
        changePasswordBtn.setBackground(LIGHT_GRAY);
        changePasswordBtn.setForeground(BLACK);
        formPanel.add(changePasswordBtn, gbc);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel twoFactorLabel = new JLabel("Two-Factor Authentication:");
        twoFactorLabel.setFont(REGULAR_FONT);
        formPanel.add(twoFactorLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel twoFactorPanel = new JPanel(new FlowLayout(0, 0, 0));
        twoFactorPanel.setBackground(LIGHT_BLUE);
        JToggleButton twoFactorToggle = new JToggleButton();
        twoFactorToggle.setPreferredSize(new Dimension(60, 30));
        twoFactorToggle.setBackground(LIGHT_GRAY);
        twoFactorToggle.setForeground(BLACK);
        twoFactorToggle.addActionListener((e) -> {
            if (twoFactorToggle.isSelected()) {
                twoFactorToggle.setBackground(ACCENT_BLUE);
            } else {
                twoFactorToggle.setBackground(LIGHT_GRAY);
            }

        });
        twoFactorPanel.add(twoFactorToggle);
        formPanel.add(twoFactorPanel, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(2));
        buttonPanel.setBackground(LIGHT_BLUE);
        JButton cancelButton = this.createSecondaryButton("Cancel");
        JButton saveButton = this.createPrimaryButton("Save Changes");
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        formPanelContainer.add(formPanel, "North");
        JScrollPane scrollPane = new JScrollPane(formPanelContainer);
        scrollPane.setBorder((Border)null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, "Center");
        panel.add(buttonPanel, "South");
        return panel;
    }

    /**
     * Creates the application preferences panel.
     */
    private JPanel createPreferencesPanel() {
        JPanel panel = this.createPanelWithHeader("App Preferences", "Customize the application to suit your needs");
        JPanel prefsPanel = new JPanel(new GridBagLayout());
        prefsPanel.setBackground(LIGHT_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 2;
        gbc.insets = new Insets(8, 10, 8, 10);
        JLabel appearanceLabel = new JLabel("Appearance");
        appearanceLabel.setFont(SUBHEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        prefsPanel.add(appearanceLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        JLabel darkModeLabel = new JLabel("Dark Mode:");
        darkModeLabel.setFont(REGULAR_FONT);
        prefsPanel.add(darkModeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        JPanel darkModePanel = new JPanel(new FlowLayout(0, 0, 0));
        darkModePanel.setBackground(LIGHT_BLUE);
        JToggleButton darkModeToggle = new JToggleButton();
        darkModeToggle.setPreferredSize(new Dimension(60, 30));
        darkModeToggle.setBackground(LIGHT_GRAY);
        darkModeToggle.addActionListener((e) -> {
            if (darkModeToggle.isSelected()) {
                darkModeToggle.setBackground(ACCENT_BLUE);
            } else {
                darkModeToggle.setBackground(LIGHT_GRAY);
            }

        });
        darkModePanel.add(darkModeToggle);
        prefsPanel.add(darkModePanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel fontSizeLabel = new JLabel("Font Size:");
        fontSizeLabel.setFont(REGULAR_FONT);
        prefsPanel.add(fontSizeLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> fontSizeCombo = new JComboBox(FONT_SIZES);
        fontSizeCombo.setFont(REGULAR_FONT);
        fontSizeCombo.setSelectedItem("Medium");
        prefsPanel.add(fontSizeCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel langLabel = new JLabel("Language:");
        langLabel.setFont(REGULAR_FONT);
        prefsPanel.add(langLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> langCombo = new JComboBox(LANGUAGES);
        langCombo.setFont(REGULAR_FONT);
        prefsPanel.add(langCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Date Format:");
        dateLabel.setFont(REGULAR_FONT);
        prefsPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> dateCombo = new JComboBox(DATE_FORMATS);
        dateCombo.setFont(REGULAR_FONT);
        prefsPanel.add(dateCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel timeLabel = new JLabel("Time Format:");
        timeLabel.setFont(REGULAR_FONT);
        prefsPanel.add(timeLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> timeCombo = new JComboBox(TIME_FORMATS);
        timeCombo.setFont(REGULAR_FONT);
        prefsPanel.add(timeCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel startupLabel = new JLabel("Startup Behavior");
        startupLabel.setFont(SUBHEADER_FONT);
        prefsPanel.add(startupLabel, gbc);
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 10, 4, 10);
        JCheckBox startupCheckbox = new JCheckBox("Start application when system boots");
        startupCheckbox.setFont(REGULAR_FONT);
        startupCheckbox.setBackground(LIGHT_BLUE);
        prefsPanel.add(startupCheckbox, gbc);
        gbc.gridy = 8;
        JCheckBox minimizedCheckbox = new JCheckBox("Start minimized in system tray");
        minimizedCheckbox.setFont(REGULAR_FONT);
        minimizedCheckbox.setBackground(LIGHT_BLUE);
        prefsPanel.add(minimizedCheckbox, gbc);
        gbc.gridy = 9;
        JCheckBox backupCheckbox = new JCheckBox("Auto-backup data weekly");
        backupCheckbox.setFont(REGULAR_FONT);
        backupCheckbox.setBackground(LIGHT_BLUE);
        backupCheckbox.setSelected(true);
        prefsPanel.add(backupCheckbox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel budgetViewLabel = new JLabel("Budget View Preferences");
        budgetViewLabel.setFont(SUBHEADER_FONT);
        prefsPanel.add(budgetViewLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        JLabel defaultViewLabel = new JLabel("Default View:");
        defaultViewLabel.setFont(REGULAR_FONT);
        prefsPanel.add(defaultViewLabel, gbc);
        gbc.gridx = 1;
        String[] views = new String[]{"Monthly", "Weekly", "Yearly"};
        JComboBox<String> viewCombo = new JComboBox(views);
        viewCombo.setFont(REGULAR_FONT);
        prefsPanel.add(viewCombo, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(2));
        buttonPanel.setBackground(LIGHT_BLUE);
        JButton resetButton = this.createSecondaryButton("Reset to Defaults");
        JButton saveButton = this.createPrimaryButton("Save Preferences");
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        JScrollPane scrollPane = new JScrollPane(prefsPanel);
        scrollPane.setBorder((Border)null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, "Center");
        panel.add(buttonPanel, "South");
        return panel;
    }

    /**
     * Creates the notifications settings panel.
     */
    private JPanel createNotificationsPanel() {
        JPanel panel = this.createPanelWithHeader("Notification Settings", "Control how and when you receive alerts and reminders");
        JPanel notifPanelContainer = new JPanel(new BorderLayout());
        notifPanelContainer.setBackground(LIGHT_BLUE);
        JPanel notifPanel = new JPanel(new GridBagLayout());
        notifPanel.setBackground(LIGHT_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 2;
        gbc.insets = new Insets(8, 10, 8, 10);
        JLabel typesLabel = new JLabel("Notification Types");
        typesLabel.setFont(SUBHEADER_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        notifPanel.add(typesLabel, gbc);
        String[][] notificationTypes = new String[][]{{"Bill Due Reminder", "Get reminded when your bills are coming due"}, {"Subscription Tracking", "Track your recurring subscriptions and payments"}, {"Weekly Summaries", "Receive summaries of your spending habits each week"}, {"Overspending Nudges", "Be alerted when you're nearing your budget limits"}, {"Motivational Boosts", "Get positive reinforcement when you meet your goals"}};
        int gridy = 1;

        for(String[] type : notificationTypes) {
            gbc.gridx = 0;
            gbc.gridy = gridy;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;
            JToggleButton toggle = new JToggleButton();
            toggle.setPreferredSize(new Dimension(50, 25));
            toggle.setBackground(ACCENT_BLUE);
            toggle.setSelected(true);
            toggle.addActionListener((e) -> {
                if (toggle.isSelected()) {
                    toggle.setBackground(ACCENT_BLUE);
                } else {
                    toggle.setBackground(LIGHT_GRAY);
                }

            });
            notifPanel.add(toggle, gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.3;
            JLabel titleLabel = new JLabel(type[0]);
            titleLabel.setFont(new Font("Segoe UI", 1, 14));
            notifPanel.add(titleLabel, gbc);
            gbc.gridx = 2;
            gbc.weightx = 0.6;
            JLabel descLabel = new JLabel(type[1]);
            descLabel.setFont(REGULAR_FONT);
            descLabel.setForeground(new Color(100, 100, 100));
            notifPanel.add(descLabel, gbc);
            ++gridy;
        }

        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel frequencyLabel = new JLabel("Notification Frequency");
        frequencyLabel.setFont(SUBHEADER_FONT);
        notifPanel.add(frequencyLabel, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);
        JPanel frequencyPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        frequencyPanel.setBackground(LIGHT_BLUE);
        ButtonGroup frequencyGroup = new ButtonGroup();
        JRadioButton dailyRadio = new JRadioButton("Daily - Receive notifications once per day");
        dailyRadio.setFont(REGULAR_FONT);
        dailyRadio.setBackground(LIGHT_BLUE);
        JRadioButton weeklyRadio = new JRadioButton("Weekly - Receive a weekly summary");
        weeklyRadio.setFont(REGULAR_FONT);
        weeklyRadio.setBackground(LIGHT_BLUE);
        weeklyRadio.setSelected(true);
        JRadioButton monthlyRadio = new JRadioButton("Monthly - Receive a monthly report");
        monthlyRadio.setFont(REGULAR_FONT);
        monthlyRadio.setBackground(LIGHT_BLUE);
        frequencyGroup.add(dailyRadio);
        frequencyGroup.add(weeklyRadio);
        frequencyGroup.add(monthlyRadio);
        frequencyPanel.add(dailyRadio);
        frequencyPanel.add(weeklyRadio);
        frequencyPanel.add(monthlyRadio);
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 3;
        notifPanel.add(frequencyPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel timeOfDayLabel = new JLabel("Notification Time");
        timeOfDayLabel.setFont(SUBHEADER_FONT);
        notifPanel.add(timeOfDayLabel, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        JLabel deliveryTimeLabel = new JLabel("Delivery Time:");
        deliveryTimeLabel.setFont(REGULAR_FONT);
        notifPanel.add(deliveryTimeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel timeSelectionPanel = new JPanel(new FlowLayout(0, 5, 0));
        timeSelectionPanel.setBackground(LIGHT_BLUE);
        String[] hours = new String[12];

        for(int i = 0; i < 12; ++i) {
            hours[i] = String.format("%d", i + 1);
        }

        String[] minutes = new String[4];

        for(int i = 0; i < 4; ++i) {
            minutes[i] = String.format("%02d", i * 15);
        }

        JComboBox<String> hourCombo = new JComboBox(hours);
        JComboBox<String> minuteCombo = new JComboBox(minutes);
        JComboBox<String> ampmCombo = new JComboBox(new String[]{"AM", "PM"});
        timeSelectionPanel.add(hourCombo);
        timeSelectionPanel.add(new JLabel(":"));
        timeSelectionPanel.add(minuteCombo);
        timeSelectionPanel.add(ampmCombo);
        notifPanel.add(timeSelectionPanel, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel methodsLabel = new JLabel("Notification Methods");
        methodsLabel.setFont(SUBHEADER_FONT);
        notifPanel.add(methodsLabel, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        JCheckBox emailCheckbox = new JCheckBox();
        emailCheckbox.setBackground(LIGHT_BLUE);
        emailCheckbox.setSelected(true);
        notifPanel.add(emailCheckbox, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.9;
        JLabel emailLabel = new JLabel("Email notifications");
        emailLabel.setFont(REGULAR_FONT);
        notifPanel.add(emailLabel, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        JCheckBox desktopCheckbox = new JCheckBox();
        desktopCheckbox.setBackground(LIGHT_BLUE);
        desktopCheckbox.setSelected(true);
        notifPanel.add(desktopCheckbox, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.9;
        JLabel desktopLabel = new JLabel("Desktop notifications");
        desktopLabel.setFont(REGULAR_FONT);
        notifPanel.add(desktopLabel, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        JCheckBox pushCheckbox = new JCheckBox();
        pushCheckbox.setBackground(LIGHT_BLUE);
        pushCheckbox.setSelected(false);
        notifPanel.add(pushCheckbox, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.9;
        JLabel pushLabel = new JLabel("Mobile push notifications (requires mobile app)");
        pushLabel.setFont(REGULAR_FONT);
        notifPanel.add(pushLabel, gbc);
        ++gridy;
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel quietLabel = new JLabel("Quiet Hours");
        quietLabel.setFont(SUBHEADER_FONT);
        notifPanel.add(quietLabel, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        JCheckBox quietHoursCheckbox = new JCheckBox();
        quietHoursCheckbox.setBackground(LIGHT_BLUE);
        notifPanel.add(quietHoursCheckbox, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.9;
        JLabel quietHoursLabel = new JLabel("Enable quiet hours");
        quietHoursLabel.setFont(REGULAR_FONT);
        notifPanel.add(quietHoursLabel, gbc);
        ++gridy;
        JPanel timePanel = new JPanel(new FlowLayout(0));
        timePanel.setBackground(LIGHT_BLUE);
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(REGULAR_FONT);
        String[] fullHours = new String[24];

        for(int i = 0; i < 24; ++i) {
            fullHours[i] = String.format("%02d:00", i);
        }

        JComboBox<String> fromCombo = new JComboBox(fullHours);
        fromCombo.setSelectedItem("22:00");
        JLabel toLabel = new JLabel("   To:");
        toLabel.setFont(REGULAR_FONT);
        JComboBox<String> toCombo = new JComboBox(fullHours);
        toCombo.setSelectedItem("07:00");
        timePanel.add(fromLabel);
        timePanel.add(fromCombo);
        timePanel.add(toLabel);
        timePanel.add(toCombo);
        gbc.gridx = 0;
        gbc.gridy = gridy++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 30, 8, 10);
        notifPanel.add(timePanel, gbc);
        notifPanelContainer.add(notifPanel, "North");
        JPanel buttonPanel = new JPanel(new FlowLayout(2));
        buttonPanel.setBackground(LIGHT_BLUE);
        JButton cancelButton = this.createSecondaryButton("Cancel");
        JButton saveButton = this.createPrimaryButton("Save Notifications");
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        JScrollPane scrollPane = new JScrollPane(notifPanelContainer);
        scrollPane.setBorder((Border)null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, "Center");
        panel.add(buttonPanel, "South");
        return panel;
    }

    /**
     * Creates the help and support panel with FAQs and contact form.
     */
    private JPanel createHelpPanel() {
        JPanel panel = this.createPanelWithHeader("Help & Support", "Get assistance and answers to your questions");
        JPanel helpContent = new JPanel();
        helpContent.setLayout(new BoxLayout(helpContent, 1));
        helpContent.setBackground(LIGHT_BLUE);
        helpContent.setBorder(new EmptyBorder(0, 20, 20, 20));
        JLabel faqLabel = new JLabel("Frequently Asked Questions");
        faqLabel.setFont(SUBHEADER_FONT);
        faqLabel.setAlignmentX(0.0F);
        faqLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        helpContent.add(faqLabel);
        String[][] faqItems = new String[][]{{"How do I create a new budget category?", "To create a new budget category, navigate to the 'Budgets' tab and click the '+' button. Enter a name for your category, set a monthly limit, and choose a color. You can also add subcategories if needed. Click 'Save' when done."}, {"How can I export my financial data?", "To export your data, go to 'Settings > Account' and scroll down to find the 'Export Data' button. You can choose between CSV, PDF, or Excel formats. Your data will be securely packaged and downloaded to your computer."}, {"How do I set up automatic bill payments?", "SpentWise doesn't directly process payments but can remind you when bills are due. Go to 'Bills > Add Bill' and enter the details including payment amount, due date, and recurrence. Enable notifications to get reminders before the due date."}, {"Can I sync data across multiple devices?", "Yes! SpentWise automatically syncs your data across all your devices. Simply log in with the same account on each device, and your budgets, expenses, and settings will be synchronized in real-time."}, {"How do I recover my password?", "If you've forgotten your password, click on the 'Forgot Password' link on the login screen. Enter your email address, and we'll send you a password reset link. For security reasons, this link expires after 24 hours."}, {"Is my financial data secure?", "Absolutely. SpentWise uses bank-level encryption to protect your data. We never store your bank account passwords and use secure connections for all data transfers. Your privacy and security are our top priorities."}};

        for(String[] item : faqItems) {
            JPanel faqItem = this.createCollapsiblePanel(item[0], item[1]);
            helpContent.add(faqItem);
            helpContent.add(Box.createVerticalStrut(5));
        }

        JLabel tutorialsLabel = new JLabel("Video Tutorials");
        tutorialsLabel.setFont(SUBHEADER_FONT);
        tutorialsLabel.setAlignmentX(0.0F);
        tutorialsLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        helpContent.add(tutorialsLabel);
        String[] tutorials = new String[]{"Getting Started with SpentWise", "Creating Your First Budget", "Tracking Expenses Efficiently", "Setting and Achieving Financial Goals", "Advanced Reporting and Analysis"};

        for(String tutorial : tutorials) {
            JButton tutorialBtn = new JButton(tutorial);
            tutorialBtn.setFont(REGULAR_FONT);
            tutorialBtn.setBackground(LIGHT_GRAY);
            tutorialBtn.setForeground(BLACK);
            tutorialBtn.setAlignmentX(0.0F);
            tutorialBtn.setBorderPainted(false);
            tutorialBtn.setFocusPainted(false);
            tutorialBtn.setCursor(new Cursor(12));
            tutorialBtn.setMaximumSize(new Dimension(500, 40));
            tutorialBtn.setHorizontalAlignment(2);
            ImageIcon playIcon = this.createIcon("play", 16);
            tutorialBtn.setIcon(playIcon);
            tutorialBtn.setIconTextGap(10);
            helpContent.add(tutorialBtn);
            helpContent.add(Box.createVerticalStrut(5));
        }

        JLabel supportLabel = new JLabel("Contact Support");
        supportLabel.setFont(SUBHEADER_FONT);
        supportLabel.setAlignmentX(0.0F);
        supportLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        helpContent.add(supportLabel);
        JLabel supportDesc = new JLabel("Having trouble? Send us a message and we'll get back to you within 24 hours.");
        supportDesc.setFont(REGULAR_FONT);
        supportDesc.setAlignmentX(0.0F);
        supportDesc.setBorder(new EmptyBorder(0, 0, 10, 0));
        helpContent.add(supportDesc);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(LIGHT_BLUE);
        formPanel.setAlignmentX(0.0F);
        formPanel.setMaximumSize(new Dimension(600, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = (double)1.0F;
        JLabel issueTypeLabel = new JLabel("Issue Type:");
        issueTypeLabel.setFont(REGULAR_FONT);
        formPanel.add(issueTypeLabel, gbc);
        gbc.gridy = 1;
        String[] issueTypes = new String[]{"Technical Problem", "Account Issue", "Billing Question", "Feature Request", "Other"};
        JComboBox<String> issueTypeCombo = new JComboBox(issueTypes);
        issueTypeCombo.setFont(REGULAR_FONT);
        formPanel.add(issueTypeCombo, gbc);
        gbc.gridy = 2;
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(REGULAR_FONT);
        formPanel.add(subjectLabel, gbc);
        gbc.gridy = 3;
        JTextField subjectField = new JTextField(20);
        subjectField.setFont(REGULAR_FONT);
        formPanel.add(subjectField, gbc);
        gbc.gridy = 4;
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(REGULAR_FONT);
        formPanel.add(messageLabel, gbc);
        gbc.gridy = 5;
        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setFont(REGULAR_FONT);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        formPanel.add(scrollPane, gbc);
        gbc.gridy = 6;
        JPanel attachmentPanel = new JPanel(new BorderLayout());
        attachmentPanel.setBackground(LIGHT_BLUE);
        JButton attachButton = new JButton("Attach Screenshot");
        attachButton.setFont(REGULAR_FONT);
        attachButton.setBackground(LIGHT_GRAY);
        attachButton.setForeground(BLACK);
        attachmentPanel.add(attachButton, "West");
        formPanel.add(attachmentPanel, gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 5, 0);
        JButton submitButton = this.createPrimaryButton("Submit Request");
        submitButton.setAlignmentX(0.0F);
        formPanel.add(submitButton, gbc);
        helpContent.add(formPanel);
        JScrollPane scrollableContent = new JScrollPane(helpContent);
        scrollableContent.setBorder((Border)null);
        scrollableContent.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollableContent, "Center");
        return panel;
    }

    /**
     * Creates the about panel with information about SpentWise.
     */
    private JPanel createAboutPanel() {
        JPanel panel = this.createPanelWithHeader("About Us", "Learn more about SpentWise");
        JPanel aboutContent = new JPanel();
        aboutContent.setLayout(new BoxLayout(aboutContent, 1));
        aboutContent.setBackground(LIGHT_BLUE);
        aboutContent.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel logoLabel = new JLabel();
        logoLabel.setText("SPENTWISE");
        logoLabel.setFont(new Font("Segoe UI", 1, 28));
        logoLabel.setForeground(DARK_BLUE);
        logoLabel.setAlignmentX(0.5F);
        aboutContent.add(logoLabel);
        JLabel versionLabel = new JLabel("Version 1.2.0");
        versionLabel.setFont(new Font("Segoe UI", 0, 14));
        versionLabel.setAlignmentX(0.5F);
        versionLabel.setBorder(new EmptyBorder(5, 0, 20, 0));
        aboutContent.add(versionLabel);
        String aboutText = "SpentWise is a personal budgeting application that we designed to help you take control of your finances. Whether you're saving for a big purchase, trying to pay off a debt, or just want to know where your money is going, SpentWise provides the tools you need to achieve success.";
        JLabel aboutLabel = this.createWrappedTextLabel(aboutText);
        aboutLabel.setAlignmentX(0.5F);
        aboutContent.add(aboutLabel);
        String[][] sections = new String[][]{{"Our Mission", "At SpentWise, our mission is to simplify budgeting and help users take control of their finances through intuitive tools, personalized insights, and accessible education. We believe everyone should have an easy, powerful way to manage their money and achieve their financial goals."}, {"Our Story", "SpentWise was founded in 2022 out of a frustration with complicated and rigid budgeting apps. What began as a basic expense tracker has grown into a full-featured budgeting platform trusted by over 100,000 users worldwide. Our team continuously evolves SpentWise based on real user feedback and the latest financial trends."}, {"Our Team", "Our team brings together specialists in finance, technology, and user-centered design. Led by CEO Jane Smith, a former financial advisor and tech entrepreneur, we are passionate about making budgeting simple, effective, and stress-free. We're headquartered in Seattle, with remote contributors across North America and Europe."}, {"Privacy & Security", "Protecting your financial information is our top priority. SpentWise uses 256-bit AES encryption to secure all data, both in storage and in transit. We never sell your personal data to third parties, and you always have full control over your information. Our systems are regularly audited by independent security experts to maintain the highest standards of protection."}};

        for(String[] section : sections) {
            JPanel sectionPanel = this.createCollapsiblePanel(section[0], section[1]);
            sectionPanel.setAlignmentX(0.5F);
            sectionPanel.setMaximumSize(new Dimension(600, 1000));
            aboutContent.add(sectionPanel);
            aboutContent.add(Box.createVerticalStrut(5));
        }

        JLabel featuresTitle = new JLabel("Key Features:");
        featuresTitle.setFont(new Font("Segoe UI", 1, 16));
        featuresTitle.setAlignmentX(0.5F);
        featuresTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        aboutContent.add(featuresTitle);
        String[] features = new String[]{"Intuitive budget creation and tracking", "Detailed spending analysis and reports", "Bill payment reminders and scheduling", "Financial goal setting and monitoring", "Secure data encryption and backup"};
        JPanel featurePanel = new JPanel(new GridLayout(features.length, 1, 0, 5));
        featurePanel.setBackground(LIGHT_BLUE);
        featurePanel.setAlignmentX(0.5F);
        featurePanel.setMaximumSize(new Dimension(500, features.length * 30));

        for(String feature : features) {
            JLabel featureLabel = new JLabel("• " + feature);
            featureLabel.setFont(REGULAR_FONT);
            featureLabel.setHorizontalAlignment(0);
            featurePanel.add(featureLabel);
        }

        aboutContent.add(featurePanel);
        JLabel contactTitle = new JLabel("Contact Information:");
        contactTitle.setFont(new Font("Segoe UI", 1, 16));
        contactTitle.setAlignmentX(0.5F);
        contactTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        aboutContent.add(contactTitle);
        JPanel contactPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        contactPanel.setBackground(LIGHT_BLUE);
        contactPanel.setAlignmentX(0.5F);
        contactPanel.setMaximumSize(new Dimension(400, 90));
        JLabel emailLabel = new JLabel("Email: support@spentwise.com");
        emailLabel.setFont(REGULAR_FONT);
        emailLabel.setHorizontalAlignment(0);
        JLabel websiteLabel = new JLabel("Website: www.spentwise.com");
        websiteLabel.setFont(REGULAR_FONT);
        websiteLabel.setHorizontalAlignment(0);
        JLabel socialLabel = new JLabel("Social: @SpentWise");
        socialLabel.setFont(REGULAR_FONT);
        socialLabel.setHorizontalAlignment(0);
        contactPanel.add(emailLabel);
        contactPanel.add(websiteLabel);
        contactPanel.add(socialLabel);
        aboutContent.add(contactPanel);
        JLabel copyrightLabel = new JLabel("© 2025 SpentWise, Inc. All rights reserved.");
        copyrightLabel.setFont(new Font("Segoe UI", 2, 12));
        copyrightLabel.setAlignmentX(0.5F);
        copyrightLabel.setBorder(new EmptyBorder(30, 0, 10, 0));
        aboutContent.add(copyrightLabel);
        JScrollPane scrollableContent = new JScrollPane(aboutContent);
        scrollableContent.setBorder((Border)null);
        scrollableContent.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollableContent, "Center");
        return panel;
    }

    /**
     * Creates a collapsible panel with a title and content.
     * Modified to create a proper dropdown effect.
     */
    private JPanel createCollapsiblePanel(String title, String content) {
        //Main container panel with border layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(5, 0, 5, 0)));

        //Title button at the NORTH position (will stay fixed)
        JButton titleBtn = new JButton(title);
        titleBtn.setFont(new Font("Segoe UI", 1, 14));
        titleBtn.setHorizontalAlignment(2); // 2 is LEFT alignment
        titleBtn.setBorderPainted(false);
        titleBtn.setContentAreaFilled(false);
        titleBtn.setFocusPainted(false);
        titleBtn.setIcon(new ArrowIcon(true));
        titleBtn.setIconTextGap(10);

        //Content panel in the CENTER position (will expand below the title)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_BLUE);
        contentPanel.setBorder(new EmptyBorder(10, 20, 5, 0));
        contentPanel.setVisible(false);

        //Content text
        JLabel contentLabel = new JLabel("<html><p style='width:500px;'>" + content + "</p></html>");
        contentLabel.setFont(REGULAR_FONT);
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        //Toggle action
        titleBtn.addActionListener(e -> {
            contentPanel.setVisible(!contentPanel.isVisible());
            titleBtn.setIcon(new ArrowIcon(!contentPanel.isVisible()));

            //Force layout update
            panel.revalidate();
            panel.repaint();
        });

        //Add components to panel - title fixed at NORTH, content expands in CENTER
        panel.add(titleBtn, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel with a header containing title and subtitle.
     */
    private JPanel createPanelWithHeader(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BLUE);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_BLUE);
        headerPanel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), new EmptyBorder(20, 30, 20, 30)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, "North");
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(REGULAR_FONT);
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        headerPanel.add(subtitleLabel, "Center");
        panel.add(headerPanel, "North");
        return panel;
    }

    /**
     * Creates a text label with automatic word wrapping.
     */
    private JLabel createWrappedTextLabel(String text) {
        JLabel label = new JLabel("<html><p style='width:500px;'>" + text + "</p></html>");
        label.setFont(REGULAR_FONT);
        label.setAlignmentX(0.0F);
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        return label;
    }

    /**
     * Creates a styled primary button with hover effects.
     */
    private JButton createPrimaryButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", 1, 14));
        button.setBackground(DARK_BLUE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(12));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(21, 101, 192));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(SettingsPanel.DARK_BLUE);
            }
        });
        return button;
    }

    /**
     * Creates a styled secondary button with hover effects.
     */
    private JButton createSecondaryButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", 0, 14));
        button.setBackground(LIGHT_GRAY);
        button.setForeground(BLACK);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(12));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(SettingsPanel.LIGHT_GRAY);
            }
        });
        return button;
    }

    /**
     * Main entry point to run the settings panel as a standalone application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SpentWise - Settings");
            frame.setDefaultCloseOperation(3);
            frame.getContentPane().add(new SettingsPanel());
            frame.setSize(900, 600);
            frame.setLocationRelativeTo((Component)null);
            frame.setVisible(true);
        });
    }

    /**
     * Custom arrow icon for collapsible panels.
     */
    private static class ArrowIcon implements Icon {
        private final boolean pointRight;

        public ArrowIcon(boolean pointRight) {
            this.pointRight = pointRight;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(SettingsPanel.BLACK);
            int size = 8;
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];
            if (this.pointRight) {
                xPoints[0] = x;
                xPoints[1] = x + size;
                xPoints[2] = x;
                yPoints[0] = y;
                yPoints[1] = y + size / 2;
                yPoints[2] = y + size;
            } else {
                xPoints[0] = x;
                xPoints[1] = x + size;
                xPoints[2] = x + size;
                yPoints[0] = y + size / 2;
                yPoints[1] = y;
                yPoints[2] = y + size;
            }

            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.dispose();
        }

        public int getIconWidth() {
            return 8;
        }

        public int getIconHeight() {
            return 8;
        }
    }
}