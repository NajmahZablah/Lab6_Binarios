/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import labbinario.Steam;
import java.io.File;
import java.util.Calendar;

/**
 *
 * @author najma
 */
public class SteamGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Usuario usuarioActual;
    
    // Paneles
    private PanelLogin panelLogin;
    private PanelAdmin panelAdmin;
    private PanelNormal panelNormal;
    
    public SteamGUI() {
        // Inicializar el sistema Steam y crear admin por defecto
        inicializarSistema();
        
        setTitle("Steam - Sistema de Gestión de VideoJuegos");
        setSize(1400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        panelLogin = new PanelLogin(this);
        panelAdmin = new PanelAdmin(this);
        panelNormal = new PanelNormal(this);
        
        mainPanel.add(panelLogin, "LOGIN");
        mainPanel.add(panelAdmin, "ADMIN");
        mainPanel.add(panelNormal, "NORMAL");
        
        add(mainPanel);
        
        mostrarPanel("LOGIN");
    }
    
    /**
     * Inicializa el sistema Steam y crea usuario admin por defecto si no existe
     */
    private void inicializarSistema() {
        try {
            // Inicializar Steam (crea directorios y archivos)
            Steam.getInstance();
            
            // Verificar si existe el archivo de players y si está vacío
            File archivoPlayers = new File("steam/player.stm");
            
            if (!archivoPlayers.exists() || archivoPlayers.length() == 0) {
                // Crear admin por defecto
                Calendar cal = Calendar.getInstance();
                cal.set(1990, 0, 1); // 1 de enero de 1990
                
                Steam.getInstance().addPlayer(
                    "admin", 
                    "admin123", 
                    "Administrador", 
                    cal.getTimeInMillis(), 
                    "default.png", 
                    "admin"
                );
                
                System.out.println("✓ Usuario administrador creado por defecto");
                System.out.println("  Username: admin");
                System.out.println("  Password: admin123");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar el sistema:\n" + e.getMessage(), 
                "Error Fatal", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public void cerrarSesion() {
        usuarioActual = null;
        mostrarPanel("LOGIN");
        panelLogin.limpiarCampos();
    }
}