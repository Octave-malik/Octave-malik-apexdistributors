package com.agriapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IndexPage extends JFrame {

    public IndexPage() {
        // Set the title and default close operation
        setTitle("Agricultural Input Distribution System - Apex Distributors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set the background image
        setContentPane(new BackgroundPanel());

        // Create the main panel for layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // Create the navigation panel (Navbar)
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        navPanel.setBackground(new Color(0, 51, 102)); // Dark blue color for the navbar

        // Add the logo to the navigation bar
        JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/logo.png")));
        navPanel.add(logoLabel);

        // Add "Apex Distributors" text below the logo
        JLabel logoText = new JLabel("Apex Distributors");
        logoText.setFont(new Font("Serif", Font.ITALIC, 16));
        logoText.setForeground(Color.WHITE);
        navPanel.add(logoText);

        // Create navigation buttons with improved styles
        JButton btnHome = createNavButton("Home");
        JButton btnAboutUs = createNavButton("About Us");
        JButton btnServices = createNavButton("Services");
        JButton btnContact = createNavButton("Contact");
        JButton btnLogin = createNavButton("Login");

        // Add buttons to the navigation panel
        navPanel.add(btnHome);
        navPanel.add(btnAboutUs);
        navPanel.add(btnServices);
        navPanel.add(btnContact);
        navPanel.add(Box.createHorizontalStrut(200)); // Space before the Login button
        navPanel.add(btnLogin);

        // Add the navigation panel to the main panel
        mainPanel.add(navPanel, BorderLayout.NORTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Set the frame to be visible
        setVisible(true);

        // Action listeners for navigation buttons
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Welcome to the Agricultural Input Distribution System!");
            }
        });

        btnAboutUs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "About Us:\nApex Distributors provides quality agricultural inputs to empower farmers and boost productivity.");
            }
        });

        btnServices.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Our Services:\n- Fertilizer Supply\n- Seed Distribution\n- Agricultural Consultancy");
            }
        });

        btnContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Contact Us:\nPhone: 123-456-7890\nEmail: info@apexdistributors.com");
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose(); // Close the current IndexPage
            }
        });
    }

    // Helper method to create styled navigation buttons
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Inner class to handle the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Load the background image
            backgroundImage = new ImageIcon(getClass().getResource("/resources/background5.jpg")).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new IndexPage();
    }
}