package visual.EntityPanels;

import model.Violation;
import services.ViolationService;
import visual.Buttons.NewViolationButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

public class ViolationPanel extends AbstractEntityPanel<Violation> {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> typeFilterCombo;
    private JComboBox<String> paidFilterCombo;
    private JTextField locationFilterField;
    private JButton filterButton;
    private JButton clearFilterButton;

    public ViolationPanel(String rol) {
        super(new ViolationService(), new String[]{
            "Code", "Type", "Date", "Location", "Description", "Points Deducted", "Paid", "License Code"
        });

        // Filters
        typeFilterCombo = new JComboBox<>(new String[]{"All", "Minor", "Major", "Critical"});
        paidFilterCombo = new JComboBox<>(new String[]{"All", "Paid", "Unpaid"});
        locationFilterField = new JTextField(8);
        filterButton = new JButton("Filter");
        clearFilterButton = new JButton("Clear");

        filterButton.addActionListener(e -> refreshTable());
        clearFilterButton.addActionListener(e -> {
            typeFilterCombo.setSelectedIndex(0);
            paidFilterCombo.setSelectedIndex(0);
            locationFilterField.setText("");
            
           
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
        filterPanel.add(new JLabel("Paid:"));
        filterPanel.add(paidFilterCombo);
        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationFilterField);
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

    private List<Violation> applyFilters(List<Violation> violations) {
        String selectedType = (String) typeFilterCombo.getSelectedItem();
        String selectedPaid = (String) paidFilterCombo.getSelectedItem();
        String location = locationFilterField.getText().trim().toLowerCase();

        return violations.stream().filter(violation -> {
            boolean matches = true;
            if (selectedType != null && !"All".equals(selectedType)) {
                matches &= selectedType.equalsIgnoreCase(violation.getViolationType());
            }
            if (selectedPaid != null && !"All".equals(selectedPaid)) {
                if ("Paid".equals(selectedPaid)) {
                    matches &= violation.isPaid();
                } else if ("Unpaid".equals(selectedPaid)) {
                    matches &= !violation.isPaid();
                }
            }
            if (!location.isEmpty()) {
                matches &= violation.getLocation() != null && violation.getLocation().toLowerCase().contains(location);
            }
            return matches;
        }).collect(Collectors.toList());
    }

    @Override
    public void refreshTable() {
        List<Violation> violations = service.getAll();
        violations = applyFilters(violations);
        model.setRowCount(0);
        for (Violation violation : violations) {
            model.addRow(getRowData(violation));
        }
    }

    @Override
    protected Object[] getRowData(Violation violation) {
        return new Object[]{
            violation.getViolationCode(),
            violation.getViolationType(),
            violation.getDate(),
            violation.getLocation(),
            violation.getDescription(),
            violation.getDeductedPoints(),
            violation.isPaid() ? "Yes" : "No",
            violation.getLicenseCode()
        };
    }

    @Override
    protected Violation getEntityFromRow(int row) {
        String code = (String) table.getValueAt(row, 0);
        return service.getById(code);
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) table.getValueAt(row, 0);
    }

    @Override
    protected void showEditDialog(Violation violation) {
        JTextField txtCode = new JTextField(violation.getViolationCode());
        txtCode.setEditable(false);

        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Minor", "Major", "Critical"});
        cmbType.setSelectedItem(violation.getViolationType());
        JTextField txtDate = new JTextField(violation.getDate() != null ? violation.getDate().toString() : "");
        JTextField txtLocation = new JTextField(violation.getLocation());
        JTextField txtDescription = new JTextField(violation.getDescription());
        JTextField txtPoints = new JTextField(String.valueOf(violation.getDeductedPoints()));
        JComboBox<String> cmbPaid = new JComboBox<>(new String[]{"Yes", "No"});
        cmbPaid.setSelectedItem(violation.isPaid() ? "Yes" : "No");
        JTextField txtLicenseCode = new JTextField(violation.getLicenseCode());

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Code:"));
        panel.add(txtCode);
        panel.add(new JLabel("Type:"));
        panel.add(cmbType);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(txtDate);
        panel.add(new JLabel("Location:"));
        panel.add(txtLocation);
        panel.add(new JLabel("Description:"));
        panel.add(txtDescription);
        panel.add(new JLabel("Points Deducted:"));
        panel.add(txtPoints);
        panel.add(new JLabel("Paid:"));
        panel.add(cmbPaid);
        panel.add(new JLabel("License Code:"));
        panel.add(txtLicenseCode);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Violation",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtDate.getText().trim().isEmpty() ||
                txtLocation.getText().trim().isEmpty() ||
                txtDescription.getText().trim().isEmpty() ||
                txtPoints.getText().trim().isEmpty() ||
                txtLicenseCode.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be completed.");
                return;
            }

            violation.setViolationType((String) cmbType.getSelectedItem());
            violation.setDate(Date.valueOf(txtDate.getText().trim()));
            violation.setLocation(txtLocation.getText().trim());
            violation.setDescription(txtDescription.getText().trim());
            violation.setDeductedPoints(Integer.parseInt(txtPoints.getText().trim()));
            violation.setPaid("Yes".equals(cmbPaid.getSelectedItem()));
            violation.setLicenseCode(txtLicenseCode.getText().trim());

            boolean ok = service.update(violation);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Violation updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating violation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
    }

    @Override
    protected JButton createAddButton() {
    	btnAdd= new NewViolationButton(null, this::refreshTable);
        return btnAdd;
    }
}