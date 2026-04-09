package visual.Reports;

import javax.swing.*;

import java.awt.*;
import java.sql.Date;

import java.util.List;
import java.util.ArrayList;
import model.Exam;
import model.Driver;
import model.AssociatedEntity;
import services.ExamService;
import services.DriverService;
import services.AssociatedEntityService;

import static report_models.ExamsInPeriodReportModel.saveExamsInPeriod;
import static report_models.SaveLocation.askSaveLocation;

/**
 * Report: Exams Taken in a Period.
 * Shows exams taken in a time period, ordered by exam date.
 */
public class ExamsInPeriodReport extends JPanel {
	  private static final long serialVersionUID = 1L;
    private static final ExamService examService = new ExamService();
    private static final DriverService driverService = new DriverService();
    private static final AssociatedEntityService entityService = new AssociatedEntityService();

    /**
     * Opens the Exams in Period report dialog.
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

        // Query exams in period, ordered by exam_date
        List<Exam> allExams = examService.getAll();
        List<Exam> filtered = new ArrayList<>();
        for (Exam exam : allExams) {
            Date examDate = exam.getExamDate();
            if (!examDate.before(startDate) && !examDate.after(endDate)) {
                filtered.add(exam);
            }
        }
        filtered.sort((a, b) -> a.getExamDate().compareTo(b.getExamDate()));

        // Prepare data for table
        Object[][] rows = new Object[filtered.size()][6];
        for (int i = 0; i < filtered.size(); i++) {
            Exam exam = filtered.get(i);
            Driver drv = driverService.getById(exam.getDriverId());
            String driverName = drv != null ? drv.getFirstName() + " " + drv.getLastName() : "(unknown)";
            AssociatedEntity entity = entityService.getById(exam.getEntityCode());
            String entityName = entity != null && entity.getEntityName() != null ? entity.getEntityName() : exam.getEntityCode();
            rows[i][0] = exam.getExamCode();
            rows[i][1] = driverName;
            rows[i][2] = exam.getExamType();
            rows[i][3] = exam.getExamDate();
            rows[i][4] = exam.getResult();
            rows[i][5] = entityName;
        }

        // Show dialog with table
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Exams Taken in Period", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(18, 18));

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = {"Exam Code", "Driver Name", "Exam Type", "Exam Date", "Result", "Entity"};
        JTable table = new JTable(rows, columns);
        table.setEnabled(false);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, Math.min(filtered.size() * 28 + 24, 350)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        dialog.add(tablePanel, BorderLayout.CENTER);


        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(true);
        exportButton.addActionListener(e -> {
            try {
                String filePath = askSaveLocation(dialog);
                if (filePath != null&&!filePath.trim().isEmpty()) {
                    saveExamsInPeriod(table, columns, filePath);
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