package visual.Reports;


import javax.swing.*;
import java.awt.*;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


import model.Center;
import services.CenterService;

/**
 * Report: Center Information Sheet.
 * Shows general information for the only center in the system.
 */
public class CenterInfoReport extends JPanel {
	  private static final long serialVersionUID = 1L;

    private static final CenterService centerService = new CenterService();

    /**
     * Opens the Center Information dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showDialog(Component parent) {
        // Assume there is only one center, get it
        List<Center> centers = centerService.getAll();
        if (centers.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No center found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Center center = centers.get(0);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Center Information", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(18, 18));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));
        infoPanel.setBackground(UIManager.getColor("Panel.background"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addRow(infoPanel, "Center Name:", center.getCenterName(), gbc, y++);
        addRow(infoPanel, "Postal Address:", center.getPostalAddress(), gbc, y++);

        // Logo
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        infoPanel.add(new JLabel("Logo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(120, 120));
        logoLabel.setBorder(BorderFactory.createEtchedBorder());

        byte[] logoBytes = center.getLogo();
        if (logoBytes != null && logoBytes.length > 0) {
             try {
                ImageIcon icon = new ImageIcon(logoBytes);
                // Optionally scale
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                // If corrupted image, skip
            }
        }
        infoPanel.add(logoLabel, gbc);
        y++;

        addRow(infoPanel, "Phone:", center.getPhoneNumber(), gbc, y++);
        addRow(infoPanel, "Email:", center.getContactEmail(), gbc, y++);
        addRow(infoPanel, "General Director:", center.getGeneralDirector(), gbc, y++);
        addRow(infoPanel, "HR Manager:", center.getHrManager(), gbc, y++);
        addRow(infoPanel, "Accounting Manager:", center.getAccountingManager(), gbc, y++);
        addRow(infoPanel, "Union Secretary:", center.getUnionSecretary(), gbc, y++);

        dialog.add(infoPanel, BorderLayout.CENTER);

        // Export button (disabled for now)
        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(false); // no action yet
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void addRow(JPanel panel, String label, String value, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JLabel(value), gbc);
    }
}