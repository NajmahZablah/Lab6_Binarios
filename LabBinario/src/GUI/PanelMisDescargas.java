/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import labbinario.Steam;

public class PanelMisDescargas extends JPanel {

    private SteamGUI frame;
    private JTable tablaDescargas;
    private DefaultTableModel modeloTabla;
    private JLabel lblContador;
    
    public PanelMisDescargas(SteamGUI frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("MIS DESCARGAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        lblContador = new JLabel("Total de descargas: 0");
        lblContador.setFont(new Font("Arial", Font.BOLD, 16));
        lblContador.setForeground(new Color(102, 192, 244));
        panelSuperior.add(lblContador, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        String[] columnas = {"Juego", "Precio", "Fecha Descarga", "SO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDescargas = new JTable(modeloTabla);
        tablaDescargas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaDescargas.setRowHeight(30);
        tablaDescargas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaDescargas.getTableHeader().setBackground(new Color(47, 60, 76));
        tablaDescargas.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaDescargas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        
        cargarDescargas();
    }
    
    private void cargarDescargas() {
        modeloTabla.setRowCount(0);
        
        // Aquí puedes implementar la lógica para cargar las descargas del usuario
        // Por ahora dejamos datos de ejemplo
        
        modeloTabla.addRow(new Object[]{"The Witcher 3", "$29.99", "28/11/2025", "Windows"});
        modeloTabla.addRow(new Object[]{"Minecraft", "$19.99", "27/11/2025", "Mac"});
        modeloTabla.addRow(new Object[]{"Cyberpunk 2077", "$59.99", "26/11/2025", "Windows"});
        
        lblContador.setText("Total de descargas: " + modeloTabla.getRowCount());
    }
}