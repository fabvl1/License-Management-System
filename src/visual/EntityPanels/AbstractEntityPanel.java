package visual.EntityPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import services.EntityService;

public abstract class AbstractEntityPanel<T> extends JPanel {
	private static final long serialVersionUID = 1L;
    protected JTable table;
    protected EntityService<T> service;
    protected String[] columns;
    protected DefaultTableModel model;
    protected JButton btnEdit;
    protected JButton btnDelete;
    protected JButton btnAdd;
    protected String user;
    protected String r;

    public AbstractEntityPanel(EntityService<T> service, String[] columns) {
        this.service = service;
        this.columns = columns;
        this.model = new DefaultTableModel(columns, 0) {
        	private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        this.table = new JTable(model);

        setLayout(new BorderLayout());
        add(createToolbar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
       
    }

    // Toolbar generalizada
    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
         btnAdd = createAddButton();
         btnEdit = new JButton("Edit");
         btnDelete = new JButton("Delete");

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        return toolbar;
    }

    // Permite que cada subclase devuelva el botón adecuado (por ejemplo, NewDriverButton, etc.)
    protected abstract  JButton  createAddButton();

    // Lógica generalizada de refresco
    public void refreshTable() {
        model.setRowCount(0);
        List<T> entities = service.getAll();
        for (T entity : entities) {
            model.addRow(getRowData(entity));
        }
    }

    // EDITAR y ELIMINAR generalizados
    protected void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select one row to change.");
            return;
        }
        T selected = getEntityFromRow(row);
        showEditDialog(selected);
        refreshTable();
    }

    protected void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select one row to delete.");
            return;
        }
        String id = getEntityIdFromRow(row);
        int opt = JOptionPane.showConfirmDialog(this, "DO you want to delete this element?", "delete", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            service.delete(id);
            refreshTable();
        }
    }
    


    public JButton getBtnEdit() {
		return btnEdit;
	}

	public void setBtnEdit(JButton btnEdit) {
		this.btnEdit = btnEdit;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(JButton btnDelete) {
		this.btnDelete = btnDelete;
	}
	
	protected void hideDelete() {
    	getBtnDelete().setVisible(false);
    	getBtnDelete().setEnabled(false);
    }
	
	protected void hideEdit() {
    	getBtnEdit().setVisible(false);
    	getBtnEdit().setEnabled(false);
    }
	


	// Deben implementarse en subclases:
    protected abstract Object[] getRowData(T entity);
    protected abstract T getEntityFromRow(int row);
    protected abstract String getEntityIdFromRow(int row);
    protected abstract void showEditDialog(T entity);
}