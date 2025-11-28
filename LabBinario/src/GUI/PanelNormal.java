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
public class PanelNormal extends JPanel {
    
    private SteamGUI frame;
    private JPanel panelContenido;
    private CardLayout contenidoLayout;
    
    private PanelCatalogo panelCatalogo;
    private PanelMisDescargas panelMisDescargas;
    private PanelMiPerfil panelMiPerfil;
    
    public PanelNormal(SteamGUI frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        add(crearBarraSuperior(), BorderLayout.NORTH);
        
        contenidoLayout = new CardLayout();
        panelContenido = new JPanel(contenidoLayout);
        
        panelCatalogo = new PanelCatalogo(frame);
        panelMisDescargas = new PanelMisDescargas();
        panelMiPerfil = new PanelMiPerfil();
        
        panelContenido.add(panelCatalogo, "CATALOGO");
        panelContenido.add(panelMisDescargas, "MIS_DESCARGAS");
        panelContenido.add(panelMiPerfil, "MI_PERFIL");
        
        add(panelContenido, BorderLayout.CENTER);
        
        contenidoLayout.show(panelContenido, "CATALOGO");
    }
    
    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(27, 40, 56));
        barra.setPreferredSize(new Dimension(0, 70));
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(102, 192, 244)));
        
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelIzquierdo.setBackground(new Color(27, 40, 56));
        
        JLabel lblLogo = new JLabel("STEAM");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(new Color(102, 192, 244));
        
        JLabel lblBienvenida = new JLabel("Bienvenido, " + 
            (frame.getUsuarioActual() != null ? frame.getUsuarioActual().getUsername() : ""));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panelIzquierdo.add(lblLogo);
        panelIzquierdo.add(Box.createHorizontalStrut(30));
        panelIzquierdo.add(lblBienvenida);
        
        barra.add(panelIzquierdo, BorderLayout.WEST);
        
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelCentro.setBackground(new Color(27, 40, 56));
        
        JButton btnCatalogo = crearBotonMenu("Catálogo", "CATALOGO");
        JButton btnMisDescargas = crearBotonMenu("Mis Descargas", "MIS_DESCARGAS");
        JButton btnMiPerfil = crearBotonMenu("Mi Perfil", "MI_PERFIL");
        
        panelCentro.add(btnCatalogo);
        panelCentro.add(btnMisDescargas);
        panelCentro.add(btnMiPerfil);
        
        barra.add(panelCentro, BorderLayout.CENTER);
        
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelDerecho.setBackground(new Color(27, 40, 56));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> frame.cerrarSesion());
        
        panelDerecho.add(btnCerrarSesion);
        barra.add(panelDerecho, BorderLayout.EAST);
        
        return barra;
    }
    
    private JButton crearBotonMenu(String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(47, 60, 76));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(102, 192, 244));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(47, 60, 76));
            }
        });
        
        btn.addActionListener(e -> contenidoLayout.show(panelContenido, panel));
        
        return btn;
    }
}