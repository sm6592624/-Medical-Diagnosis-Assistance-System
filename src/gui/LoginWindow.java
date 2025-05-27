package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login window for the Medical Diagnosis System.
 */
@SuppressWarnings("unused")
public class LoginWindow extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    /**
     * Constructs the login window UI.
     */
    public LoginWindow() {
        setTitle("Login - Medical Diagnosis");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set a modern, colorful look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Custom colors
        Color bgColor = new Color(245, 249, 255);
        Color accentColor = new Color(66, 133, 244);
        Color buttonColor = new Color(52, 168, 83);
        Color buttonText = Color.WHITE;
        Color fieldBg = new Color(232, 240, 254);
        Color labelColor = new Color(32, 33, 36);

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(245, 249, 255), w, h, new Color(200, 221, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Medical Diagnosis Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(accentColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userLabel, gbc);

        userField = new JTextField(15);
        userField.setBackground(fieldBg);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);

        passField = new JPasswordField(15);
        passField.setBackground(fieldBg);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(buttonText);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(accentColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(buttonColor);
            }
        });
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(loginButton, gbc);

        // Set loginButton as default for Enter key
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (username.equals("admin") && password.equals("admin")) {
                dispose();
                new MainWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });

        add(panel);
        setVisible(true);
    }
}