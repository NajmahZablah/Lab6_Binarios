/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.IOException;
import labbinario.Steam;
/**
 *
 * @author najma
 */
public class PanelPrintGames extends JPanel {
    private JTable tablaJuegos;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar, btnImprimir;
    private JTextArea areaTexto;
    
    public PanelPrintGames() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("TODOS LOS JUEGOS REGISTRADOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        
        btnRefrescar = crearBoton("Refrescar", new Color(102, 192, 244));
        btnImprimir = crearBoton("Imprimir en Consola", new Color(0, 123, 255));
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnImprimir);
        
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de juegos
        String[] columnas = {"Code", "Título", "SO", "Edad Mín.", "Precio", "Descargas", "Imagen"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaJuegos = new JTable(modeloTabla);
        tablaJuegos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaJuegos.setRowHeight(25);
        tablaJuegos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaJuegos.getTableHeader().setBackground(new Color(47, 60, 76));
        tablaJuegos.getTableHeader().setForeground(Color.WHITE);
        tablaJuegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar anchos de columnas
        tablaJuegos.getColumnModel().getColumn(0).setPreferredWidth(60);  // Code
        tablaJuegos.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaJuegos.getColumnModel().getColumn(2).setPreferredWidth(40);  // SO
        tablaJuegos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Edad
        tablaJuegos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Precio
        tablaJuegos.getColumnModel().getColumn(5).setPreferredWidth(80);  // Descargas
        tablaJuegos.getColumnModel().getColumn(6).setPreferredWidth(150); // Imagen
        
        JScrollPane scrollPane = new JScrollPane(tablaJuegos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Panel con pestañas (tabla y vista de texto)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        tabbedPane.addTab("Vista de Tabla", scrollPane);
        
        // Área de texto para mostrar en formato texto
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        
        tabbedPane.addTab("Vista de Texto", scrollTexto);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        btnRefrescar.addActionListener(e -> cargarJuegos());
        btnImprimir.addActionListener(e -> imprimirEnConsola());
        
        cargarJuegos();
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void cargarJuegos() {
        modeloTabla.setRowCount(0);
        StringBuilder textoCompleto = new StringBuilder();
        textoCompleto.append("========== JUEGOS REGISTRADOS ==========\n\n");
        
        try {
            java.io.RandomAccessFile rgames = new java.io.RandomAccessFile("steam/games.stm", "r");
            
            if (rgames.length() == 0) {
                modeloTabla.addRow(new Object[]{"", "No hay juegos registrados", "", "", "", "", ""});
                textoCompleto.append("No hay juegos registrados.\n");
            } else {
                int contador = 0;
                while (rgames.getFilePointer() < rgames.length()) {
                    int code = rgames.readInt();
                    String titulo = rgames.readUTF();
                    char so = rgames.readChar();
                    int edadMinima = rgames.readInt();
                    double precio = rgames.readDouble();
                    int contadorDescargas = rgames.readInt();
                    String path = rgames.readUTF();
                    
                    modeloTabla.addRow(new Object[]{
                        code, 
                        titulo, 
                        so, 
                        edadMinima, 
                        String.format("$%.2f", precio), 
                        contadorDescargas,
                        path
                    });
                    
                    // Agregar al texto
                    textoCompleto.append("----------------------------------------\n");
                    textoCompleto.append("Code: ").append(code).append("\n");
                    textoCompleto.append("Título: ").append(titulo).append("\n");
                    textoCompleto.append("Sistema Operativo: ").append(so).append("\n");
                    textoCompleto.append("Edad Mínima: ").append(edadMinima).append(" años\n");
                    textoCompleto.append("Precio: $").append(String.format("%.2f", precio)).append("\n");
                    textoCompleto.append("Descargas: ").append(contadorDescargas).append("\n");
                    textoCompleto.append("Imagen: ").append(path).append("\n\n");
                    
                    contador++;
                }
                
                textoCompleto.append("========================================\n");
                textoCompleto.append("Total de juegos: ").append(contador).append("\n");
            }
            
            rgames.close();
            
            areaTexto.setText(textoCompleto.toString());
            areaTexto.setCaretPosition(0); // Scroll al inicio
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar juegos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void imprimirEnConsola() {
        try {
            Steam.getInstance().printGames();
            JOptionPane.showMessageDialog(this, 
                "Juegos impresos en consola.\nRevisa la ventana de output.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al imprimir juegos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}