/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import labbinario.Steam;
import labbinario.Player;

/**
 *
 * @author najma
 */
public class PanelLogin extends JPanel {
    
    private SteamGUI frame;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public PanelLogin(SteamGUI frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(new Color(27, 40, 56));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblLogo = new JLabel("STEAM", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 60));
        lblLogo.setForeground(new Color(102, 192, 244));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblLogo, gbc);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Videojuegos", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(lblSubtitulo, gbc);
        
        gbc.gridy = 2;
        gbc.ipady = 20;
        add(Box.createVerticalStrut(20), gbc);
        gbc.ipady = 0;
        
        JPanel panelForm = crearPanelFormulario();
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelForm, gbc);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(47, 60, 76));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(102, 192, 244), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblUsuario, gbc);
        
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);
        
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 3;
        panel.add(txtPassword, gbc);
        
        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(102, 192, 244));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setPreferredSize(new Dimension(250, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnLogin, gbc);
        
        btnLogin.addActionListener(e -> intentarLogin());
        txtPassword.addActionListener(e -> intentarLogin());
        
        return panel;
    }
    
    private void intentarLogin() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos!");
            return;
        }
        
        try {
            Player playerEncontrado = validarCredenciales(username, password);
            
            if (playerEncontrado != null) {
                Usuario usuario = new Usuario(
                    playerEncontrado.getUserName(), 
                    playerEncontrado.getTipoUsuario()
                );
                frame.setUsuarioActual(usuario);
                frame.setCodigoUsuarioActual(playerEncontrado.getCode());
                
                if (playerEncontrado.getTipoUsuario().equals("admin")) {
                    frame.mostrarPanel("ADMIN");
                } else {
                    frame.mostrarPanel("NORMAL");
                }
            } else {
                mostrarError("Usuario o contraseña incorrectos");
            }
            
        } catch (IOException ex) {
            mostrarError("Error al validar credenciales: " + ex.getMessage());
        }
    }
    
    private Player validarCredenciales(String username, String password) throws IOException {
        Steam steam = Steam.getInstance();
        java.io.RandomAccessFile rplayer = new java.io.RandomAccessFile("steam/player.stm", "r");
        
        try {
            while (rplayer.getFilePointer() < rplayer.length()) {
                int code = rplayer.readInt();
                String user = rplayer.readUTF();
                String pass = rplayer.readUTF();
                String nombre = rplayer.readUTF();
                long nacimiento = rplayer.readLong();
                int contador = rplayer.readInt();
                String img = rplayer.readUTF();
                String tipo = rplayer.readUTF();
                
                if (user.equals(username) && pass.equals(password)) {
                    return new Player(code, user, pass, nombre, nacimiento, contador, img, tipo);
                }
            }
        } finally {
            rplayer.close();
        }
        
        return null;
    }
    
    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}