/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author najma
 */
public class PanelVerDescargas extends JPanel {
    
    private JTable tablaDescargas;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar, btnVerDetalle;
    
    public PanelVerDescargas() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("DESCARGAS GENERADAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        
        btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.setBackground(new Color(0, 123, 255));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.addActionListener(e -> verDetalle());
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(102, 192, 244));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarDescargas());
        
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnRefrescar);
        
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        String[] columnas = {"Code Download", "Cliente", "Juego", "Precio", "Fecha", "SO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDescargas = new JTable(modeloTabla);
        tablaDescargas.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaDescargas.setRowHeight(25);
        tablaDescargas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaDescargas.getTableHeader().setBackground(new Color(47, 60, 76));
        tablaDescargas.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaDescargas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        
        cargarDescargas();
    }
    
    private void cargarDescargas() {
        // Leer todos los archivos de steam/downloads/
        modeloTabla.setRowCount(0);
        
        // Ejemplo
        modeloTabla.addRow(new Object[]{1, "user1", "The Witcher 3", 29.99, "28/11/2025", "W"});
        modeloTabla.addRow(new Object[]{2, "user2", "Minecraft", 19.99, "27/11/2025", "M"});
        modeloTabla.addRow(new Object[]{3, "user1", "Cyberpunk 2077", 59.99, "26/11/2025", "W"});
    }
    
    private void verDetalle() {
        int filaSeleccionada = tablaDescargas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una descarga");
            return;
        }
        
        // Leer el archivo download_X.stm y mostrarlo
        String codeDownload = tablaDescargas.getValueAt(filaSeleccionada, 0).toString();
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                    "Detalle de Descarga #" + codeDownload, true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JTextArea areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);
        areaDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Ssimulación
        areaDetalle.setText("[28/11/2025 14:35:22]\n\n");
        areaDetalle.append("(IMAGEN DEL JUEGO)\n\n");
        areaDetalle.append("Download #" + codeDownload + "\n\n");
        areaDetalle.append("Juan Pérez ha bajado The Witcher 3\n");
        areaDetalle.append("a un precio de $ 29.99.\n");
        
        dialog.add(new JScrollPane(areaDetalle));
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        dialog.add(panelBoton, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}