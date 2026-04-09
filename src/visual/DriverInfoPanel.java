package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;

/**
 * Panel de Reporte de Ficha de Conductor Determinado.
 */
public class DriverInfoPanel extends JPanel {
	 private static final long serialVersionUID = 1L;
    public DriverInfoPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel("Driver Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton showButton = new JButton("Show driver report...");
        showButton.addActionListener(e -> showDriverInfoDialog());

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(showButton);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Muestra el diálogo con la ficha del conductor.
     * Aquí debes conectar esto con la búsqueda real de un conductor (por DNI, por ejemplo).
     */
    private void showDriverInfoDialog() {
        // Simulación de datos del conductor y sus relaciones.
        String nombre = "Juan Pérez";
        String dni = "12345678";
        String direccion = "Av. Siempre Viva 123, Ciudad";
        String telefono = "+1 555-987654";
        String estadoLicencia = "Vigente";

        // Licencias
        Object[][] licencias = {
            {"A", "2022-01-15", "2027-01-14"},
            {"B", "2020-03-12", "2025-03-11"}
        };
        // Infracciones
        Object[][] infracciones = {
            {"Serious", "2024-06-10", 8},
            {"Minor", "2023-11-05", 6}
        };

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Driver Information", Dialog.ModalityType.APPLICATION_MODAL);
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
        addRow(infoPanel, "Name:", nombre, gbc, y++);
        addRow(infoPanel, "ID Number:", dni, gbc, y++);
        addRow(infoPanel, "Address:", direccion, gbc, y++);
        addRow(infoPanel, "Phone:", telefono, gbc, y++);
        addRow(infoPanel, "License Status:", estadoLicencia, gbc, y++);

        // Licencias emitidas
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel licLabel = new JLabel("Licenses issued:");
        licLabel.setFont(licLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(licLabel, gbc); y++;
        gbc.gridwidth = 1;

        String[] licCols = {"Type", "Issued", "Expires"};
        JTable licTable = new JTable(licencias, licCols);
        licTable.setEnabled(false);
        licTable.setRowHeight(22);
        JScrollPane licScroll = new JScrollPane(licTable);
        licScroll.setPreferredSize(new Dimension(300, Math.min(licencias.length * 28 + 24, 120)));
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        infoPanel.add(licScroll, gbc); y++;
        gbc.gridwidth = 1;

        // Infracciones registradas
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JLabel infLabel = new JLabel("Registered violations:");
        infLabel.setFont(infLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(infLabel, gbc); y++;
        gbc.gridwidth = 1;

        String[] infCols = {"Type", "Date", "Points"};
        JTable infTable = new JTable(infracciones, infCols);
        infTable.setEnabled(false);
        infTable.setRowHeight(22);
        JScrollPane infScroll = new JScrollPane(infTable);
        infScroll.setPreferredSize(new Dimension(300, Math.min(infracciones.length * 28 + 24, 120)));
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        infoPanel.add(infScroll, gbc); y++;

        dialog.add(infoPanel, BorderLayout.CENTER);

        // Exportar botón
        JButton exportButton = new JButton("Export...");
        exportButton.addActionListener((ActionEvent e) -> exportDriverInfoSimple(
                nombre, dni, direccion, telefono, estadoLicencia, licencias, infracciones
        ));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addRow(JPanel panel, String label, String value, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(new JLabel(value), gbc);
    }

    // Exportación simple a CSV o TXT (sin dependencias externas)
    private void exportDriverInfoSimple(String nombre, String dni, String direccion, String telefono, String estadoLic, Object[][] licencias, Object[][] infracciones) {
        String[] options = {"CSV (.csv)", "Text (.txt)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select the format to export:",
                "Export",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == -1) return;

        JFileChooser chooser = new JFileChooser();
        if (choice == 0)
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        else
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files", "txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        String ext = choice == 0 ? ".csv" : ".txt";
        if (!file.getName().toLowerCase().endsWith(ext)) {
            file = new File(file.getAbsolutePath() + ext);
        }

        try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
            if (choice == 0) { // CSV
                pw.println("Name,ID Number,Address,Phone,License Status");
                pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", nombre, dni, direccion, telefono, estadoLic);
                pw.println();
                pw.println("Licenses issued");
                pw.println("Type,Issued,Expires");
                for (Object[] row : licencias) {
                    pw.printf("\"%s\",\"%s\",\"%s\"\n", row[0], row[1], row[2]);
                }
                pw.println();
                pw.println("Registered violations");
                pw.println("Type,Date,Points");
                for (Object[] row : infracciones) {
                    pw.printf("\"%s\",\"%s\",\"%s\"\n", row[0], row[1], row[2]);
                }
            } else { // TXT
                pw.printf("Name: %s\nID Number: %s\nAddress: %s\nPhone: %s\nLicense Status: %s\n\n",
                        nombre, dni, direccion, telefono, estadoLic);
                pw.println("Licenses issued:");
                for (Object[] row : licencias) {
                    pw.printf("  Type: %s, Issued: %s, Expires: %s\n", row[0], row[1], row[2]);
                }
                pw.println("\nRegistered violations:");
                for (Object[] row : infracciones) {
                    pw.printf("  Type: %s, Date: %s, Points: %s\n", row[0], row[1], row[2]);
                }
            }
            JOptionPane.showMessageDialog(this, "Report exported successfully:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}