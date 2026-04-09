package visual.EntityPanels;

import model.License;
import services.LicenseService;
import visual.Buttons.NewLicenseButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

public class LicensePanel extends AbstractEntityPanel<License> {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> typeFilterCombo;
    private JComboBox<String> vehicleFilterCombo;
    private JComboBox<String> renewedFilterCombo;
    private JTextField driverIdFilterField;
    private JButton filterButton;
    private JButton clearFilterButton;

    public LicensePanel(String rol) {
        super(new LicenseService(), new String[]{
            "Code", "Type", "Issue Date", "Expiration Date", "Vehicle Category", "Restrictions", "Renewed", "Driver ID"
        });

        // Filters
        typeFilterCombo = new JComboBox<>(new String[]{"All", "A", "B", "C", "D", "E"});
        vehicleFilterCombo = new JComboBox<>(new String[]{"All", "Motorcycle", "Car", "Truck", "Bus"});
        renewedFilterCombo = new JComboBox<>(new String[]{"All", "Renewed", "Not Renewed"});
        driverIdFilterField = new JTextField(8);
        filterButton = new JButton("Filter");
        clearFilterButton = new JButton("Clear");

        filterButton.addActionListener(e -> refreshTable());
        clearFilterButton.addActionListener(e -> {
            typeFilterCombo.setSelectedIndex(0);
            vehicleFilterCombo.setSelectedIndex(0);
            renewedFilterCombo.setSelectedIndex(0);
            driverIdFilterField.setText("");
            

            
            
            refreshTable();
        });

        // Find and remove the toolbar added by the parent (from BorderLayout.NORTH)
        JToolBar toolbar = null;
        for (Component comp : getComponents()) {
            if (comp instanceof JToolBar) {
                toolbar = (JToolBar) comp;
                break;
            }
        }
        if (toolbar != null) {
            remove(toolbar);
        }

        // Filter panel (no border, to match the toolbar)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilterCombo);
        filterPanel.add(new JLabel("Vehicle:"));
        filterPanel.add(vehicleFilterCombo);
        filterPanel.add(new JLabel("Renewed:"));
        filterPanel.add(renewedFilterCombo);
        filterPanel.add(new JLabel("Driver ID:"));
        filterPanel.add(driverIdFilterField);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        // Unified top panel: toolbar + filters, all in FlowLayout for perfect alignment
        JPanel unifiedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        unifiedPanel.setOpaque(false);
        if (toolbar != null) unifiedPanel.add(toolbar);
        unifiedPanel.add(filterPanel);

        add(unifiedPanel, BorderLayout.NORTH);

        
        if(rol.equalsIgnoreCase("admin")) {
        	hideDelete();
			hideEdit();
			btnAdd.setEnabled(false);
			btnAdd.setVisible(false);
		
			
		} else
		 if(rol.equalsIgnoreCase("examiner")) {
			 hideDelete();
				hideEdit();
				btnAdd.setEnabled(false);
				btnAdd.setVisible(false);
			
		}
		else if (rol.equalsIgnoreCase("manager")) {
			
		}
		else if (rol.equalsIgnoreCase("supervisor")) {
			hideDelete();
			hideEdit();
			btnAdd.setEnabled(false);
			btnAdd.setVisible(false);
			
		}
        
        
        refreshTable();
    }

    private List<License> applyFilters(List<License> licenses) {
        String selectedType = (String) typeFilterCombo.getSelectedItem();
        String selectedVehicle = (String) vehicleFilterCombo.getSelectedItem();
        String selectedRenewed = (String) renewedFilterCombo.getSelectedItem();
        String driverId = driverIdFilterField.getText().trim().toLowerCase();

        return licenses.stream().filter(license -> {
            boolean matches = true;
            if (selectedType != null && !"All".equals(selectedType)) {
                matches &= selectedType.equalsIgnoreCase(license.getLicenseType());
            }
            if (selectedVehicle != null && !"All".equals(selectedVehicle)) {
                matches &= selectedVehicle.equalsIgnoreCase(license.getVehicleCategory());
            }
            if (selectedRenewed != null && !"All".equals(selectedRenewed)) {
                if ("Renewed".equals(selectedRenewed)) {
                    matches &= license.isRenewed();
                } else if ("Not Renewed".equals(selectedRenewed)) {
                    matches &= !license.isRenewed();
                }
            }
            if (!driverId.isEmpty()) {
                matches &= license.getDriverId() != null && license.getDriverId().toLowerCase().contains(driverId);
            }
            return matches;
        }).collect(Collectors.toList());
    }

