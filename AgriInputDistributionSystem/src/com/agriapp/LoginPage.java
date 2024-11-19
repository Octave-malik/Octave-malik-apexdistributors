package com.agriapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public LoginPage() {
        // Set up the login page
        setTitle("Login - Agricultural Input Distribution System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set custom background color
        getContentPane().setBackground(new Color(33, 37, 41));

        // Create the main panel with rounded corners and gradient background
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setOpaque(false);

        // Set GridBagConstraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE); // Change label color
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setBackground(new Color(55, 63, 69)); // Dark field background
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE); // White caret
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(55, 63, 69));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Show password checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setForeground(Color.WHITE);
        showPasswordCheckBox.setBackground(new Color(33, 37, 41));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(showPasswordCheckBox, gbc);

        // Login button with rounded edges and hover effect
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        loginButton.setPreferredSize(new Dimension(200, 40));

        // Add hover effect for login button
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 84, 150)); // Darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 102, 204)); // Original color
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        // Action listener for password visibility toggle
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

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }

    // Method to authenticate user
    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Database connection
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultural_input_distribution", "root", "nzioki")) {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");

                // Immediately redirect to dashboard based on role
                if (role.equals("Agent")) {
                    new AgentDashboard(username).setVisible(true);
                    dispose(); // Close login page
                } else if (role.equals("Admin")) {
                    // You can define the admin dashboard in a similar way
                    JOptionPane.showMessageDialog(this, "Admin Dashboard is not implemented yet.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.");
        }
    }

    // Main method to run the login page
    public static void main(String[] args) {
        new LoginPage();
    }
}
