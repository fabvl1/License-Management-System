package visual.Reports;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import model.Driver;
import model.License;
import model.Violation;
import services.DriverService;
import services.LicenseService;
import services.ViolationService;

import static report_models.DriverInfoReportModel.saveDriverInfoReport;
import static report_models.SaveLocation.askSaveLocation;

/**
 * Report: Driver Information Sheet.
 * Shows the selected driver's info, licenses, and recorded violations.
 */
public class DriverInfoReport extends JPanel {
	  private static final long serialVersionUID = 1L;

    private static final DriverService driverService = new DriverService();
    private static final LicenseService licenseService = new LicenseService();
    private static final ViolationService violationService = new ViolationService();

    /**
     * Opens the Driver Information dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showDialog(Component parent) {
        String driverId = JOptionPane.showInputDialog(parent, "Enter driver's ID (Document number):", "Driver ID", JOptionPane.QUESTION_MESSAGE);
        if (driverId == null || driverId.trim().isEmpty()) return;

        Driver driver = driverService.getById(driverId.trim());
        if (driver == null || driver.getDriverId() == null || driver.getDriverId().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Driver not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<License> licenses = licenseService.getLicensesByDriver(driverId.trim());

        // License status: example logic - show the first license status found (improve as needed)
        String licenseStatus;
        if (!licenses.isEmpty()) {
            licenseStatus = licenses.get(0).getLicenseType() + " (" + licenses.get(0).getLicenseCode() + ")";
        }else{
            licenseStatus = "No licenses";
        }

        // Gather all violations for all licenses
        java.util.List<Violation> allViolations = new java.util.ArrayList<>();
        for (License lic : licenses) {
            List<Violation> v = violationService.getViolationsByLicense(lic.getLicenseCode());
            if (v != null) allViolations.addAll(v);
        }

        // Data for licenses table
        Object[][] licenseRows = new Object[licenses.size()][3];
        for (int i = 0; i < licenses.size(); i++) {
            License lic = licenses.get(i);
            licenseRows[i][0] = lic.getLicenseType();
            licenseRows[i][1] = lic.getIssueDate();
            licenseRows[i][2] = lic.getExpirationDate();
        }
        // Data for violations table
        Object[][] violationRows = new Object[allViolations.size()][3];
        for (int i = 0; i < allViolations.size(); i++) {
            Violation v = allViolations.get(i);
            violationRows[i][0] = v.getViolationType();
            violationRows[i][1] = v.getDate();
            violationRows[i][2] = v.getDeductedPoints();
        }

        // Info fields
        String fullName = driver.getFirstName() + " " + driver.getLastName();
        String address = driver.getAddress();
        String phone = driver.getPhoneNumber();

        // Create and show dialog
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Driver Information", Dialog.ModalityType.APPLICATION_MODAL);
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
        addRow(infoPanel, "Name:", fullName, gbc, y++);
        addRow(infoPanel, "ID Number:", driverId, gbc, y++);
        addRow(infoPanel, "Address:", address, gbc, y++);
        addRow(infoPanel, "Phone:", phone, gbc, y++);
        addRow(infoPanel, "License Status:", licenseStatus, gbc, y++);

        // Licenses issued
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel licLabel = new JLabel("Licenses issued:");
        licLabel.setFont(licLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(licLabel, gbc); y++;
        gbc.gridwidth = 1;

        String[] licCols = {"Type", "Issued", "Expires"};
        JTable licTable = new JTable(licenseRows, licCols);
        licTable.setEnabled(false);
        licTable.setRowHeight(22);
        JScrollPane licScroll = new JScrollPane(licTable);
        licScroll.setPreferredSize(new Dimension(350, Math.min(licenses.size() * 28 + 24, 120)));
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        infoPanel.add(licScroll, gbc); y++;
        gbc.gridwidth = 1;

        // Registered violations
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel infLabel = new JLabel("Registered violations:");
        infLabel.setFont(infLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(infLabel, gbc); y++;
        gbc.gridwidth = 1;

        String[] infCols = {"Type", "Date", "Points"};
        JTable infTable = new JTable(violationRows, infCols);
        infTable.setEnabled(false);
        infTable.setRowHeight(22);
        JScrollPane infScroll = new JScrollPane(infTable);
        infScroll.setPreferredSize(new Dimension(350, Math.min(allViolations.size() * 28 + 24, 120)));
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        infoPanel.add(infScroll, gbc); y++;

        dialog.add(infoPanel, BorderLayout.CENTER);

        // Export button (disabled for now)
        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(true);
        exportButton.addActionListener(e -> {
            try {
                String filePath = askSaveLocation(dialog);
                if (filePath != null&&!filePath.trim().isEmpty()) {
                    saveDriverInfoReport(
                            filePath,
                            fullName,
                            driverId,
                            address,
                            phone,
                            licenseStatus,
                            licenses,
                            allViolations
                    );
                    JOptionPane.showMessageDialog(dialog, "Report exported successfully!", "Export PDF", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error generating report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        });
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