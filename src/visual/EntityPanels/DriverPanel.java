package visual.EntityPanels;

import model.Driver;
import services.DriverService;
import visual.Buttons.NewDriverButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

public class DriverPanel extends AbstractEntityPanel<Driver> {
    private static final long serialVersionUID = 1L;
    private JTextField firstNameFilterField;
    private JTextField lastNameFilterField;
    private JComboBox<String> licenseStatusFilterCombo;
    private JButton filterButton;
    private JButton clearFilterButton;

    public DriverPanel(String rol) {
    	 super(new DriverService(), new String[]{
    		        "ID", "First Name", "Last Name", "Birth Date", "Address", "Phone Number", "Email", "License Status"
    		    });

    		    // Filters
    		    firstNameFilterField = new JTextField(8);
    		    lastNameFilterField = new JTextField(8);
    		    licenseStatusFilterCombo = new JComboBox<>(new String[]{"All", "Active", "Suspended", "Expired"});
    		    filterButton = new JButton("Filter");
    		    clearFilterButton = new JButton("Clear");

    		    filterButton.addActionListener(e -> refreshTable());
    		    clearFilterButton.addActionListener(e -> {
    		        firstNameFilterField.setText("");
    		        lastNameFilterField.setText("");
    		        licenseStatusFilterCombo.setSelectedIndex(0);
    		        
    		       
    		        
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

    		    // Filter panel (no border, to match the buttons)
    		    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    		    filterPanel.setOpaque(false);
    		    filterPanel.add(new JLabel("First Name:"));
    		    filterPanel.add(firstNameFilterField);
    		    filterPanel.add(new JLabel("Last Name:"));
    		    filterPanel.add(lastNameFilterField);
    		    filterPanel.add(new JLabel("License Status:"));
    		    filterPanel.add(licenseStatusFilterCombo);
    		    filterPanel.add(filterButton);
    		    filterPanel.add(clearFilterButton);

    		    // Unified top panel: toolbar + filters, all in FlowLayout
    		    JPanel unifiedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    		    unifiedPanel.setOpaque(false);
    		    if (toolbar != null) unifiedPanel.add(toolbar);
    		    unifiedPanel.add(filterPanel);

    		    add(unifiedPanel, BorderLayout.NORTH);
    		    
    		    
    		    //validation hide
    		    if(rol.equalsIgnoreCase("admin")) {
    		    	hideDelete();
					hideEdit();
					btnAdd.setEnabled(false);
					btnAdd.setVisible(false);
					
				} else
				 if(rol.equalsIgnoreCase("examiner")) {
					
				}
				else if (rol.equalsIgnoreCase("manager")) {
					hideDelete();
					hideEdit();
					btnAdd.setEnabled(false);
					btnAdd.setVisible(false);
				
				}
				else if (rol.equalsIgnoreCase("supervisor")) {
					hideDelete();
					hideEdit();
					btnAdd.setEnabled(false);
					btnAdd.setVisible(false);
					
				}
    		   
    		    refreshTable();
    		}
    

    private List<Driver> applyFilters(List<Driver> drivers) {
        String firstName = firstNameFilterField.getText().trim().toLowerCase();
        String lastName = lastNameFilterField.getText().trim().toLowerCase();
        String selectedStatus = (String) licenseStatusFilterCombo.getSelectedItem();

        return drivers.stream().filter(driver -> {
            boolean matches = true;
            if (!firstName.isEmpty()) {
                matches &= driver.getFirstName() != null && driver.getFirstName().toLowerCase().contains(firstName);
            }
            if (!lastName.isEmpty()) {
                matches &= driver.getLastName() != null && driver.getLastName().toLowerCase().contains(lastName);
            }
            if (selectedStatus != null && !"All".equals(selectedStatus)) {
                matches &= selectedStatus.equalsIgnoreCase(driver.getLicenseStatus());
            }
            return matches;
        }).collect(Collectors.toList());
    }

    @Override
    public void refreshTable() {
        List<Driver> drivers = service.getAll();
        drivers = applyFilters(drivers);
        model.setRowCount(0);
        for (Driver driver : drivers) {
            model.addRow(getRowData(driver));
        }
    }

    @Override
    protected Object[] getRowData(Driver driver) {
        return new Object[]{
            driver.getDriverId(),
            driver.getFirstName(),
            driver.getLastName(),
            driver.getBirthDate(),
            driver.getAddress(),
            driver.getPhoneNumber(),
            driver.getEmail(),
            driver.getLicenseStatus()
        };
    }

    @Override
    protected Driver getEntityFromRow(int row) {
        String id = (String) table.getValueAt(row, 0);
        return service.getById(id);
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) table.getValueAt(row, 0);
    }

    @Override
    protected void showEditDialog(Driver driver) {
        JTextField txtId = new JTextField(driver.getDriverId());
        txtId.setEditable(false);

        JTextField txtFirstName = new JTextField(driver.getFirstName());
        JTextField txtLastName = new JTextField(driver.getLastName());
        JTextField txtAddress = new JTextField(driver.getAddress());
        JTextField txtPhone = new JTextField(driver.getPhoneNumber());
        JTextField txtEmail = new JTextField(driver.getEmail());
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Suspended", "Expired"});
        cmbStatus.setSelectedItem(driver.getLicenseStatus());
        JTextField txtBirthDate = new JTextField(driver.getBirthDate() != null ? driver.getBirthDate().toString() : "");

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("First Name:"));
        panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(txtLastName);
        panel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        panel.add(txtBirthDate);
        panel.add(new JLabel("Address:"));
        panel.add(txtAddress);
        panel.add(new JLabel("Phone Number:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("License Status:"));
        panel.add(cmbStatus);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Driver",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtFirstName.getText().trim().isEmpty() ||
                txtLastName.getText().trim().isEmpty() ||
                txtBirthDate.getText().trim().isEmpty() ||
                txtAddress.getText().trim().isEmpty() ||
                txtPhone.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be completed.");
                return;
            }

            driver.setFirstName(txtFirstName.getText().trim());
            driver.setLastName(txtLastName.getText().trim());
            driver.setBirthDate(Date.valueOf(txtBirthDate.getText().trim()));
            driver.setAddress(txtAddress.getText().trim());
            driver.setPhoneNumber(txtPhone.getText().trim());
            driver.setEmail(txtEmail.getText().trim());
            driver.setLicenseStatus((String) cmbStatus.getSelectedItem());

            boolean ok = service.update(driver);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Driver updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating driver.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
    }

    @Override
    protected JButton createAddButton() {
    	btnAdd= new NewDriverButton(null, this::refreshTable);
        return btnAdd;
    }
    
    
}