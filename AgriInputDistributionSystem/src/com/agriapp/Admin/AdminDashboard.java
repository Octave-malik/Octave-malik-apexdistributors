package com.agriapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private static final Dimension BUTTON_SIZE = new Dimension(180, 50);
    private JPanel mainPanel;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/agricultural_input_distribution";
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "nzioki";  // Your MySQL password

    public AdminDashboard() {
        // Set up the dashboard window
        setTitle("Admin Dashboard - Agricultural Input Distribution System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for the navigation menu
        JPanel sidePanel = createSidePanel();

        // Initialize the main content panel
        mainPanel = createMainPanel();

        // Set up layout and add panels to the frame
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create navigation buttons
        JButton btnDashboard = createNavButton("Dashboard");
        JButton btnManageUsers = createNavButton("Manage Users");
        JButton btnViewReports = createNavButton("View Reports");
        JButton btnManageInputs = createNavButton("Manage Inputs");
        JButton btnOrders = createNavButton("Orders & Distribution");
        JButton btnSettings = createNavButton("Settings");
        JButton btnLogout = createLogoutButton();

        // Add buttons to the panel
        panel.add(btnDashboard);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnManageUsers);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnViewReports);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnManageInputs);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnOrders);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSettings);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnLogout);

        return panel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(45, 45, 48));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);

        // Action listener for navigation
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (text) {
                    case "Dashboard":
                        showDashboard();
                        break;
                    case "Manage Users":
                        showManageUsers();
                        break;
                    case "View Reports":
                        showViewReports();
                        break;
                    case "Manage Inputs":
                        showManageInputs();
                        break;
                    case "Orders & Distribution":
                        showOrders();
                        break;
                    case "Settings":
                        showSettings();
                        break;
                }
            }
        });

        return button;
    }

    private JButton createLogoutButton() {
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogout.setBackground(new Color(255, 69, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER);
        btnLogout.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnLogout.setPreferredSize(BUTTON_SIZE);
        btnLogout.setMaximumSize(BUTTON_SIZE);
        btnLogout.setMinimumSize(BUTTON_SIZE);

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out...");
                dispose(); // Close the dashboard window
                new LoginPage(); // Redirect to the login page
            }
        });

        return btnLogout;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Placeholder content for the dashboard
        JLabel placeholder = new JLabel("Welcome to the Admin Dashboard", SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }

    // Update the main panel to display the dashboard stats
    private void showDashboard() {
        // Fetch data from the database
        String[][] data = fetchDashboardData();
        updateMainPanelWithTable(data);
    }

    // Fetch data from the database
    private String[][] fetchDashboardData() {
        String queryUsers = "SELECT COUNT(*) FROM Users";
        String queryOrders = "SELECT COUNT(*) FROM Distributions";
        String queryDistributed = "SELECT SUM(quantity) FROM Distributions";
        
        int totalUsers = 0, totalOrders = 0, totalDistributed = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Query total users
            ResultSet rs = stmt.executeQuery(queryUsers);
            if (rs.next()) {
                totalUsers = rs.getInt(1);
            }

            // Query total orders
            rs = stmt.executeQuery(queryOrders);
            if (rs.next()) {
                totalOrders = rs.getInt(1);
            }

            // Query total distributed inputs
            rs = stmt.executeQuery(queryDistributed);
            if (rs.next()) {
                totalDistributed = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new String[][] {
            {"Total Users", String.valueOf(totalUsers)},
            {"Total Orders", String.valueOf(totalOrders)},
            {"Total Distributed Inputs", String.valueOf(totalDistributed)}
        };
    }

    private void updateMainPanelWithTable(String[][] data) {
        // Create the dashboard table
        String[] columns = {"Statistic", "Value"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable dashboardTable = new JTable(tableModel);
        dashboardTable.setFillsViewportHeight(true);
        
        // Add data rows to the table
        for (String[] row : data) {
            tableModel.addRow(row);
        }

        // Add the table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(dashboardTable);

        // Update the main panel
        mainPanel.removeAll();
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Manage Users Section
    private void showManageUsers() {
    // Fetch all users (not just admins) from the database
    String[][] userData = fetchAllUsers();

    // Set up the table for displaying users
    String[] columns = {"User ID", "Username", "Role", "Actions"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable userTable = new JTable(tableModel);
    userTable.setFillsViewportHeight(true);
    userTable.setRowHeight(30);

    // Add fetched users to the table
    for (String[] row : userData) {
        tableModel.addRow(row);
    }

    // Add actions for adding, editing, and deleting users
    JPanel actionsPanel = new JPanel();
    actionsPanel.setLayout(new FlowLayout());
    JButton btnAddUser = new JButton("Add User");
    JButton btnEditUser = new JButton("Edit User");
    JButton btnDeleteUser = new JButton("Delete User");

    // Button Actions
    btnAddUser.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAddUserDialog();
        }
    });

    btnEditUser.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String userId = (String) tableModel.getValueAt(selectedRow, 0);
                showEditUserDialog(userId);
            } else {
                JOptionPane.showMessageDialog(null, "Select a user to edit.");
            }
        }
    });

    btnDeleteUser.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String userId = (String) tableModel.getValueAt(selectedRow, 0);
                deleteUser(userId);
            } else {
                JOptionPane.showMessageDialog(null, "Select a user to delete.");
            }
        }
    });

    actionsPanel.add(btnAddUser);
    actionsPanel.add(btnEditUser);
    actionsPanel.add(btnDeleteUser);

    // Place table and actions panel on the main panel
    JPanel manageUsersPanel = new JPanel(new BorderLayout());
    manageUsersPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
    manageUsersPanel.add(actionsPanel, BorderLayout.SOUTH);

    // Update the main panel
    mainPanel.removeAll();
    mainPanel.add(manageUsersPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

// Method to fetch all users from the database
private String[][] fetchAllUsers() {
    String query = "SELECT * FROM Users";  // Get all users
    java.util.List<String[]> users = new java.util.ArrayList<>();

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = conn.createStatement()) {

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String userId = rs.getString("user_id");
            String username = rs.getString("username");
            String role = rs.getString("role");
            users.add(new String[]{userId, username, role});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return users.toArray(new String[0][]);
}

// Method to handle adding a new user
private void showAddUserDialog() {
    JDialog addUserDialog = new JDialog(this, "Add User", true);
    addUserDialog.setSize(300, 200);
    addUserDialog.setLocationRelativeTo(this);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));
    
    JTextField txtUsername = new JTextField();
    JTextField txtRole = new JTextField();

    panel.add(new JLabel("Username:"));
    panel.add(txtUsername);
    panel.add(new JLabel("Role:"));
    panel.add(txtRole);

    JButton btnSave = new JButton("Save");
    btnSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = txtUsername.getText();
            String role = txtRole.getText();

            if (!username.isEmpty() && !role.isEmpty()) {
                addUserToDatabase(username, role);
                addUserDialog.dispose(); // Close the dialog
                showManageUsers(); // Refresh the user table
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            }
        }
    });

    panel.add(btnSave);
    addUserDialog.add(panel);
    addUserDialog.setVisible(true);
}

