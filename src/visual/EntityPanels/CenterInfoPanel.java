package visual.EntityPanels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import model.Center;
import visual.Reports.CenterInfoReport;
import services.CenterService;

public class CenterInfoPanel extends CenterInfoReport {

    private static final long serialVersionUID = 1L;

    private static final CenterService centerService = new CenterService();

    /**
     * Opens the editable Center Information dialog.
     * @param parent the parent component for dialog centering
     */
    public static void showEditableDialog(Component parent) {
        // Assume there is only one center, get it
        List<Center> centers = centerService.getAll();
        if (centers.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No center found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Center center = centers.get(0);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Edit Center Configuration", Dialog.ModalityType.APPLICATION_MODAL);
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

        // Código del centro SOLO LECTURA
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        infoPanel.add(new JLabel("Center Code:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel centerCodeLabel = new JLabel(center.getCenterCode());
        infoPanel.add(centerCodeLabel, gbc);
        y++;

        // Editable fields
        JTextField centerNameField = addEditableRow(infoPanel, "Center Name:", center.getCenterName(), gbc, y++);
        JTextField postalAddressField = addEditableRow(infoPanel, "Postal Address:", center.getPostalAddress(), gbc, y++);
        JTextField phoneNumberField = addEditableRow(infoPanel, "Phone:", center.getPhoneNumber(), gbc, y++);
        JTextField contactEmailField = addEditableRow(infoPanel, "Email:", center.getContactEmail(), gbc, y++);
        JTextField generalDirectorField = addEditableRow(infoPanel, "General Director:", center.getGeneralDirector(), gbc, y++);
        JTextField hrManagerField = addEditableRow(infoPanel, "HR Manager:", center.getHrManager(), gbc, y++);
        JTextField accountingManagerField = addEditableRow(infoPanel, "Accounting Manager:", center.getAccountingManager(), gbc, y++);
        JTextField unionSecretaryField = addEditableRow(infoPanel, "Union Secretary:", center.getUnionSecretary(), gbc, y++);

        // Logo
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        infoPanel.add(new JLabel("Logo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(120, 120));
        logoLabel.setBorder(BorderFactory.createEtchedBorder());

        // Estado del logo
        final byte[][] logoBytesRef = new byte[1][];
        byte[] logoBytes = center.getLogo();
        if (logoBytes != null && logoBytes.length > 0) {
            try {
                ImageIcon icon = new ImageIcon(logoBytes);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
                logoBytesRef[0] = logoBytes;
            } catch (Exception e) {
                // If corrupted image, skip
            }
        }
        infoPanel.add(logoLabel, gbc);

        // Botón para elegir imagen
        JButton chooseLogoButton = new JButton("Seleccionar logo...");
        gbc.gridx = 2;
        infoPanel.add(chooseLogoButton, gbc);

        chooseLogoButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif", "bmp"));
            int result = chooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    ImageIcon icon = new ImageIcon(bytes);
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    logoLabel.setIcon(new ImageIcon(img));
                    logoBytesRef[0] = bytes;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "No se pudo cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        y++;

        dialog.add(infoPanel, BorderLayout.CENTER);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Update center object with new values
            center.setCenterName(centerNameField.getText());
            center.setPostalAddress(postalAddressField.getText());
            center.setPhoneNumber(phoneNumberField.getText());
            center.setContactEmail(contactEmailField.getText());
            center.setGeneralDirector(generalDirectorField.getText());
            center.setHrManager(hrManagerField.getText());
            center.setAccountingManager(accountingManagerField.getText());
            center.setUnionSecretary(unionSecretaryField.getText());
            // Logo
            if (logoBytesRef[0] != null) {
                center.setLogo(logoBytesRef[0]);
            }
            // Save changes to the database
            if (centerService.update(center)) {
                JOptionPane.showMessageDialog(dialog, "Center information updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update center information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static JTextField addEditableRow(JPanel panel, String label, String value, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JTextField textField = new JTextField(value, 20);
        panel.add(textField, gbc);
        return textField;
    }
}