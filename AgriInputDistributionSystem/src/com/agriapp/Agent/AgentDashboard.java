package com.agriapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.sql.*;

public class AgentDashboard extends JFrame {
    private String username;
    private static final Dimension BUTTON_SIZE = new Dimension(180, 50); // Fixed size for all buttons
    private JPanel mainPanel; // Declare mainPanel to update its content dynamically

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/agricultural_input_distribution";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "nzioki";

    public AgentDashboard(String username) {
        this.username = username;

        // Set up the dashboard window
        setTitle("Agent Dashboard - Agricultural Input Distribution System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for the navigation menu
        JPanel sidePanel = createSidePanel();

        // Initialize the main content panel (dynamic content)
        mainPanel = createMainPanel();

        // Set up layout and add panels to the frame
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidePanel() {
        // Create the side navigation panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBackground(new Color(60, 63, 65));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create navigation buttons
        JButton btnDashboard = createNavButton("Dashboard");
        JButton btnStock = createNavButton("Input Stock");
        JButton btnDistribution = createNavButton("Distribution");
        JButton btnSuppliers = createNavButton("Suppliers");
        JButton btnReports = createNavButton("Reports");
        JButton btnProfile = createNavButton("Profile/Settings");
        JButton btnLogout = createLogoutButton();

        // Add buttons to the panel
        panel.add(btnDashboard);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnStock);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnDistribution);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSuppliers);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnReports);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnProfile);
        panel.add(Box.createVerticalGlue()); // Pushes logout button to the bottom
        panel.add(Box.createVerticalStrut(20)); // Space before logout button
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
                    case "Input Stock":
                        showInputStock();
                        break;
                    case "Distribution":
                        showDistribution();
                        break;
                    case "Suppliers":
                        showSuppliers();
                        break;
                    case "Reports":
                        showReports();
                        break;
                    case "Profile/Settings":
                        showProfileSettings();
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

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Placeholder for dynamic content
        JLabel placeholder = new JLabel("Select an option from the sidebar", SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 16));
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }

    // Method to show Dashboard content with tables
    private void showDashboard() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Available Inputs Table
        dashboardPanel.add(createAvailableInputsTable());
        dashboardPanel.add(Box.createVerticalStrut(20)); // Space between sections

        // Distribution History Table
        dashboardPanel.add(createDistributionHistoryTable());
        dashboardPanel.add(Box.createVerticalStrut(20)); // Space between sections

        // Notifications Table
        dashboardPanel.add(createNotificationsTable());

        // Update main panel with new content
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(dashboardPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Create Available Inputs Table
    private JScrollPane createAvailableInputsTable() {
        String[] columns = {"Input Name", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, quantity FROM Inputs");

            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                model.addRow(new Object[]{name, quantity});
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Inputs"));

        return scrollPane;
    }

    // Create Distribution History Table
    private JScrollPane createDistributionHistoryTable() {
        String[] columns = {"Input Name", "Quantity", "Recipient", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT i.name, d.quantity, d.recipient_name, d.date, d.status " +
                "FROM Distributions d " +
                "JOIN Inputs i ON d.input_id = i.input_id " +
                "ORDER BY d.date DESC LIMIT 5"
            );

            while (rs.next()) {
                String inputName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                String recipient = rs.getString("recipient_name");
                Date date = rs.getDate("date");
                String status = rs.getString("status");
                model.addRow(new Object[]{inputName, quantity, recipient, date, status});
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recent Distribution History"));

        return scrollPane;
    }

    // Create Notifications Table
    private JScrollPane createNotificationsTable() {
        String[] columns = {"Input Name", "Stock"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, quantity FROM Inputs WHERE quantity < 50");

            while (rs.next()) {
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                model.addRow(new Object[]{name, quantity});
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Low Stock Notifications"));

        return scrollPane;
    }

    private void showInputStock() {
    // Create the input stock panel
    JPanel inputStockPanel = new JPanel();
    inputStockPanel.setLayout(new BorderLayout());
    inputStockPanel.setBackground(Color.WHITE);
    inputStockPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Add the title label for Input Stock
    JLabel inputStockLabel = new JLabel("Available Agricultural Inputs:");
    inputStockLabel.setFont(new Font("Arial", Font.BOLD, 16));
    inputStockPanel.add(inputStockLabel, BorderLayout.NORTH);

    // Create a table to display the available inputs
    String[] columns = {"Input Name", "Quantity Available", "Price", "Supplier"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable inputStockTable = new JTable(tableModel);
    inputStockTable.setFillsViewportHeight(true);

    // Add the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(inputStockTable);
    inputStockPanel.add(scrollPane, BorderLayout.CENTER);

    try {
        // Query for available inputs and join the Inputs and Suppliers tables
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        String query = "SELECT i.name, i.quantity, i.price, s.name AS supplier " +
                       "FROM Inputs i " +
                       "LEFT JOIN Suppliers s ON i.supplier_id = s.supplier_id";
        ResultSet rs = stmt.executeQuery(query);

        // Populate the table with data from the database
        while (rs.next()) {
            String name = rs.getString("name");
            int quantity = rs.getInt("quantity");
            double price = rs.getDouble("price");
            String supplier = rs.getString("supplier");

            // Add row to the table
            tableModel.addRow(new Object[]{name, quantity, price, supplier});
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Update the main panel content
    mainPanel.removeAll();
    mainPanel.add(inputStockPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

    private void showDistribution() {
    // Create the distribution panel
    JPanel distributionPanel = new JPanel();
    distributionPanel.setLayout(new BorderLayout());
    distributionPanel.setBackground(Color.WHITE);
    distributionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Add the title label for Distributions
    JLabel distributionLabel = new JLabel("Agent Distribution Activities:");
    distributionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    distributionPanel.add(distributionLabel, BorderLayout.NORTH);

    // Create a table to display the distribution activities
    String[] columns = {"Input Name", "Quantity Distributed", "Recipient Name", "Date", "Status"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable distributionTable = new JTable(tableModel);
    distributionTable.setFillsViewportHeight(true);

    // Add the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(distributionTable);
    distributionPanel.add(scrollPane, BorderLayout.CENTER);

    try {
        // Query for the distribution activities of the logged-in agent
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        // Assuming the agent_id is available (it can be retrieved from the session or logged-in agent info)
        int agentId = 1;  // Replace this with the actual agent ID dynamically
        String query = "SELECT i.name AS input_name, d.quantity, d.recipient_name, d.date, d.status " +
                       "FROM Distributions d " +
                       "JOIN Inputs i ON d.input_id = i.input_id " +
                       "WHERE d.agent_id = " + agentId;
        ResultSet rs = stmt.executeQuery(query);

        // Populate the table with data from the database
        while (rs.next()) {
            String inputName = rs.getString("input_name");
            int quantity = rs.getInt("quantity");
            String recipientName = rs.getString("recipient_name");
            Date date = rs.getDate("date");
            String status = rs.getString("status");

            // Add row to the table
            tableModel.addRow(new Object[]{inputName, quantity, recipientName, date, status});
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Update the main panel content
    mainPanel.removeAll();
    mainPanel.add(distributionPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}


    private void showSuppliers() {
    // Create the suppliers panel
    JPanel suppliersPanel = new JPanel();
    suppliersPanel.setLayout(new BorderLayout());
    suppliersPanel.setBackground(Color.WHITE);
    suppliersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Add the title label for Suppliers
    JLabel suppliersLabel = new JLabel("Supplier Information:");
    suppliersLabel.setFont(new Font("Arial", Font.BOLD, 16));
    suppliersPanel.add(suppliersLabel, BorderLayout.NORTH);

    // Create a table to display the supplier details
    String[] columns = {"Supplier Name", "Contact Info", "Delivery Schedule", "Rating"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable suppliersTable = new JTable(tableModel);
    suppliersTable.setFillsViewportHeight(true);

    // Add the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(suppliersTable);
    suppliersPanel.add(scrollPane, BorderLayout.CENTER);

    try {
        // Query for all suppliers from the database
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        String query = "SELECT name, contact_info, delivery_schedule, rating FROM Suppliers";
        ResultSet rs = stmt.executeQuery(query);

        // Populate the table with data from the database
        while (rs.next()) {
            String name = rs.getString("name");
            String contactInfo = rs.getString("contact_info");
            String deliverySchedule = rs.getString("delivery_schedule");
            float rating = rs.getFloat("rating");

            // Add row to the table
            tableModel.addRow(new Object[]{name, contactInfo, deliverySchedule, rating});
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Update the main panel content
    mainPanel.removeAll();
    mainPanel.add(suppliersPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}


    private void showReports() {
    // Create the reports panel
    JPanel reportsPanel = new JPanel();
    reportsPanel.setLayout(new BorderLayout());
    reportsPanel.setBackground(Color.WHITE);
    reportsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Add the title label for Reports
    JLabel reportsLabel = new JLabel("Generated Reports:");
    reportsLabel.setFont(new Font("Arial", Font.BOLD, 16));
    reportsPanel.add(reportsLabel, BorderLayout.NORTH);

    // Create a table to display the report details
    String[] columns = {"Report ID", "Type", "Generated On", "Description"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
    JTable reportsTable = new JTable(tableModel);
    reportsTable.setFillsViewportHeight(true);

    // Add the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(reportsTable);
    reportsPanel.add(scrollPane, BorderLayout.CENTER);

    try {
        // Query for reports generated by the logged-in agent
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        int agentId = 1;  // Replace with the actual logged-in agent ID dynamically
        String query = "SELECT r.report_id, r.type, r.generated_on, r.description " +
                       "FROM Reports r " +
                       "JOIN Users u ON r.generated_by = u.user_id " +
                       "JOIN Agents a ON u.user_id = a.user_id " +
                       "WHERE a.agent_id = " + agentId;
        ResultSet rs = stmt.executeQuery(query);

        // Populate the table with data from the database
        while (rs.next()) {
            int reportId = rs.getInt("report_id");
            String type = rs.getString("type");
            Date generatedOn = rs.getDate("generated_on");
            String description = rs.getString("description");

            // Add row to the table
            tableModel.addRow(new Object[]{reportId, type, generatedOn, description});
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Add a listener to open a detailed report view when a row is selected
    reportsTable.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && reportsTable.getSelectedRow() != -1) {
            int selectedReportId = (int) tableModel.getValueAt(reportsTable.getSelectedRow(), 0);
            showReportDetails(selectedReportId);
        }
    });

    // Update the main panel content
    mainPanel.removeAll();
    mainPanel.add(reportsPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

private void showReportDetails(int reportId) {
    // Create the report details panel
    JPanel reportDetailsPanel = new JPanel();
    reportDetailsPanel.setLayout(new BorderLayout());
    reportDetailsPanel.setBackground(Color.WHITE);
    reportDetailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Add the title label for Report Details
    JLabel reportDetailsLabel = new JLabel("Report Details:");
    reportDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
    reportDetailsPanel.add(reportDetailsLabel, BorderLayout.NORTH);

    // Add a text area to display the report description
    JTextArea reportDescriptionTextArea = new JTextArea();
    reportDescriptionTextArea.setEditable(false);
    reportDescriptionTextArea.setLineWrap(true);
    reportDescriptionTextArea.setWrapStyleWord(true);
    reportDescriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
    JScrollPane descriptionScrollPane = new JScrollPane(reportDescriptionTextArea);
    reportDetailsPanel.add(descriptionScrollPane, BorderLayout.CENTER);

    try {
        // Query to get the full report details by report ID
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        String query = "SELECT description FROM Reports WHERE report_id = " + reportId;
        ResultSet rs = stmt.executeQuery(query);

        // Set the report description into the text area
        if (rs.next()) {
            String description = rs.getString("description");
            reportDescriptionTextArea.setText(description);
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Update the main panel content with the report details view
    mainPanel.removeAll();
    mainPanel.add(reportDetailsPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

// Method to show profile settings
private void showProfileSettings() {
    // Create the profile settings panel
    JPanel profileSettingsPanel = new JPanel();
    profileSettingsPanel.setLayout(new BorderLayout());
    profileSettingsPanel.setBackground(new Color(247, 247, 247)); // Light gray background
    profileSettingsPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

    // Add the title label for Profile Settings
    JLabel profileSettingsLabel = new JLabel("Profile Settings");
    profileSettingsLabel.setFont(new Font("Arial", Font.BOLD, 20));
    profileSettingsLabel.setForeground(new Color(0, 102, 204)); // Blue color for title
    profileSettingsPanel.add(profileSettingsLabel, BorderLayout.NORTH);

    // Create the form panel to edit personal information and password
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Labels and text fields for personal information
    JLabel firstNameLabel = new JLabel("First Name:");
    JTextField firstNameTextField = new JTextField(20);

    JLabel lastNameLabel = new JLabel("Last Name:");
    JTextField lastNameTextField = new JTextField(20);

    JLabel emailLabel = new JLabel("Email:");
    JTextField emailTextField = new JTextField(20);

    JLabel phoneLabel = new JLabel("Phone Number:");
    JTextField phoneTextField = new JTextField(20);

    JLabel addressLabel = new JLabel("Address:");
    JTextField addressTextField = new JTextField(20);

    // Password fields for changing the password
    JLabel passwordLabel = new JLabel("New Password:");
    JPasswordField passwordField = new JPasswordField(20);

    // Show password checkbox
    JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    showPasswordCheckBox.setBackground(Color.WHITE);

    // Add components to the form panel using GridBagLayout
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(firstNameLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(firstNameTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    formPanel.add(lastNameLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(lastNameTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    formPanel.add(emailLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(emailTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    formPanel.add(phoneLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(phoneTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    formPanel.add(addressLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(addressTextField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    formPanel.add(passwordLabel, gbc);
    gbc.gridx = 1;
    formPanel.add(passwordField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    formPanel.add(showPasswordCheckBox, gbc); // Add the show password checkbox

    // Add form panel to the profile settings panel
    profileSettingsPanel.add(formPanel, BorderLayout.CENTER);

    // Button panel for Save and Cancel actions
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

    JButton saveButton = new JButton("Save Changes");
    saveButton.setBackground(new Color(0, 102, 204)); // Blue color for button
    saveButton.setForeground(Color.WHITE);
    saveButton.setFont(new Font("Arial", Font.BOLD, 14));
    saveButton.setPreferredSize(new Dimension(150, 40));
    saveButton.setFocusPainted(false);
    saveButton.setBorderPainted(false);
    saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.setBackground(Color.GRAY); // Gray color for cancel button
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
    cancelButton.setPreferredSize(new Dimension(150, 40));
    cancelButton.setFocusPainted(false);
    cancelButton.setBorderPainted(false);
    cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Add action listeners to the buttons
    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Validate input fields
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String phone = phoneTextField.getText();
            String address = addressTextField.getText();
            String newPassword = new String(passwordField.getPassword());

            // Handle updating the user profile in the database
            updateUserProfile(firstName, lastName, email, phone, address, newPassword);
        }
    });

    cancelButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Close the settings window (or simply return to the previous screen)
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    });

    // Add buttons to the button panel
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    profileSettingsPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Add functionality to show/hide password
    showPasswordCheckBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        }
    });

    // Update the main panel content with the profile settings view
    mainPanel.removeAll();
    mainPanel.add(profileSettingsPanel, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();
}

// Make updateUserProfile method public to ensure accessibility
public void updateUserProfile(String firstName, String lastName, String email, String phone, String address, String newPassword) {
    // Assume we have the current logged-in user's user_id, for example:
    int loggedInUserId = 2; // Replace with actual logged-in user ID
    
    // Build the SQL update query
    String query = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? WHERE user_id = ?";
    
    if (!newPassword.isEmpty()) {
        query = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ?, password = ? WHERE user_id = ?";
    }

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Set values in the prepared statement
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, phone);
        stmt.setString(5, address);

        if (!newPassword.isEmpty()) {
            stmt.setString(6, newPassword); // Save the password as entered (no hashing)
            stmt.setInt(7, loggedInUserId);
        } else {
            stmt.setInt(6, loggedInUserId);
        }

        // Execute the update query
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Profile updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Error updating profile.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    

    public static void main(String[] args) {
        // Sample username for demonstration purposes
        new AgentDashboard("Agent001");
    }
}