// Method to insert a new user into the database
private void addUserToDatabase(String username, String role) {
    String query = "INSERT INTO Users (username, role) VALUES (?, ?)";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, username);
        stmt.setString(2, role);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to handle editing an existing user
private void showEditUserDialog(String userId) {
    JDialog editUserDialog = new JDialog(this, "Edit User", true);
    editUserDialog.setSize(300, 200);
    editUserDialog.setLocationRelativeTo(this);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));

    // Fetch existing user details
    String[] userDetails = fetchUserById(userId);

    JTextField txtUsername = new JTextField(userDetails[0]);
    JTextField txtRole = new JTextField(userDetails[1]);

    panel.add(new JLabel("Username:"));
    panel.add(txtUsername);
    panel.add(new JLabel("Role:"));
    panel.add(txtRole);

    JButton btnSave = new JButton("Save");
    btnSave.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = txtUsername.getText();
            String role = txtRole.getText();

            if (!username.isEmpty() && !role.isEmpty()) {
                updateUserInDatabase(userId, username, role);
                editUserDialog.dispose();
                showManageUsers(); // Refresh the user table
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            }
        }
    });

    panel.add(btnSave);
    editUserDialog.add(panel);
    editUserDialog.setVisible(true);
}

// Method to fetch a user by their ID
private String[] fetchUserById(String userId) {
    String query = "SELECT username, role FROM Users WHERE user_id = ?";
    String[] userDetails = new String[2];

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            userDetails[0] = rs.getString("username");
            userDetails[1] = rs.getString("role");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return userDetails;
}