    @Override
    public void refreshTable() {
        List<License> licenses = service.getAll();
        licenses = applyFilters(licenses);
        model.setRowCount(0);
        for (License license : licenses) {
            model.addRow(getRowData(license));
        }
    }

    @Override
    protected Object[] getRowData(License license) {
        return new Object[]{
            license.getLicenseCode(),
            license.getLicenseType(),
            license.getIssueDate(),
            license.getExpirationDate(),
            license.getVehicleCategory(),
            license.getRestrictions(),
            license.isRenewed() ? "Yes" : "No",
            license.getDriverId()
        };
    }

    @Override
    protected License getEntityFromRow(int row) {
        String code = (String) table.getValueAt(row, 0);
        return service.getById(code);
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) table.getValueAt(row, 0);
    }

    @Override
    protected void showEditDialog(License license) {
        JTextField txtCode = new JTextField(license.getLicenseCode());
        txtCode.setEditable(false);

        JComboBox<String> cmbType = new JComboBox<>(new String[]{"A", "B", "C", "D", "E"});
        cmbType.setSelectedItem(license.getLicenseType());
        JTextField txtIssueDate = new JTextField(license.getIssueDate() != null ? license.getIssueDate().toString() : "");
        JTextField txtExpirationDate = new JTextField(license.getExpirationDate() != null ? license.getExpirationDate().toString() : "");
        JComboBox<String> cmbVehicle = new JComboBox<>(new String[]{"Motorcycle", "Car", "Truck", "Bus"});
        cmbVehicle.setSelectedItem(license.getVehicleCategory());
        JTextField txtRestrictions = new JTextField(license.getRestrictions());
        JComboBox<String> cmbRenewed = new JComboBox<>(new String[]{"Yes", "No"});
        cmbRenewed.setSelectedItem(license.isRenewed() ? "Yes" : "No");
        JTextField txtDriverId = new JTextField(license.getDriverId());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Code:"));
        panel.add(txtCode);
        panel.add(new JLabel("Type:"));
        panel.add(cmbType);
        panel.add(new JLabel("Issue Date (YYYY-MM-DD):"));
        panel.add(txtIssueDate);
        panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        panel.add(txtExpirationDate);
        panel.add(new JLabel("Vehicle Category:"));
        panel.add(cmbVehicle);
        panel.add(new JLabel("Restrictions:"));
        panel.add(txtRestrictions);
        panel.add(new JLabel("Renewed:"));
        panel.add(cmbRenewed);
        panel.add(new JLabel("Driver ID:"));
        panel.add(txtDriverId);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit License",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtIssueDate.getText().trim().isEmpty() ||
                txtExpirationDate.getText().trim().isEmpty() ||
                txtRestrictions.getText().trim().isEmpty() ||
                txtDriverId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be completed.");
                return;
            }

            license.setLicenseType((String) cmbType.getSelectedItem());
            license.setIssueDate(Date.valueOf(txtIssueDate.getText().trim()));
            license.setExpirationDate(Date.valueOf(txtExpirationDate.getText().trim()));
            license.setVehicleCategory((String) cmbVehicle.getSelectedItem());
            license.setRestrictions(txtRestrictions.getText().trim());
            license.setRenewed("Yes".equals(cmbRenewed.getSelectedItem()));
            license.setDriverId(txtDriverId.getText().trim());

            boolean ok = service.update(license);
            if (ok) {
                JOptionPane.showMessageDialog(this, "License updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating license.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
    }

    @Override
    protected JButton createAddButton() {
    	btnAdd= new NewLicenseButton(null, this::refreshTable);
        return btnAdd;
    }
}