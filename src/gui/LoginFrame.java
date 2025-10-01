package gui;

import services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        this.userService = new UserService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Sistema de Gestión - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Título
        JLabel titleLabel = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel usernameLabel = new JLabel("Usuario:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField();
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        
        JButton registerButton = new JButton("Registrar Usuario");
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        formPanel.add(new JLabel()); // Espacio vacío
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Action Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
        
        // Enter key listener for login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        add(mainPanel);
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userService.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido " + username + "!", 
                                        "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cerrar ventana de login
            new MainFrame().setVisible(true); // Abrir ventana principal
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", 
                                        "Error de Login", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    private void showRegisterDialog() {
        JTextField newUsername = new JTextField();
        JPasswordField newPassword = new JPasswordField();
        JTextField newEmail = new JTextField();
        JTextField newRole = new JTextField();
        
        Object[] message = {
            "Usuario:", newUsername,
            "Contraseña:", newPassword,
            "Email:", newEmail,
            "Rol:", newRole
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Nuevo Usuario", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String username = newUsername.getText().trim();
            String password = new String(newPassword.getPassword());
            String email = newEmail.getText().trim();
            String role = newRole.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            UserService userService = new UserService();
            userService.addUser(new models.User(username, password, email, role));
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", 
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}