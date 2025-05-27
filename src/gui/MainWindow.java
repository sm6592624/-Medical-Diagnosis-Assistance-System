package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import src.logic.DiagnosisLogic;

/**
 * Main application window for the Medical Diagnosis Assistant.
 */
@SuppressWarnings("unused")
public class MainWindow extends JFrame {
    private JList<String> symptomList;
    private JTextArea resultArea;

    private final String[] symptoms = {
        "Fever", "Cough", "Headache", "Nausea", "Chest Pain", "Fatigue", "Sore Throat", "Shortness of Breath", "Pale Skin", "Vomiting", "Diarrhea", "Runny Nose", "Sneezing", "Itchy Eyes"
    };

    /**
     * Constructs the main window UI.
     */
    public MainWindow() {
        setTitle("Medical Diagnosis Assistant");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Enable maximize button for consistency with LoginWindow
        setLayout(new BorderLayout(10, 10));

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
        Color resultBg = new Color(255, 255, 255);

        JPanel topPanel = new JPanel(new GridBagLayout()) {
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
        topPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Medical Diagnosis Assistant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(accentColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(titleLabel, gbc);

        JLabel label = new JLabel("Select Symptoms:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(label, gbc);

        symptomList = new JList<>(symptoms);
        symptomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        symptomList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        symptomList.setBackground(fieldBg);
        symptomList.setBorder(BorderFactory.createLineBorder(accentColor, 1));
        JScrollPane scrollPane = new JScrollPane(symptomList);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        topPanel.add(scrollPane, gbc);

        JButton diagnoseButton = new JButton("Diagnose");
        diagnoseButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        diagnoseButton.setBackground(buttonColor);
        diagnoseButton.setForeground(buttonText);
        diagnoseButton.setFocusPainted(false);
        diagnoseButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)));
        diagnoseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        diagnoseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                diagnoseButton.setBackground(accentColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                diagnoseButton.setBackground(buttonColor);
            }
        });
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(diagnoseButton, gbc);

        JButton saveButton = new JButton("Save Report");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveButton.setBackground(accentColor);
        saveButton.setForeground(buttonText);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(buttonColor, 2),
            BorderFactory.createEmptyBorder(6, 18, 6, 18)));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(buttonColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(accentColor);
            }
        });
        gbc.gridx = 2; gbc.gridy = 3; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(saveButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        resultArea.setBackground(resultBg);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        JScrollPane resultPane = new JScrollPane(resultArea);
        resultPane.setPreferredSize(new Dimension(450, 180));
        add(resultPane, BorderLayout.CENTER);

        diagnoseButton.addActionListener(e -> {
            List<String> selected = symptomList.getSelectedValuesList();
            if (selected == null || selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one symptom.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String diagnosis = DiagnosisLogic.diagnose(selected);
            resultArea.setText(diagnosis);
            resultArea.setCaretPosition(0); // Scroll to top
            DiagnosisLogic.saveHistory(selected, diagnosis);
        });

        saveButton.addActionListener(e -> {
            String report = resultArea.getText();
            if (report == null || report.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No diagnosis to save.", "Save Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Path reportPath = Paths.get("data", "diagnosis_report.txt");
            try {
                Files.createDirectories(reportPath.getParent());
                Files.write(reportPath, report.getBytes());
                JOptionPane.showMessageDialog(this, "Report saved to " + reportPath.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}