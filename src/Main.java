package src;

import javax.swing.UIManager;

/**
 * Entry point for the Enhanced Medical Diagnosis System.
 */
public class Main {
    public static void main(String[] args) {
        // Set a modern look and feel for better UI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        src.gui.LoginWindow loginWindow = new src.gui.LoginWindow();
        loginWindow.setExtendedState(javax.swing.JFrame.NORMAL); // Allow maximize
        loginWindow.setResizable(true); // Enable maximize button
    }
}