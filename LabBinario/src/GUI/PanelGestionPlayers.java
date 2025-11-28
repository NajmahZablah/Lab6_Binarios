/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Calendar;

/**
 *
 * @author najma
 */
public class PanelGestionPlayers extends JPanel {

    private JTable tablaPlayers;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnModificar, btnEliminar, btnRefrescar;
    
    public PanelGestionPlayers() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE PLAYERS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(Color.WHITE);
        
        btnRegistrar = crearBoton("Registrar Player", new Color(40, 167, 69));
        btnModificar = crearBoton("Modificar", new Color(255, 193, 7));
        btnEliminar = crearBoton("Eliminar", new Color(220, 53, 69));
        btnRefrescar = crearBoton("Refrescar", new Color(102, 192, 244));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        String[] columnas = {"Code", "Username", "Nombre", "Fecha Nac.", "Tipo", "Descargas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPlayers = new JTable(modeloTabla);
        tablaPlayers.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPlayers.setRowHeight(25);
        tablaPlayers.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaPlayers.getTableHeader().setBackground(new Color(47, 60, 76));
        tablaPlayers.getTableHeader().setForeground(Color.WHITE);
        tablaPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaPlayers);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        
        btnRegistrar.addActionListener(e -> mostrarDialogoRegistrar());
        btnModificar.addActionListener(e -> mostrarDialogoModificar());
        btnEliminar.addActionListener(e -> eliminarPlayer());
        btnRefrescar.addActionListener(e -> cargarPlayers());
        
