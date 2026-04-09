package visual.EntityPanels;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import model.User;
import services.UserService;

public class UserPanel extends AbstractEntityPanel<User> {
    private static final long serialVersionUID = 1L;

    public UserPanel() {
        super(new UserService(), new String[]{"Name", "Rol", "Password"});
        refreshTable();
    }

    @Override
    protected JButton createAddButton() {
        JButton btnAdd = new JButton("Add User");
        btnAdd.setEnabled(false);
        btnAdd.setVisible(false);
        btnAdd.addActionListener(e -> showEditDialog(null));
        return btnAdd;
    }

    @Override
    protected Object[] getRowData(User user) {
        return new Object[]{user.getNombre(), user.getRol(), user.getContra()};
    }

    @Override
    protected User getEntityFromRow(int row) {
        String nombre = (String) model.getValueAt(row, 0);
        String rol = (String) model.getValueAt(row, 1);
        String contra = (String) model.getValueAt(row, 2);
        User user = new User();
        user.setNombre(nombre);
        user.setRol(rol);
        user.setContra(contra);
        return user;
    }

    @Override
    protected String getEntityIdFromRow(int row) {
        return (String) model.getValueAt(row, 0); // El nombre del usuario es el ID
    }
    @Override
    protected void showEditDialog(User user) {
        JDialog dialog = new JDialog((java.awt.Frame) null, user == null ? "Add User" : "Edit User", true); // Modal
        dialog.setSize(400, 250);
        dialog.setLayout(new BorderLayout(10, 10));
    
        // Panel principal con margen
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas, con espacio entre componentes
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Campo para el nombre de usuario
        mainPanel.add(new javax.swing.JLabel("User Name:"));
        JTextField txtNombre = new JTextField(user != null ? user.getNombre() : "");
        mainPanel.add(txtNombre);
    
        // Campo para el rol
        mainPanel.add(new javax.swing.JLabel("Role:"));
        JTextField txtRol = new JTextField(user != null ? user.getRol() : "");
        mainPanel.add(txtRol);
    
        // Campo para la contraseña
        mainPanel.add(new javax.swing.JLabel("Password:"));
        JTextField txtContra = new JTextField(user != null ? user.getContra() : "");
        mainPanel.add(txtContra);
    
        // Botón de guardar
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String rol = txtRol.getText();
            String contra = txtContra.getText();
    
            if (nombre.isEmpty() || rol.isEmpty() || contra.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.");
                return;
            }
    
            UserService userService = (UserService) service;
            if (user == null) {
                // Crear nuevo usuario
                User newUser = new User();
                newUser.setNombre(nombre);
                newUser.setRol(rol);
                newUser.setContra(contra);
                userService.create(newUser);
            } else {
                // Actualizar usuario existente
                user.setNombre(nombre);
                user.setRol(rol);
                user.setContra(contra);
                userService.update(user);
            }
    
            refreshTable();
            dialog.dispose();
        });
    
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this); // Centrar el diálogo respecto al panel principal
        dialog.setVisible(true);
    }
}