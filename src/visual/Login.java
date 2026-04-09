package visual;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import services.UserService;
import visual.Buttons.ModernButton;
import visual_utils.ImagePanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FlatDarkLaf.setup();
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        FlatDarkLaf.setup();

        setTitle("Sistema de Gestión de Licencias");
        
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 650);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 934, 22);
        contentPane.add(menuBar);

        JMenu mnNewMenu = new JMenu("Configuración");
        menuBar.add(mnNewMenu);

        JMenu mnNewMenu_1 = new JMenu("Tema");
        mnNewMenu.add(mnNewMenu_1);

        JMenuItem mntmNewMenuItem = new JMenuItem("Claro");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    SwingUtilities.updateComponentTreeUI(Login.this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        mnNewMenu_1.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Oscuro");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    SwingUtilities.updateComponentTreeUI(Login.this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        mnNewMenu_1.add(mntmNewMenuItem_1);

        JPanel panel = new JPanel();
        panel.setBounds(0, 22, 934, 588);
        contentPane.add(panel);
        panel.setLayout(null);

        

        // Campo de usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblUsuario.setBounds(326, 301, 234, 30);
        panel.add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(326, 330, 258, 35);
        panel.add(txtUsuario);
        txtUsuario.setColumns(10);

        // Campo de contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblContrasena.setBounds(326, 402, 234, 30);
        panel.add(lblContrasena);

        JPasswordField pwdContrasena = new JPasswordField();
        pwdContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pwdContrasena.setBounds(326, 433, 271, 35);
        panel.add(pwdContrasena);
        
        // Nuevo ImagePanel al lado del avatar
        ImagePanel additionalImage = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo.png")));
        // Ajusta las coordenadas: a la derecha del avatar (232 + 271 + 10 = 513)
        additionalImage.setBounds(52, 12, 364, 277);
        panel.add(additionalImage);
        
        ImagePanel additionalImage_1 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo_en.png")));
        additionalImage_1.setBounds(497, 11, 364, 277);
        panel.add(additionalImage_1);

        // Botón para mostrar/ocultar contraseña
        JLabel lblTogglePass = new JLabel(new ImageIcon(Login.class.getResource("/visual/llave (1).png")));
        lblTogglePass.setBounds(594, 433, 44, 35);
        panel.add(lblTogglePass);
        
        ModernButton mdrnbtnAceptar = new ModernButton("Aceptar");
        mdrnbtnAceptar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		UserService usuarios = new UserService();
            String password = new String(pwdContrasena.getPassword());
            if(usuarios.autenticar(txtUsuario.getText(), password)) {
        			String rol =usuarios.getRolPorUsuario(txtUsuario.getText());
        			LicenseManagementUI l = new LicenseManagementUI(rol);
        			l.setVisible(true);
        			dispose();
        			
        		}else {
        		 javax.swing.JOptionPane.showMessageDialog(
        		            null,
        		            "Credenciales incorrectas.",
        		            "Error de Autenticación",
        		            javax.swing.JOptionPane.ERROR_MESSAGE
        		        );
        		    }
        		
        		
        	}
        });
        mdrnbtnAceptar.setBounds(326, 513, 271, 39);
        panel.add(mdrnbtnAceptar);
        lblTogglePass.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                pwdContrasena.setEchoChar(mostrar ? (char) 0 : '•');
            }
        });
    }
}
       