        cargarPlayers();
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
        return btn;
    }
    
    private void cargarPlayers() {
        // AQUÍ IRÍA LA LÓGICA: Steam.getAllPlayers()
        modeloTabla.setRowCount(0);
        
        modeloTabla.addRow(new Object[]{1, "user1", "Juan Pérez", "01/01/2000", "normal", 5});
        modeloTabla.addRow(new Object[]{2, "admin", "Admin User", "15/05/1990", "admin", 0});
        modeloTabla.addRow(new Object[]{3, "maria123", "María García", "20/08/1995", "normal", 12});
    }
    
    private void mostrarDialogoRegistrar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Registrar Nuevo Player", true);
        dialog.setSize(550, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelForm.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblUsername, gbc);
        
        JTextField txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblPassword, gbc);
        
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
        lblConfirmarPassword.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblConfirmarPassword, gbc);
        
        JPasswordField txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtConfirmarPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblNombre, gbc);
        
        JTextField txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblFechaNac = new JLabel("Fecha de Nacimiento:");
        lblFechaNac.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblFechaNac, gbc);
        
        JPanel panelFecha = crearPanelFecha();
        gbc.gridx = 1;
        panelForm.add(panelFecha, gbc);
        
        JSpinner spnDia = (JSpinner) panelFecha.getComponent(0);
        JComboBox<String> cmbMes = (JComboBox<String>) panelFecha.getComponent(2);
        JSpinner spnAnio = (JSpinner) panelFecha.getComponent(4);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblTipo, gbc);
        
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"normal", "admin"});
        cmbTipo.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(cmbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblImagen = new JLabel("Imagen de Perfil:");
        lblImagen.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblImagen, gbc);
        
        JPanel panelImagen = new JPanel(new BorderLayout(5, 5));
        panelImagen.setBackground(Color.WHITE);
        
        JTextField txtRutaImagen = new JTextField(15);
        txtRutaImagen.setEditable(false);
        txtRutaImagen.setFont(new Font("Arial", Font.PLAIN, 11));
        txtRutaImagen.setBackground(Color.LIGHT_GRAY);
        
        JButton btnSeleccionarImagen = new JButton("Seleccionar");
        btnSeleccionarImagen.setBackground(new Color(102, 192, 244));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
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
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JSeparator separator = new JSeparator();
        panelForm.add(separator, gbc);
        gbc.gridwidth = 1;
        
        dialog.add(panelForm, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnGuardar = new JButton("Guardar Player");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(140, 35));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmar = new String(txtConfirmarPassword.getPassword());
            String nombre = txtNombre.getText().trim();
            String tipo = (String) cmbTipo.getSelectedItem();
            String rutaImagen = txtRutaImagen.getText();
            
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "El username no puede estar vacío", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "La contraseña no puede estar vacía", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmar)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Las contraseñas no coinciden", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "El nombre no puede estar vacío", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int dia = (int) spnDia.getValue();
            int mes = cmbMes.getSelectedIndex() + 1;
            int anio = (int) spnAnio.getValue();
            
            if (!esFechaValida(dia, mes, anio)) {
                JOptionPane.showMessageDialog(dialog, 
                    "La fecha ingresada no es válida", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(anio, mes - 1, dia, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long fechaNacimientoLong = calendar.getTimeInMillis();
            
            // AQUÍ IRÍA: Steam.addPlayer(username, password, nombre, fechaNacimientoLong, tipo, rutaImagen)
            
            String fechaFormateada = String.format("%02d/%02d/%04d", dia, mes, anio);
            
            String mensaje = String.format(
                "Player registrado exitosamente:\n\n" +
                "Username: %s\n" +
                "Nombre: %s\n" +
                "Fecha Nac.: %s\n" +
                "Tipo: %s",
                username, nombre, fechaFormateada, tipo
            );
            
            JOptionPane.showMessageDialog(dialog, mensaje, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            cargarPlayers();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel crearPanelFecha() {
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFecha.setBackground(Color.WHITE);
        
        SpinnerNumberModel modeloDia = new SpinnerNumberModel(1, 1, 31, 1);
        JSpinner spnDia = new JSpinner(modeloDia);
        spnDia.setPreferredSize(new Dimension(60, 25));
        JSpinner.NumberEditor editorDia = new JSpinner.NumberEditor(spnDia, "00");
        spnDia.setEditor(editorDia);
        
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        JComboBox<String> cmbMes = new JComboBox<>(meses);
        cmbMes.setPreferredSize(new Dimension(110, 25));
        
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        SpinnerNumberModel modeloAnio = new SpinnerNumberModel(2000, 1900, anioActual, 1);
        JSpinner spnAnio = new JSpinner(modeloAnio);
        spnAnio.setPreferredSize(new Dimension(80, 25));
        
        panelFecha.add(spnDia);
        panelFecha.add(new JLabel("de"));
        panelFecha.add(cmbMes);
        panelFecha.add(new JLabel("de"));
        panelFecha.add(spnAnio);
        
        return panelFecha;
    }
    
    private boolean esFechaValida(int dia, int mes, int anio) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false); // Modo estricto para validación
            calendar.set(anio, mes - 1, dia);
            calendar.getTime(); // Esto lanza excepción si la fecha es inválida
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void mostrarDialogoModificar() {
        int filaSeleccionada = tablaPlayers.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un player de la tabla para modificar", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int code = (int) tablaPlayers.getValueAt(filaSeleccionada, 0);
        String usernameActual = (String) tablaPlayers.getValueAt(filaSeleccionada, 1);
        String nombreActual = (String) tablaPlayers.getValueAt(filaSeleccionada, 2);
        String fechaActual = (String) tablaPlayers.getValueAt(filaSeleccionada, 3);
        String tipoActual = (String) tablaPlayers.getValueAt(filaSeleccionada, 4);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Modificar Player - Code: " + code, true);
        dialog.setSize(550, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelForm.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblUsername, gbc);
        
        JTextField txtUsername = new JTextField(20);
        txtUsername.setText(usernameActual);
        txtUsername.setEditable(false);
        txtUsername.setBackground(Color.LIGHT_GRAY);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPassword = new JLabel("Nueva Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblPassword, gbc);
        
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblConfirmarPassword = new JLabel("Confirmar Password:");
        lblConfirmarPassword.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblConfirmarPassword, gbc);
        
        JPasswordField txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtConfirmarPassword, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        JLabel lblNota = new JLabel("(Dejar vacío si no desea cambiar la contraseña)");
        lblNota.setFont(new Font("Arial", Font.ITALIC, 10));
        lblNota.setForeground(Color.GRAY);
        panelForm.add(lblNota, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblNombre, gbc);
        
        JTextField txtNombre = new JTextField(20);
        txtNombre.setText(nombreActual);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblFechaNac = new JLabel("Fecha de Nacimiento:");
        lblFechaNac.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblFechaNac, gbc);
        
        JPanel panelFecha = crearPanelFecha();
        
        try {
            String[] partes = fechaActual.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);
            
            JSpinner spnDia = (JSpinner) panelFecha.getComponent(0);
            JComboBox<String> cmbMes = (JComboBox<String>) panelFecha.getComponent(2);
            JSpinner spnAnio = (JSpinner) panelFecha.getComponent(4);
            
            spnDia.setValue(dia);
            cmbMes.setSelectedIndex(mes - 1);
            spnAnio.setValue(anio);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        gbc.gridx = 1;
        panelForm.add(panelFecha, gbc);
        
        JSpinner spnDia = (JSpinner) panelFecha.getComponent(0);
        JComboBox<String> cmbMes = (JComboBox<String>) panelFecha.getComponent(2);
        JSpinner spnAnio = (JSpinner) panelFecha.getComponent(4);
        
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblTipo, gbc);
        
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"normal", "admin"});
        cmbTipo.setSelectedItem(tipoActual);
        cmbTipo.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelForm.add(cmbTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblImagen = new JLabel("Imagen de Perfil:");
        lblImagen.setFont(new Font("Arial", Font.BOLD, 13));
        panelForm.add(lblImagen, gbc);
        
        JPanel panelImagen = new JPanel(new BorderLayout(5, 5));
        panelImagen.setBackground(Color.WHITE);
        
        JTextField txtRutaImagen = new JTextField(15);
        txtRutaImagen.setEditable(false);
        txtRutaImagen.setFont(new Font("Arial", Font.PLAIN, 11));
        txtRutaImagen.setBackground(Color.LIGHT_GRAY);
        // AQUÍ IRÍA: Cargar la ruta de imagen actual del player
        
        JButton btnSeleccionarImagen = new JButton("Cambiar");
        btnSeleccionarImagen.setBackground(new Color(102, 192, 244));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
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
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(255, 193, 7));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(160, 35));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> {
            String password = new String(txtPassword.getPassword());
            String confirmar = new String(txtConfirmarPassword.getPassword());
            String nombre = txtNombre.getText().trim();
            String tipo = (String) cmbTipo.getSelectedItem();
            String rutaImagen = txtRutaImagen.getText();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "El nombre no puede estar vacío", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.isEmpty() && !password.equals(confirmar)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Las contraseñas no coinciden", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int dia = (int) spnDia.getValue();
            int mes = cmbMes.getSelectedIndex() + 1;
            int anio = (int) spnAnio.getValue();
            
            if (!esFechaValida(dia, mes, anio)) {
                JOptionPane.showMessageDialog(dialog, 
                    "La fecha ingresada no es válida", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(anio, mes - 1, dia, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long fechaNacimientoLong = calendar.getTimeInMillis();
            
            // AQUÍ IRÍA: Steam.updatePlayer(code, password.isEmpty() ? null : password, 
            //                               nombre, fechaNacimientoLong, tipo, rutaImagen)
            
            JOptionPane.showMessageDialog(dialog, 
                "Player modificado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            cargarPlayers();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void eliminarPlayer() {
        int filaSeleccionada = tablaPlayers.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un player de la tabla para eliminar", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int code = (int) tablaPlayers.getValueAt(filaSeleccionada, 0);
        String username = (String) tablaPlayers.getValueAt(filaSeleccionada, 1);
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el player?\n\n" +
            "Code: " + code + "\n" +
            "Username: " + username,
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // AQUÍ IRÍA: Steam.deletePlayer(code)
            
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, 
                "Player eliminado exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}