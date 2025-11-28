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
public class PanelCatalogo extends JPanel {
    
    private SteamGUI frame;
    private JPanel panelJuegos;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltroSO;
    
    public PanelCatalogo(SteamGUI frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(27, 40, 56));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusqueda.setBackground(new Color(27, 40, 56));
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(Color.WHITE);
        
        txtBuscar = new JTextField(25);
        
        JLabel lblFiltro = new JLabel("Sistema Operativo:");
        lblFiltro.setForeground(Color.WHITE);
        
        cmbFiltroSO = new JComboBox<>(new String[]{"Todos", "Windows", "Mac", "Linux"});
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(102, 192, 244));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> cargarJuegos());
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(Box.createHorizontalStrut(20));
        panelBusqueda.add(lblFiltro);
        panelBusqueda.add(cmbFiltroSO);
        panelBusqueda.add(btnBuscar);
        
        add(panelBusqueda, BorderLayout.NORTH);
        
        panelJuegos = new JPanel(new GridLayout(0, 3, 20, 20));
        panelJuegos.setBackground(new Color(27, 40, 56));
        
        JScrollPane scrollPane = new JScrollPane(panelJuegos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(27, 40, 56));
        add(scrollPane, BorderLayout.CENTER);
        
        cargarJuegos();
    }
    
    private void cargarJuegos() {
        panelJuegos.removeAll();
        
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        String filtroSO = (String) cmbFiltroSO.getSelectedItem();
        
        try {
            java.io.RandomAccessFile rgames = new java.io.RandomAccessFile("steam/games.stm", "r");
            
            while (rgames.getFilePointer() < rgames.length()) {
                int code = rgames.readInt();
                String titulo = rgames.readUTF();
                char so = rgames.readChar();
                int edadMinima = rgames.readInt();
                double precio = rgames.readDouble();
                int contador = rgames.readInt();
                String path = rgames.readUTF();
                
                // Filtrar por búsqueda
                if (!busqueda.isEmpty() && !titulo.toLowerCase().contains(busqueda)) {
                    continue;
                }
                
                // Filtrar por sistema operativo
                String soNombre = convertirSO(so);
                if (!filtroSO.equals("Todos") && !filtroSO.equals(soNombre)) {
                    continue;
                }
                
                panelJuegos.add(crearTarjetaJuego(
                    code, titulo, soNombre, edadMinima, precio, path
                ));
            }
            
            rgames.close();
            
            // Si no hay juegos, mostrar mensaje
            if (panelJuegos.getComponentCount() == 0) {
                JLabel lblVacio = new JLabel("No se encontraron juegos");
                lblVacio.setForeground(Color.WHITE);
                lblVacio.setFont(new Font("Arial", Font.BOLD, 16));
                lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
                panelJuegos.add(lblVacio);
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar juegos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        panelJuegos.revalidate();
        panelJuegos.repaint();
    }
    
    private String convertirSO(char so) {
        switch (so) {
            case 'W':
                return "Windows";
            case 'M':
                return "Mac";
            case 'L':
                return "Linux";
            default:
                return "Desconocido";
        }
    }
    
    private char convertirSOChar(String so) {
        switch (so) {
            case "Windows":
                return 'W';
            case "Mac":
                return 'M';
            case "Linux":
                return 'L';
            default:
                return 'W';
        }
    }
    
    private JPanel crearTarjetaJuego(int code, String titulo, String so, 
                                     int edadMin, double precio, String rutaImagen) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(5, 5));
        tarjeta.setBackground(new Color(47, 60, 76));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(102, 192, 244), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setPreferredSize(new Dimension(300, 350));
        
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(280, 180));
        lblImagen.setBackground(Color.DARK_GRAY);
        lblImagen.setOpaque(true);
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setForeground(Color.WHITE);
        
        // Intentar cargar la imagen
        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
                lblImagen.setText("");
            } else {
                lblImagen.setText("(Imagen del Juego)");
            }
        } catch (Exception e) {
            lblImagen.setText("(Imagen del Juego)");
        }
        
        tarjeta.add(lblImagen, BorderLayout.NORTH);
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(47, 60, 76));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSO = new JLabel("SO: " + so);
        lblSO.setForeground(Color.LIGHT_GRAY);
        lblSO.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSO.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblEdad = new JLabel("Edad mínima: " + edadMin + " años");
        lblEdad.setForeground(Color.LIGHT_GRAY);
        lblEdad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEdad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", precio));
        lblPrecio.setForeground(new Color(178, 212, 56));
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblSO);
        panelInfo.add(lblEdad);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblPrecio);
        
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        
        JButton btnDescargar = new JButton("DESCARGAR");
        btnDescargar.setBackground(new Color(102, 192, 244));
        btnDescargar.setForeground(Color.WHITE);
        btnDescargar.setFont(new Font("Arial", Font.BOLD, 13));
        btnDescargar.setFocusPainted(false);
        btnDescargar.setBorderPainted(false);
        btnDescargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDescargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnDescargar.addActionListener(e -> mostrarDialogoDescarga(code, titulo, so, edadMin, precio));
        
        tarjeta.add(btnDescargar, BorderLayout.SOUTH);
        
        return tarjeta;
    }
    
    private void mostrarDialogoDescarga(int codeJuego, String titulo, 
                                       String soJuego, int edadMinima, double precio) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                    "Descargar: " + titulo, true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));
        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblInfo = new JLabel("<html><b>Confirmar Descarga</b><br><br>" +
                                    "Juego: " + titulo + "<br>" +
                                    "Precio: $" + String.format("%.2f", precio) + "<br>" +
                                    "Edad mínima: " + edadMinima + " años</html>");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelCentral.add(lblInfo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panelCentral.add(new JLabel("Sistema Operativo:"), gbc);
        
        JComboBox<String> cmbSO = new JComboBox<>(new String[]{"Windows", "Mac", "Linux"});
        cmbSO.setSelectedItem(soJuego);
        gbc.gridx = 1;
        panelCentral.add(cmbSO, gbc);
        
        dialog.add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnConfirmar = new JButton("Confirmar Descarga");
        btnConfirmar.setBackground(new Color(40, 167, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.addActionListener(e -> {
            String soSeleccionado = (String) cmbSO.getSelectedItem();
            char soChar = convertirSOChar(soSeleccionado);
            int codigoCliente = frame.getCodigoUsuarioActual();
            
            try {
                boolean descargaExitosa = Steam.getInstance().downloadGame(
                    codeJuego, codigoCliente, soChar
                );
                
                if (descargaExitosa) {
                    JOptionPane.showMessageDialog(dialog, 
                        "¡Descarga exitosa!\nRevisa 'Mis Descargas'",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "No se pudo realizar la descarga.\n" +
                        "Verifica:\n" +
                        "- Sistema operativo compatible\n" +
                        "- Edad mínima requerida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Error al descargar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}