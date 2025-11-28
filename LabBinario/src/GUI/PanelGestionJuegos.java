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
public class PanelGestionJuegos extends JPanel {
    
    private JTable tablaJuegos;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnModificar, btnEliminar, btnCambiarPrecio, btnRefrescar;
    
    public PanelGestionJuegos() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE JUEGOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        
        btnRegistrar = crearBoton("Registrar Juego", new Color(40, 167, 69));
        btnModificar = crearBoton("Modificar", new Color(255, 193, 7));
        btnEliminar = crearBoton("Eliminar", new Color(220, 53, 69));
        btnCambiarPrecio = crearBoton("Cambiar Precio", new Color(0, 123, 255));
        btnRefrescar = crearBoton("Refrescar", new Color(102, 192, 244));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCambiarPrecio);
        panelBotones.add(btnRefrescar);
        
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        String[] columnas = {"Code", "Título", "SO", "Edad Mín.", "Precio", "Descargas"};
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
        
        JScrollPane scrollPane = new JScrollPane(tablaJuegos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        
        btnRegistrar.addActionListener(e -> mostrarDialogoRegistrar());
        btnModificar.addActionListener(e -> mostrarDialogoModificar());
        btnEliminar.addActionListener(e -> eliminarJuego());
        btnCambiarPrecio.addActionListener(e -> mostrarDialogoCambiarPrecio());
        btnRefrescar.addActionListener(e -> cargarJuegos());
        
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
        // Steam.printGames() o Steam.getAllGames()
        modeloTabla.setRowCount(0);
        
        modeloTabla.addRow(new Object[]{1, "The Witcher 3", "W", 18, 29.99, 150});
        modeloTabla.addRow(new Object[]{2, "Minecraft", "M", 7, 19.99, 300});
        modeloTabla.addRow(new Object[]{3, "Cyberpunk 2077", "L", 18, 59.99, 80});
    }
    
    private void mostrarDialogoRegistrar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                    "Registrar Nuevo Juego", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Título:"), gbc);
        JTextField txtTitulo = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Sistema Operativo:"), gbc);
        JComboBox<String> cmbSO = new JComboBox<>(new String[]{
            "Windows (W)", "Mac (M)", "Linux (L)"
        });
        gbc.gridx = 1;
        panelForm.add(cmbSO, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Edad Mínima:"), gbc);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 18, 1);
        JSpinner spnEdad = new JSpinner(spinnerModel);
        gbc.gridx = 1;
        panelForm.add(spnEdad, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Precio ($):"), gbc);
        JTextField txtPrecio = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtPrecio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Imagen:"), gbc);
        JPanel panelImagen = new JPanel(new BorderLayout(5, 5));
        JTextField txtRutaImagen = new JTextField(15);
        txtRutaImagen.setEditable(false);
        JButton btnSeleccionarImagen = new JButton("Seleccionar");
        btnSeleccionarImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                txtRutaImagen.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        panelImagen.add(txtRutaImagen, BorderLayout.CENTER);
        panelImagen.add(btnSeleccionarImagen, BorderLayout.EAST);
        gbc.gridx = 1;
        panelForm.add(panelImagen, gbc);
        
        dialog.add(panelForm, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> {
            // AQUÍ IRÍA: Steam.addGame(...)
            JOptionPane.showMessageDialog(dialog, "Juego registrado exitosamente");
            dialog.dispose();
            cargarJuegos();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoModificar() {
        int filaSeleccionada = tablaJuegos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego para modificar");
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Funcionalidad de modificar");
    }
    
    private void eliminarJuego() {
        int filaSeleccionada = tablaJuegos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego para eliminar");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este juego?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Steam.deleteGame(code)
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Juego eliminado exitosamente");
        }
    }
    
    private void mostrarDialogoCambiarPrecio() {
        int filaSeleccionada = tablaJuegos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un juego");
            return;
        }
        
        String precioActual = tablaJuegos.getValueAt(filaSeleccionada, 4).toString();
        String nuevoPrecio = JOptionPane.showInputDialog(this,
            "Precio actual: $" + precioActual + "\nIngrese el nuevo precio:",
            "Cambiar Precio",
            JOptionPane.QUESTION_MESSAGE);
        
        if (nuevoPrecio != null && !nuevoPrecio.trim().isEmpty()) {
            try {
                double precio = Double.parseDouble(nuevoPrecio);
                // AQUÍ IRÍA: Steam.updatePriceFor(code, precio)
                tablaJuegos.setValueAt(precio, filaSeleccionada, 4);
                JOptionPane.showMessageDialog(this, "Precio actualizado exitosamente");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}