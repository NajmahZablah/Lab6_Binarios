/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import labbinario.Steam;

/**
 *
 * @author najma
 */
public class PanelMiPerfil extends JPanel {
   
    private SteamGUI frame;
    private JLabel lblImagenPerfil;
    private JTextField txtNombre, txtUsername;
    private JPasswordField txtPassword, txtConfirmarPassword;
    private JLabel lblDescargas;
    private String rutaImagenActual;
    
    public PanelMiPerfil(SteamGUI frame) {
        this.frame = frame;
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Título
        JLabel lblTitulo = new JLabel("MI PERFIL");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        add(lblTitulo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setPreferredSize(new Dimension(200, 200));
        lblImagenPerfil.setBackground(Color.LIGHT_GRAY);
        lblImagenPerfil.setOpaque(true);
        lblImagenPerfil.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblImagenPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPerfil.setVerticalAlignment(SwingConstants.CENTER);
        lblImagenPerfil.setText("(Sin Imagen)");
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 4;
        panelCentral.add(lblImagenPerfil, gbc);
        
        JButton btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setBackground(new Color(102, 192, 244));
        btnCambiarImagen.setForeground(Color.WHITE);
        btnCambiarImagen.setFocusPainted(false);
        btnCambiarImagen.addActionListener(e -> cambiarImagen());
        gbc.gridy = 4; gbc.gridheight = 1;
        panelCentral.add(btnCambiarImagen, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        panelCentral.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(25);
        txtUsername.setEditable(false);
        txtUsername.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 2;
        panelCentral.add(txtUsername, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panelCentral.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(25);
        gbc.gridx = 2;
        panelCentral.add(txtNombre, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panelCentral.add(new JLabel("Nueva Contraseña:"), gbc);
        txtPassword = new JPasswordField(25);
        gbc.gridx = 2;
        panelCentral.add(txtPassword, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        panelCentral.add(new JLabel("Confirmar Contraseña:"), gbc);
        txtConfirmarPassword = new JPasswordField(25);
        gbc.gridx = 2;
        panelCentral.add(txtConfirmarPassword, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        panelCentral.add(new JLabel("Total Descargas:"), gbc);
        lblDescargas = new JLabel("0");
        lblDescargas.setFont(new Font("Arial", Font.BOLD, 16));
        lblDescargas.setForeground(new Color(102, 192, 244));
        gbc.gridx = 2;
        panelCentral.add(lblDescargas, gbc);
        
        add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(180, 40));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
        
        cargarDatosPerfil();
    }
    
    private void cargarDatosPerfil() {
        int codigoUsuario = frame.getCodigoUsuarioActual();
        
        try {
            java.io.RandomAccessFile rplayer = new java.io.RandomAccessFile("steam/player.stm", "r");
            
            while (rplayer.getFilePointer() < rplayer.length()) {
                int code = rplayer.readInt();
                String user = rplayer.readUTF();
                String pass = rplayer.readUTF();
                String nombre = rplayer.readUTF();
                long nacimiento = rplayer.readLong();
                int contador = rplayer.readInt();
                String img = rplayer.readUTF();
                String tipo = rplayer.readUTF();
                
                if (code == codigoUsuario) {
                    txtUsername.setText(user);
                    txtNombre.setText(nombre);
                    lblDescargas.setText(String.valueOf(contador));
                    rutaImagenActual = img;
                    
                    // Intentar cargar la imagen
                    try {
                        ImageIcon icon = new ImageIcon(img);
                        if (icon.getIconWidth() > 0) {
                            Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            lblImagenPerfil.setIcon(new ImageIcon(image));
                            lblImagenPerfil.setText("");
                        }
                    } catch (Exception e) {
                        lblImagenPerfil.setText("(Sin Imagen)");
                    }
                    
                    break;
                }
            }
            
            rplayer.close();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar datos del perfil: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cambiarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            rutaImagenActual = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Cargar y mostrar la imagen
            try {
                ImageIcon icon = new ImageIcon(rutaImagenActual);
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblImagenPerfil.setIcon(new ImageIcon(img));
                lblImagenPerfil.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar la imagen", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmarPassword.getPassword());
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre no puede estar vacío", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.isEmpty() && !password.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, 
                "Las contraseñas no coinciden", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int codigoUsuario = frame.getCodigoUsuarioActual();
        String tipoUsuario = frame.getUsuarioActual().getTipoUsuario();
        
        // Si el password está vacío, mantener el actual
        String passwordFinal = password.isEmpty() ? obtenerPasswordActual(codigoUsuario) : password;
        
        try {
            boolean actualizado = Steam.getInstance().modificarPlayer(
                codigoUsuario, 
                passwordFinal, 
                nombre, 
                rutaImagenActual != null ? rutaImagenActual : "default.png", 
                tipoUsuario
            );
            
            if (actualizado) {
                JOptionPane.showMessageDialog(this, 
                    "Perfil actualizado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                txtPassword.setText("");
                txtConfirmarPassword.setText("");
                cargarDatosPerfil();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo actualizar el perfil", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al actualizar perfil: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String obtenerPasswordActual(int codigoUsuario) {
        try {
            java.io.RandomAccessFile rplayer = new java.io.RandomAccessFile("steam/player.stm", "r");
            
            while (rplayer.getFilePointer() < rplayer.length()) {
                int code = rplayer.readInt();
                String user = rplayer.readUTF();
                String pass = rplayer.readUTF();
                String nombre = rplayer.readUTF();
                long nacimiento = rplayer.readLong();
                int contador = rplayer.readInt();
                String img = rplayer.readUTF();
                String tipo = rplayer.readUTF();
                
                if (code == codigoUsuario) {
                    rplayer.close();
                    return pass;
                }
            }
            
            rplayer.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return "";
    }
}