package visual.Reports;

import javax.swing.*;

import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import model.Violation;
import model.License;
import model.Driver;
import services.ViolationService;
import services.LicenseService;
import services.DriverService;

import static report_models.InfractionsInPeriodReportModel.saveInfractionsInPeriod;
import static report_models.SaveLocation.askSaveLocation;

/**
 * Report: Registered Infractions in a Period.
 * Shows infractions registered in a given period, ordered by infraction date.
 */
public class InfractionsInPeriodReport extends JPanel {
	  private static final long serialVersionUID = 1L;
    private static final ViolationService violationService = new ViolationService();
    private static final LicenseService licenseService = new LicenseService();
    private static final DriverService driverService = new DriverService();

    /**
     * Opens the Infractions in Period report dialog.
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

        // Query all violations and filter by date in period
        List<Violation> allViolations = violationService.getAll();
        List<Violation> filtered = new ArrayList<>();
        for (Violation v : allViolations) {
            Date infractionDate = v.getDate();
            if (!infractionDate.before(startDate) && !infractionDate.after(endDate)) {
                filtered.add(v);
            }
        }
        // Sort by infraction date
        filtered.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        // Prepare data for table
        Object[][] rows = new Object[filtered.size()][7];
        for (int i = 0; i < filtered.size(); i++) {
            Violation v = filtered.get(i);
            // Get license and driver info
            String driverName = "(unknown)";
          
            if (v.getLicenseCode() != null) {
                License lic = licenseService.getById(v.getLicenseCode());
                if (lic != null) {
                  
                    Driver drv = driverService.getById(lic.getDriverId());
                    if (drv != null) {
                        driverName = drv.getFirstName() + " " + drv.getLastName();
                        
                    } 
                }
            }
            rows[i][0] = v.getViolationCode();
            rows[i][1] = driverName;
            rows[i][2] = v.getViolationType();
            rows[i][3] = v.getDate();
            rows[i][4] = v.getLocation();
            rows[i][5] = v.getDeductedPoints();
            rows[i][6] = v.isPaid() ? "Paid" : "Pending";
        }

        // Show dialog with table
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Infractions Registered in Period", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(18, 18));

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = {"Infraction Code", "Driver Name", "Infraction Type", "Infraction Date", "Place", "Points Deducted", "Status"};
        JTable table = new JTable(rows, columns);
        table.setEnabled(false);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(900, Math.min(filtered.size() * 28 + 24, 350)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        dialog.add(tablePanel, BorderLayout.CENTER);


        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(true);
        exportButton.addActionListener(e -> {
            try {
                String filePath = askSaveLocation(dialog);
                if (filePath != null&&!filePath.trim().isEmpty()) {
                    saveInfractionsInPeriod(filePath, columns, rows);
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