// Method to update user details in the database
private void updateUserInDatabase(String userId, String username, String role) {
    String query = "UPDATE Users SET username = ?, role = ? WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, username);
        stmt.setString(2, role);
        stmt.setString(3, userId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to handle deleting a user
private void deleteUser(String userId) {
    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", 
                                                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        String query = "DELETE FROM Users WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.executeUpdate();
            showManageUsers(); // Refresh the user table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    // New methods

    // Method to display the Reports Section
private void showViewReports() {
    // Fetch report data from the database
    String[][] reportData = fetchReportData();

    // Set up the table for displaying the report
    String[] columns = {"Report Item", "Value"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable reportTable = new JTable(tableModel);
    reportTable.setFillsViewportHeight(true);
    reportTable.setRowHeight(30);

    // Add fetched report data to the table
    for (String[] row : reportData) {
        tableModel.addRow(row);
    }

    // Create a panel for the report table and the print button
    JPanel reportPanel = new JPanel(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(reportTable);
    JButton btnPrint = new JButton("Print Report");

    // Add action listener for printing the report
    btnPrint.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                boolean printed = reportTable.print();
                if (printed) {
                    JOptionPane.showMessageDialog(null, "Report printed successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Printing canceled.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error printing report: " + ex.getMessage());
            }
        }
    });

    // Add components to the panel
    reportPanel.add(scrollPane, BorderLayout.CENTER);
    reportPanel.add(btnPrint, BorderLayout.SOUTH);

    // Update the main panel
    mainPanel.removeAll();
    mainPanel.add(reportPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

// Method to fetch report data from the database
private String[][] fetchReportData() {
    String queryUsers = "SELECT COUNT(*) FROM Users";
    String queryOrders = "SELECT COUNT(*) FROM Distributions";
    String queryDistributed = "SELECT SUM(quantity) FROM Distributions";
    String queryAgents = "SELECT COUNT(*) FROM Users WHERE role = 'Agent'";

    int totalUsers = 0, totalOrders = 0, totalDistributed = 0, totalAgents = 0;

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = conn.createStatement()) {

        // Query total users
        ResultSet rs = stmt.executeQuery(queryUsers);
        if (rs.next()) {
            totalUsers = rs.getInt(1);
        }

        // Query total orders
        rs = stmt.executeQuery(queryOrders);
        if (rs.next()) {
            totalOrders = rs.getInt(1);
        }

        // Query total distributed inputs
        rs = stmt.executeQuery(queryDistributed);
        if (rs.next()) {
            totalDistributed = rs.getInt(1);
        }

        // Query total agents
        rs = stmt.executeQuery(queryAgents);
        if (rs.next()) {
            totalAgents = rs.getInt(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return new String[][] {
        {"Total Users", String.valueOf(totalUsers)},
        {"Total Orders", String.valueOf(totalOrders)},
        {"Total Distributed Inputs", String.valueOf(totalDistributed)},
        {"Total Agents", String.valueOf(totalAgents)}
    };
}



    private void showManageInputs() {
        updateMainPanel("Manage Inputs page content will be added here.");
    }

    private void showOrders() {
        updateMainPanel("Orders & Distribution page content will be added here.");
    }

private void showSettings() {
    // Clear the existing content and set up new content for the Settings page
    updateMainPanel("Settings Page content will be added here.");

    // Create components for the Change Password section
    JLabel changePasswordLabel = new JLabel("Change Password");
    changePasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JTextField currentPasswordField = new JTextField(20);
    JPasswordField newPasswordField = new JPasswordField(20);
    JPasswordField confirmPasswordField = new JPasswordField(20);
    JButton changePasswordButton = new JButton("Update Password");

    // Styling for the password fields and button
    changePasswordButton.setBackground(new Color(34, 139, 34));
    changePasswordButton.setForeground(Color.WHITE);
    changePasswordButton.setFocusPainted(false);
    changePasswordButton.setPreferredSize(new Dimension(150, 40));

    // Action listener for password update
    changePasswordButton.addActionListener(e -> {
        String currentPassword = currentPasswordField.getText();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newPassword.equals(confirmPassword)) {
            if (updatePasswordInDatabase(currentPassword, newPassword)) {
                JOptionPane.showMessageDialog(null, "Password updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Current password is incorrect.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Passwords do not match.");
        }
    });

    // Create components for Notification Preferences
    JLabel notificationLabel = new JLabel("Notification Preferences");
    notificationLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JCheckBox enableNotificationsCheckBox = new JCheckBox("Enable Notifications");
    JButton savePreferencesButton = new JButton("Save Preferences");

    // Styling for the button
    savePreferencesButton.setBackground(new Color(34, 139, 34));
    savePreferencesButton.setForeground(Color.WHITE);
    savePreferencesButton.setFocusPainted(false);
    savePreferencesButton.setPreferredSize(new Dimension(150, 40));

    // Action listener for saving preferences
    savePreferencesButton.addActionListener(e -> {
        boolean notificationsEnabled = enableNotificationsCheckBox.isSelected();
        // Execute SQL query to update notification preference in the database
        updateNotificationPreferencesInDatabase(notificationsEnabled);
        JOptionPane.showMessageDialog(null, "Notification preferences saved.");
    });

    // Layout the components (using JPanel and BoxLayout for vertical stacking)
    JPanel settingsPanel = new JPanel();
    settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
    settingsPanel.setBackground(new Color(240, 240, 240)); // Light gray background
    settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

    // Add Change Password components
    settingsPanel.add(changePasswordLabel);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(new JLabel("Current Password"));
    settingsPanel.add(currentPasswordField);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(new JLabel("New Password"));
    settingsPanel.add(newPasswordField);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(new JLabel("Confirm New Password"));
    settingsPanel.add(confirmPasswordField);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(changePasswordButton);
    settingsPanel.add(Box.createVerticalStrut(20)); // Spacer between sections

    // Add Notification Preferences components
    settingsPanel.add(notificationLabel);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(enableNotificationsCheckBox);
    settingsPanel.add(Box.createVerticalStrut(10)); // Spacer
    settingsPanel.add(savePreferencesButton);

    // Set the main panel to the settings page content
    updateMainPanel(settingsPanel);
}

private boolean updatePasswordInDatabase(String currentPassword, String newPassword) {
    // Replace the following logic with real database logic
    // Ensure to validate the current password first, then update the password in the database.

    try {
        // Assume you already have a database connection (e.g., using JDBC)
        String storedPassword = getStoredPasswordFromDatabase();  // Retrieve stored password for comparison
        if (currentPassword.equals(storedPassword)) {
            String hashedNewPassword = hashPassword(newPassword); // Hash the new password before storing
            // Perform the SQL query to update the password in the database
            String updatePasswordQuery = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updatePasswordQuery)) {
                stmt.setString(1, hashedNewPassword);
                stmt.setString(2, getUsernameFromSession()); // Get the username of the current user
                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0; // Return true if the update was successful
            }
        } else {
            return false; // Return false if the current password is incorrect
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

private String getStoredPasswordFromDatabase() {
    // Replace with real database query to retrieve the stored password
    return "storedPassword";  // Placeholder for actual stored password
}

private String hashPassword(String password) {
    // Implement a secure hash function like bcrypt or PBKDF2
    // For simplicity, using a basic hash (e.g., MD5 or SHA-256) is not recommended for real applications.
    return Integer.toHexString(password.hashCode()); // Example, do not use in production
}

private String getUsernameFromSession() {
    // Replace with actual logic to get the logged-in user's username from the session or context
    return "currentUser";  // Placeholder for actual username
}

private void updateNotificationPreferencesInDatabase(boolean notificationsEnabled) {
    // Update the notification preferences in the database
    String updatePreferencesQuery = "UPDATE users SET notifications_enabled = ? WHERE username = ?";
    try (PreparedStatement stmt = connection.prepareStatement(updatePreferencesQuery)) {
        stmt.setBoolean(1, notificationsEnabled);
        stmt.setString(2, getUsernameFromSession()); // Get the username of the current user
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void updateMainPanel(JPanel panelContent) {
    // Assuming you have a method to update the main content of your window
    mainPanel.removeAll();  // Clear previous content
    mainPanel.add(panelContent);  // Add the new content (Settings page)
    mainPanel.revalidate();  // Revalidate to apply changes
    mainPanel.repaint();  // Repaint the panel to reflect new content
}


    // Helper method to update main panel with a message
    private void updateMainPanel(String message) {
        mainPanel.removeAll();
        JTextArea textArea = new JTextArea(message);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setEditable(false); // Make the text area non-editable
        textArea.setBackground(Color.WHITE);
        mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
