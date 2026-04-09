package visual;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import model.User;
import services.UserService;
import visual.Buttons.ModernButton;
import visual_utils.AvatarCircular;
import visual_utils.ImagePanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;  // Asegúrate de que la clase ImagePanel esté en este paquete

public class AddRol extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JTextField textField_1;
    private JPasswordField passwordField_1;
    private JTextField textField_2;
    private JPasswordField passwordField_2;
    private JTextField textField_3;
    private JPasswordField passwordField_3;
    private JLayeredPane paneles;
    private UserService userService;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	FlatDarkLaf.setup();
                    AddRol frame = new AddRol();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public AddRol() {
    	FlatDarkLaf.setup();
		userService = new UserService();
    	setTitle("ADD-ROL");
    	setResizable(false);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 830, 608);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 814, 22);
        contentPane.add(menuBar);

        JMenu mnNewMenu = new JMenu("Setting");
        menuBar.add(mnNewMenu);

        JMenu mnNewMenu_1 = new JMenu("theme");
        mnNewMenu.add(mnNewMenu_1);

        JMenuItem mntmNewMenuItem = new JMenuItem("Light");
        mntmNewMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  try {
        	            // Establece el look and feel light
        	            UIManager.setLookAndFeel(new FlatLightLaf());
        	            // Actualiza la UI
        	            SwingUtilities.updateComponentTreeUI(AddRol.this);
        	        } catch (Exception ex) {
        	            ex.printStackTrace();
        	        }
        	
        	}
        });
        mnNewMenu_1.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Dark");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  try {
        	            // Establece el look and feel light
        	            UIManager.setLookAndFeel(new FlatDarkLaf());
        	            // Actualiza la UI
        	            SwingUtilities.updateComponentTreeUI(AddRol.this);
        	        } catch (Exception ex) {
        	            ex.printStackTrace();
        	        }
        	}
        });
        mnNewMenu_1.add(mntmNewMenuItem_1);

        JPanel panel = new JPanel();
        panel.setBounds(0, 22, 814, 547);
        contentPane.add(panel);
        panel.setLayout(null);

        JMenuBar menuBar_1 = new JMenuBar();
        menuBar_1.setBounds(0, 0, 814, 22);
        menuBar_1.setLayout(new GridLayout(1, 4));
        panel.add(menuBar_1);

        JMenu mnNewMenu_2 = new JMenu("Admin");  
        mnNewMenu_2.setHorizontalAlignment(SwingConstants.CENTER);         
        mnNewMenu_2.setHorizontalTextPosition(SwingConstants.CENTER);
        mnNewMenu_2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBar_1.add(mnNewMenu_2);
        mnNewMenu_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cardLayout = (CardLayout) paneles.getLayout();
				cardLayout.show(paneles,"admin");
				mnNewMenu_2.setPopupMenuVisible(false);
		
			}
		});

        JMenu mnNewMenu_3 = new JMenu("Manager");
        mnNewMenu_3.setHorizontalAlignment(SwingConstants.CENTER);         
        mnNewMenu_3.setHorizontalTextPosition(SwingConstants.CENTER);
        mnNewMenu_3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBar_1.add(mnNewMenu_3);
        mnNewMenu_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cardLayout = (CardLayout) paneles.getLayout();
				cardLayout.show(paneles,"gestor");
				mnNewMenu_3.setPopupMenuVisible(false);
		
			}
		});

        JMenu mnNewMenu_4 = new JMenu("Examiner");
        mnNewMenu_4.setHorizontalAlignment(SwingConstants.CENTER);         
        mnNewMenu_4.setHorizontalTextPosition(SwingConstants.CENTER);
        mnNewMenu_4.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBar_1.add(mnNewMenu_4);
        mnNewMenu_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cardLayout = (CardLayout) paneles.getLayout();
				cardLayout.show(paneles,"examinador");
				mnNewMenu_4.setPopupMenuVisible(false);
		
			}
		});
        

        JMenu mnNewMenu_5 = new JMenu("Supervisor");
        mnNewMenu_5.setHorizontalAlignment(SwingConstants.CENTER);         
        mnNewMenu_5.setHorizontalTextPosition(SwingConstants.CENTER);
        mnNewMenu_5.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuBar_1.add(mnNewMenu_5);
        mnNewMenu_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cardLayout = (CardLayout) paneles.getLayout();
				cardLayout.show(paneles,"supervisor");
				mnNewMenu_5.setPopupMenuVisible(false);
		
			}
		});

        paneles = new JLayeredPane();
        paneles.setBounds(0, 21, 814, 526);
        panel.add(paneles);
        paneles.setLayout(new CardLayout(0, 0));

        JPanel admin = new JPanel();
        paneles.add(admin, "admin");
        admin.setLayout(null);

        // Avatar circular del Admin
        AvatarCircular avatarCircular = new AvatarCircular();
        avatarCircular.setAvatar(new ImageIcon(AddRol.class.getResource("/visual/google_admin_icon_131692.png")));
        avatarCircular.setBounds(253, 70, 271, 234);
        admin.add(avatarCircular);

        // Nuevo ImagePanel al lado del avatar
        ImagePanel additionalImage = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo.png")));
        // Ajusta las coordenadas: a la derecha del avatar (232 + 271 + 10 = 513)
        additionalImage.setBounds(10, 39, 233, 277);
        admin.add(additionalImage);

        JLabel lblNewLabel = new JLabel("Admin");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 33));
        lblNewLabel.setBounds(143, 14, 482, 45);
        admin.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(253, 326, 271, 45);
        admin.add(textField);
        textField.setColumns(10);

        ModernButton mdrnbtnAceptar = new ModernButton("Aceptar");
        mdrnbtnAceptar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!userService.exist(textField.getText())) {
					User u=new User();
					u.setNombre(textField.getText());
					u.setContra(new String(passwordField.getPassword()));
					u.setRol("admin");
					if (userService.create(u)) {
			            JOptionPane.showMessageDialog(null, "Usuario ha sido creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			            SwingUtilities.getWindowAncestor((JButton) e.getSource()).dispose(); // Cierra el JFrame actual
					}
        	    } else {
        	        // Mostrar mensaje de usuario incorrecto
        	        JOptionPane.showMessageDialog(null, "Este usuario ya existe.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        	    }
        		
        		
        		
        	}
        });
        mdrnbtnAceptar.setBounds(253, 460, 271, 39);
        admin.add(mdrnbtnAceptar);

        // Label para el ícono de la llave (contraseña)
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(AddRol.class.getResource("/visual/llave (1).png")));
        lblNewLabel_1.setBounds(532, 382, 60, 45);
        admin.add(lblNewLabel_1);
        lblNewLabel_1.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                if (mostrar) {
                    passwordField.setEchoChar((char) 0); // Mostrar texto
                } else {
                    passwordField.setEchoChar('•'); // Ocultar texto con puntos
                }
            }
        });

        passwordField = new JPasswordField();
        passwordField.setBounds(253, 382, 271, 45);
        admin.add(passwordField);
        
        ImagePanel additionalImage_1 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo_en.png")));
        additionalImage_1.setBounds(534, 39, 270, 277);
        admin.add(additionalImage_1);
        
        JPanel gestor = new JPanel();
        gestor.setLayout(null);
        paneles.add(gestor, "gestor");
        
        AvatarCircular avatarCircular_1 = new AvatarCircular();
        avatarCircular_1.setAvatar(new ImageIcon(AddRol.class.getResource("/visual/financiero (1).png")));
        avatarCircular_1.setBounds(253, 70, 271, 234);
        gestor.add(avatarCircular_1);
        
        ImagePanel additionalImage_2 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo.png")));;
        additionalImage_2.setBounds(10, 39, 233, 277);
        gestor.add(additionalImage_2);
        
        JLabel lblGestorPrincipal = new JLabel("Manager");
        lblGestorPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        lblGestorPrincipal.setFont(new Font("Segoe UI Black", Font.BOLD, 33));
        lblGestorPrincipal.setBounds(143, 14, 482, 45);
        gestor.add(lblGestorPrincipal);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(253, 326, 271, 45);
        gestor.add(textField_1);
        
        ModernButton mdrnbtnAceptar_1 = new ModernButton("Aceptar");
        mdrnbtnAceptar_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!userService.exist(textField_1.getText())) {
					User u=new User();
					u.setNombre(textField_1.getText());
					u.setContra(new String(passwordField_1.getPassword()));
					u.setRol("manager");
					if (userService.create(u)) {
			            JOptionPane.showMessageDialog(null, "Usuario ha sido creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			            SwingUtilities.getWindowAncestor((JButton) e.getSource()).dispose(); // Cierra el JFrame actual
					}
        	    } else {
        	        // Mostrar mensaje de usuario incorrecto
        	        JOptionPane.showMessageDialog(null, "Este usuario ya existe.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        	    }
        		
        		
        	}
        });
        mdrnbtnAceptar_1.setBounds(253, 460, 271, 39);
        gestor.add(mdrnbtnAceptar_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("");
        lblNewLabel_1_1.setIcon(new ImageIcon(AddRol.class.getResource("/visual/llave (1).png")));
        lblNewLabel_1_1.setBounds(532, 382, 60, 45);
        gestor.add(lblNewLabel_1_1);
        lblNewLabel_1_1.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                if (mostrar) {
                	passwordField_1.setEchoChar((char) 0); // Mostrar texto
                } else {
                	passwordField_1.setEchoChar('•'); // Ocultar texto con puntos
                }
            }
        });
        
        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(253, 382, 271, 45);
        gestor.add(passwordField_1);
        
        ImagePanel additionalImage_1_1 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo_en.png")));
        additionalImage_1_1.setBounds(534, 39, 270, 277);
        gestor.add(additionalImage_1_1);
        
        JPanel examinador = new JPanel();
        examinador.setLayout(null);
        paneles.add(examinador, "examinador");
        
        AvatarCircular avatarCircular_2 = new AvatarCircular();
        avatarCircular_2.setAvatar(new ImageIcon(AddRol.class.getResource("/visual/examinar.png")));
        avatarCircular_2.setBounds(253, 70, 271, 234);
        examinador.add(avatarCircular_2);
        
        ImagePanel additionalImage_3 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo.png")));
        additionalImage_3.setBounds(10, 39, 233, 277);
        examinador.add(additionalImage_3);
        
        JLabel lblNewLabel_3 = new JLabel("Examiner");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_3.setFont(new Font("Segoe UI Black", Font.BOLD, 33));
        lblNewLabel_3.setBounds(143, 14, 482, 45);
        examinador.add(lblNewLabel_3);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(253, 326, 271, 45);
        examinador.add(textField_2);
        
        ModernButton mdrnbtnAceptar_2 = new ModernButton("Aceptar");
        mdrnbtnAceptar_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
				if(!userService.exist(textField_2.getText())) {
					User u=new User();
					u.setNombre(textField_2.getText());
					u.setContra(new String(passwordField_2.getPassword()));
					u.setRol("examiner");
					if (userService.create(u)) {
			            JOptionPane.showMessageDialog(null, "Usuario ha sido creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			            SwingUtilities.getWindowAncestor((JButton) e.getSource()).dispose(); // Cierra el JFrame actual
					}
        	    } else {
        	        // Mostrar mensaje de usuario incorrecto
        	        JOptionPane.showMessageDialog(null, "Este usuario ya existe.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        	    }
        		
        	}
        });
        mdrnbtnAceptar_2.setBounds(253, 460, 271, 39);
        examinador.add(mdrnbtnAceptar_2);
        
        JLabel lblNewLabel_1_2 = new JLabel("");
        lblNewLabel_1_2.setIcon(new ImageIcon(AddRol.class.getResource("/visual/llave (1).png")));
        lblNewLabel_1_2.setBounds(532, 382, 60, 45);
        examinador.add(lblNewLabel_1_2);
        lblNewLabel_1_2.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                if (mostrar) {
                	passwordField_2.setEchoChar((char) 0); // Mostrar texto
                } else {
                	passwordField_2.setEchoChar('•'); // Ocultar texto con puntos
                }
            }
        });
        
        passwordField_2 = new JPasswordField();
        passwordField_2.setBounds(253, 382, 271, 45);
        examinador.add(passwordField_2);
        
        ImagePanel additionalImage_1_2 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo_en.png")));
        additionalImage_1_2.setBounds(534, 39, 270, 277);
        examinador.add(additionalImage_1_2);
        
        JPanel supervisor = new JPanel();
        supervisor.setLayout(null);
        paneles.add(supervisor, "supervisor");
        
        AvatarCircular avatarCircular_3 = new AvatarCircular();
        avatarCircular_3.setAvatar(new ImageIcon(AddRol.class.getResource("/visual/supervisor.png")));
        avatarCircular_3.setBounds(253, 70, 271, 234);
        supervisor.add(avatarCircular_3);
        
        ImagePanel additionalImage_4 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo.png")));
        additionalImage_4.setBounds(10, 39, 233, 277);
        supervisor.add(additionalImage_4);
        
        JLabel lblNewLabel_4 = new JLabel("Supervisor");
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_4.setFont(new Font("Segoe UI Black", Font.BOLD, 33));
        lblNewLabel_4.setBounds(143, 14, 482, 45);
        supervisor.add(lblNewLabel_4);
        
        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.setBounds(253, 326, 271, 45);
        supervisor.add(textField_3);
        
        ModernButton mdrnbtnAceptar_3 = new ModernButton("Aceptar");
        mdrnbtnAceptar_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(!userService.exist(textField_3.getText())) {
					User u=new User();
					u.setNombre(textField_3.getText());
					u.setContra(new String(passwordField_3.getPassword()));
					u.setRol("supervisor");
					if (userService.create(u)) {
			            JOptionPane.showMessageDialog(null, "Usuario ha sido creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			            SwingUtilities.getWindowAncestor((JButton) e.getSource()).dispose(); // Cierra el JFrame actual
					}
        	    } else {
        	        // Mostrar mensaje de usuario incorrecto
        	        JOptionPane.showMessageDialog(null, "Este usuario ya existe.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        	    }
        		
        	}
        });
        mdrnbtnAceptar_3.setBounds(253, 460, 271, 39);
        supervisor.add(mdrnbtnAceptar_3);
        
        JLabel lblNewLabel_1_3 = new JLabel("");
        lblNewLabel_1_3.setIcon(new ImageIcon(AddRol.class.getResource("/visual/llave (1).png")));
        lblNewLabel_1_3.setBounds(532, 382, 60, 45);
        supervisor.add(lblNewLabel_1_3);
        lblNewLabel_1_3.addMouseListener(new MouseAdapter() {
            private boolean mostrar = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar = !mostrar;
                if (mostrar) {
                	passwordField_3.setEchoChar((char) 0); // Mostrar texto
                } else {
                	passwordField_3.setEchoChar('•'); // Ocultar texto con puntos
                }
            }
        });
        
        passwordField_3 = new JPasswordField();
        passwordField_3.setBounds(253, 382, 271, 45);
        supervisor.add(passwordField_3);
        
        ImagePanel additionalImage_1_3 = new ImagePanel(new ImageIcon(AddRol.class.getResource("/visual/logo_en.png")));
        additionalImage_1_3.setBounds(534, 39, 270, 277);
        supervisor.add(additionalImage_1_3);

        // Almacena el echo char por defecto del passwordField
        final char defaultEchoChar = passwordField.getEchoChar();

        // Listener para alternar la visibilidad de la contraseña
        lblNewLabel_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getEchoChar() == '\0') {
                    passwordField.setEchoChar(defaultEchoChar);
                } else {
                    passwordField.setEchoChar('\0'); // Muestra la contraseña sin enmascararla
                }
            }
        });
    }
}
