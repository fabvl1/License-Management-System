package visual.EntityPanels;

import model.AssociatedEntity;
import services.AssociatedEntityService;
import visual.Buttons.NewAssociatedEntityButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AssociatedEntityPanel extends AbstractEntityPanel<AssociatedEntity> {
    private static final long serialVersionUID = 1L;
    private JTextField entityNameFilterField;
    private JComboBox<String> entityTypeFilterCombo;
    private JButton filterButton;
    private JButton clearFilterButton;

    public AssociatedEntityPanel(String rol) {
        super(new AssociatedEntityService(), new String[]{
                "Entity Code", "Entity Name", "Entity Type", "Address", "Phone Number", "Contact Email", "Director Name"
        });

        // Filters
        entityNameFilterField = new JTextField(10);
        entityTypeFilterCombo = new JComboBox<>(new String[]{"All", "Clinic", "DrivingSchool"});
        filterButton = new JButton("Filter");
        clearFilterButton = new JButton("Clear");

        filterButton.addActionListener(e -> refreshTable());
        clearFilterButton.addActionListener(e -> {
            entityNameFilterField.setText("");
            entityTypeFilterCombo.setSelectedIndex(0);
            
            
            refreshTable();
        });

        // Remove the toolbar added by the parent
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

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Entity Name:"));
        filterPanel.add(entityNameFilterField);
        filterPanel.add(new JLabel("Entity Type:"));
        filterPanel.add(entityTypeFilterCombo);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        // Unified top panel
        JPanel unifiedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        unifiedPanel.setOpaque(false);
        if (toolbar != null) unifiedPanel.add(toolbar);
        unifiedPanel.add(filterPanel);

        add(unifiedPanel, BorderLayout.NORTH);
        
        //validation hide
        if(rol.equalsIgnoreCase("admin")) {
	    	
			
		} else
		 if(rol.equalsIgnoreCase("examiner")) {
			 hideDelete();
				hideEdit();
				btnAdd.setEnabled(false);
				btnAdd.setVisible(false);
			
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

    private List<AssociatedEntity> applyFilters(List<AssociatedEntity> entities) {
        String name = entityNameFilterField.getText().trim().toLowerCase();
        String selectedType = (String) entityTypeFilterCombo.getSelectedItem();

        return entities.stream().filter(entity -> {
            boolean matches = true;
            if (!name.isEmpty()) {
                matches &= entity.getEntityName() != null && entity.getEntityName().toLowerCase().contains(name);
            }
            if (selectedType != null && !"All".equals(selectedType)) {
                matches &= selectedType.equalsIgnoreCase(entity.getEntityType());
            }
            return matches;
        }).collect(Collectors.toList());
    }

    @Override
    public void refreshTable() {
        List<AssociatedEntity> entities = service.getAll();
        entities = applyFilters(entities);
        model.setRowCount(0);
        for (AssociatedEntity entity : entities) {
            model.addRow(getRowData(entity));
        }
    }

    @Override
    protected Object[] getRowData(AssociatedEntity entity) {
        return new Object[]{
                entity.getEntityCode(),
                entity.getEntityName(),
                entity.getEntityType(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getContactEmail(),
                entity.getDirectorName(),
             
        };
    }

    @Override
    protected AssociatedEntity getEntityFromRow(int row) {
        String entityCode = (String) table.getValueAt(row, 0);
        return service.getById(entityCode);
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) table.getValueAt(row, 0);
    }

    @Override
    protected void showEditDialog(AssociatedEntity entity) {
        JTextField txtEntityCode = new JTextField(entity.getEntityCode());
        txtEntityCode.setEditable(false);

        JTextField txtEntityName = new JTextField(entity.getEntityName());
        JComboBox<String> cmbEntityType = new JComboBox<>(new String[]{"Clinic", "DrivingSchool"});
        cmbEntityType.setSelectedItem(entity.getEntityType());
        JTextField txtAddress = new JTextField(entity.getAddress());
        JTextField txtPhoneNumber = new JTextField(entity.getPhoneNumber());
        JTextField txtContactEmail = new JTextField(entity.getContactEmail());
        JTextField txtDirectorName = new JTextField(entity.getDirectorName());
       

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Entity Code:"));
        panel.add(txtEntityCode);
        panel.add(new JLabel("Entity Name:"));
        panel.add(txtEntityName);
        panel.add(new JLabel("Entity Type:"));
        panel.add(cmbEntityType);
        panel.add(new JLabel("Address:"));
        panel.add(txtAddress);
        panel.add(new JLabel("Phone Number:"));
        panel.add(txtPhoneNumber);
        panel.add(new JLabel("Contact Email:"));
        panel.add(txtContactEmail);
        panel.add(new JLabel("Director Name:"));
        panel.add(txtDirectorName);
    

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Associated Entity",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtEntityName.getText().trim().isEmpty() ||
                txtAddress.getText().trim().isEmpty() ||
                txtPhoneNumber.getText().trim().isEmpty() ||
                txtContactEmail.getText().trim().isEmpty() ||
                txtDirectorName.getText().trim().isEmpty() 
               ) {
                JOptionPane.showMessageDialog(this, "All fields must be completed.");
                return;
            }

            entity.setEntityName(txtEntityName.getText().trim());
            entity.setEntityType((String) cmbEntityType.getSelectedItem());
            entity.setAddress(txtAddress.getText().trim());
            entity.setPhoneNumber(txtPhoneNumber.getText().trim());
            entity.setContactEmail(txtContactEmail.getText().trim());
            entity.setDirectorName(txtDirectorName.getText().trim());
           

            boolean ok = service.update(entity);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Associated entity updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating associated entity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshTable();
        }
       
    }

    @Override
    protected JButton createAddButton() {
    	btnAdd= new NewAssociatedEntityButton(null, this::refreshTable);
        return btnAdd;
    }
    

}