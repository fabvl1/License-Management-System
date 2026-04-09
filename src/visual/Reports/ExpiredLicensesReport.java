package visual.Reports;

import javax.swing.*;

import java.awt.*;
import java.sql.Date;

import java.util.List;
import java.util.ArrayList;
import model.License;
import model.Driver;
import services.LicenseService;
import services.DriverService;

import static report_models.ExpiredLicensesReportModel.saveExpiredLicensesReport;
import static report_models.SaveLocation.askSaveLocation;

/**
 * Report: Drivers with Expired Licenses in a Period.
 * Shows drivers with licenses expired in a given period, ordered by expiration date.
 */
public class ExpiredLicensesReport extends JPanel {
	  private static final long serialVersionUID = 1L;

    private static final LicenseService licenseService = new LicenseService();
    private static final DriverService driverService = new DriverService();

    /**
     * Opens the Expired Licenses report dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showDialog(Component parent) {
        // Ask for period (start and end date)
       
        String startInput = JOptionPane.showInputDialog(parent, "Enter start date (yyyy-MM-dd):", "Start Date", JOptionPane.QUESTION_MESSAGE);
        if (startInput == null || startInput.trim().isEmpty()) return;
        String endInput = JOptionPane.showInputDialog(parent, "Enter end date (yyyy-MM-dd):", "End Date", JOptionPane.QUESTION_MESSAGE);
        if (endInput == null || endInput.trim().isEmpty()) return;
        Date startDate, endDate;
        try {
            startDate = Date.valueOf(startInput.trim());
            endDate = Date.valueOf(endInput.trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Invalid date format. Use yyyy-MM-dd.", "Input error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Query all licenses and filter by expiration_date in period
        List<License> allLicenses = licenseService.getAll();
        List<License> filtered = new ArrayList<>();
        for (License lic : allLicenses) {
            Date exp = lic.getExpirationDate();
            if (!exp.before(startDate) && !exp.after(endDate)) {
                filtered.add(lic);
            }
        }
        // Sort by expiration date
        filtered.sort((a, b) -> a.getExpirationDate().compareTo(b.getExpirationDate()));

        // Prepare data for table
        Object[][] rows = new Object[filtered.size()][5];
        for (int i = 0; i < filtered.size(); i++) {
            License lic = filtered.get(i);
            Driver drv = driverService.getById(lic.getDriverId());
            String driverName = drv != null ? drv.getFirstName() + " " + drv.getLastName() : "(unknown)";
            String driverId = drv != null ? drv.getDriverId() : lic.getDriverId();
            rows[i][0] = driverName;
            rows[i][1] = driverId;
            rows[i][2] = lic.getLicenseType();
            rows[i][3] = lic.getExpirationDate();
            rows[i][4] = lic.isRenewed() ? "Renewed" : "Expired";
        }

        // Show dialog with table
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Drivers with Expired Licenses", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(18, 18));

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = {"Driver Name", "ID Number", "License Type", "Expiration Date", "Status"};
        JTable table = new JTable(rows, columns);
        table.setEnabled(false);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, Math.min(filtered.size() * 28 + 24, 350)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        dialog.add(tablePanel, BorderLayout.CENTER);


        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(true);
        exportButton.addActionListener(e -> {
            try {
                String filePath = askSaveLocation(dialog);
                if (filePath != null&&!filePath.trim().isEmpty()) {
                    saveExpiredLicensesReport(filePath, columns, rows);
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
}