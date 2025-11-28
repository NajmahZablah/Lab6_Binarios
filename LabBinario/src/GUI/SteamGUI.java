/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;

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
