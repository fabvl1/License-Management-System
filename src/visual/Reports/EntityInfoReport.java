package visual.Reports;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.AssociatedEntity;
import services.AssociatedEntityService;

import static report_models.EntityInfoReportModel.saveEntityInfoReport;
import static report_models.SaveLocation.askSaveLocation;

/**
 * Report: Associated Entity Information Sheet.
 * Displays a list of registered associated entities; shows details of the selected one.
 */
public class EntityInfoReport extends JPanel {
	  private static final long serialVersionUID = 1L;

    private static final AssociatedEntityService entityService = new AssociatedEntityService();

    /**
     * Opens the Associated Entity Information dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showDialog(Component parent) {
        List<AssociatedEntity> entities = entityService.getAll();
        if (entities.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No associated entities found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If there's more than one entity, let the user select which one
        AssociatedEntity selected = null;
        if (entities.size() == 1) {
            selected = entities.get(0);
        } else {
            String[] options = entities.stream()
                    .map(e -> e.getEntityName() + " (" + e.getEntityType() + ")")
                    .toArray(String[]::new);
            int choice = JOptionPane.showOptionDialog(
                    parent,
                    "Select an associated entity to view details:",
                    "Select Associated Entity",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice >= 0) {
                selected = entities.get(choice);
            }
        }
        if (selected == null) return;

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Associated Entity Information", Dialog.ModalityType.APPLICATION_MODAL);
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
        addRow(infoPanel, "Entity Name:", selected.getEntityName(), gbc, y++);
        addRow(infoPanel, "Type:", selected.getEntityType(), gbc, y++);
        addRow(infoPanel, "Address:", selected.getAddress(), gbc, y++);
        addRow(infoPanel, "Phone:", selected.getPhoneNumber(), gbc, y++);
        addRow(infoPanel, "Email:", selected.getContactEmail(), gbc, y++);
        addRow(infoPanel, "Director:", selected.getDirectorName(), gbc, y++);

        dialog.add(infoPanel, BorderLayout.CENTER);


        JButton exportButton = new JButton("Export...");
        exportButton.setEnabled(true);
        AssociatedEntity finalSelected = selected;
        exportButton.addActionListener(e -> {
            try {
                String filePath = askSaveLocation(dialog);
                if (filePath != null&&!filePath.trim().isEmpty()) {
                    saveEntityInfoReport(finalSelected, filePath);
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