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
public class PanelAdmin extends JPanel {
    
    private SteamGUI frame;
    private JPanel panelMenu;
    private JPanel panelContenido;
    private CardLayout contenidoLayout;
    
    private PanelGestionPlayers panelPlayers;
    private PanelGestionJuegos panelJuegos;
    private PanelReportes panelReportes;
    private PanelVerDescargas panelDescargas;
    
    public PanelAdmin(SteamGUI frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        inicializarComponentes();
    }
    private void inicializarComponentes() {
        add(crearBarraSuperior(), BorderLayout.NORTH);
        
        panelMenu = crearMenuLateral();
        add(panelMenu, BorderLayout.WEST);
        
        contenidoLayout = new CardLayout();
        panelContenido = new JPanel(contenidoLayout);
        panelContenido.setBackground(Color.WHITE);
        
        panelPlayers = new PanelGestionPlayers();
        panelJuegos = new PanelGestionJuegos();
        panelReportes = new PanelReportes();
        panelDescargas = new PanelVerDescargas();
        
        panelContenido.add(panelPlayers, "PLAYERS");
        panelContenido.add(panelJuegos, "JUEGOS");
        panelContenido.add(panelReportes, "REPORTES");
        panelContenido.add(panelDescargas, "DESCARGAS");
        
        add(panelContenido, BorderLayout.CENTER);
    }
    
    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(27, 40, 56));
        barra.setPreferredSize(new Dimension(0, 60));
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(102, 192, 244)));
        
        JLabel lblLogo = new JLabel("  STEAM - ADMIN");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogo.setForeground(new Color(102, 192, 244));
        barra.add(lblLogo, BorderLayout.WEST);
        
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelDerecho.setBackground(new Color(27, 40, 56));
        
        JLabel lblUsuario = new JLabel("Admin: " + (frame.getUsuarioActual() != null ? 
            frame.getUsuarioActual().getUsername() : ""));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> frame.cerrarSesion());
        
        panelDerecho.add(lblUsuario);
        panelDerecho.add(btnCerrarSesion);
        
        barra.add(panelDerecho, BorderLayout.EAST);
        
        return barra;
    }
    
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(47, 60, 76));
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(102, 192, 244)));
        
        JLabel lblTitulo = new JLabel("  MENÚ PRINCIPAL");
        lblTitulo.setForeground(Color.LIGHT_GRAY);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setMaximumSize(new Dimension(220, 40));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(lblTitulo);
        menu.add(Box.createVerticalStrut(10));
        
        agregarBotonMenu(menu, "Gestión de Players", "PLAYERS");
        agregarBotonMenu(menu, "Gestión de Juegos", "JUEGOS");
        agregarBotonMenu(menu, "Ver Reportes", "REPORTES");
        agregarBotonMenu(menu, "Ver Descargas", "DESCARGAS");
        
        return menu;
    }
    
    private void agregarBotonMenu(JPanel menu, String texto, String panel) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(47, 60, 76));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(102, 192, 244));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(47, 60, 76));
            }
        });
        
        btn.addActionListener(e -> contenidoLayout.show(panelContenido, panel));
        
        menu.add(btn);
        menu.add(Box.createVerticalStrut(5));
    }
}