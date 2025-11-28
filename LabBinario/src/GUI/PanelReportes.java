/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import labbinario.Steam;

public class PanelReportes extends JPanel {

    private Steam steam;
    private JTextArea areaReporte;
    private JTextField txtCodigoCliente, txtNombreArchivo;
    private JButton btnGenerarReporte;

    public PanelReportes(Steam steam) {
        this.steam = steam;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("REPORTES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(47, 60, 76));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.WHITE);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Generar Reporte de Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Código del Cliente:"), gbc);
        txtCodigoCliente = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtCodigoCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Nombre del Archivo:"), gbc);
        txtNombreArchivo = new JTextField(15);
        gbc.gridx = 1;
        panelForm.add(txtNombreArchivo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelBotonesForm = new JPanel(new FlowLayout());
        panelBotonesForm.setBackground(Color.WHITE);

        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setBackground(new Color(40, 167, 69));
        btnGenerarReporte.setForeground(Color.WHITE);
        btnGenerarReporte.setFocusPainted(false);
        btnGenerarReporte.addActionListener(e -> generarReporte());

        panelBotonesForm.add(btnGenerarReporte);
        panelForm.add(panelBotonesForm, gbc);

        panelCentral.add(panelForm, BorderLayout.NORTH);

        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaReporte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(areaReporte);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Vista Previa del Reporte"));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void generarReporte() {
        String codigoStr = txtCodigoCliente.getText().trim();
        String nombreArchivo = txtNombreArchivo.getText().trim();

        if (codigoStr.isEmpty() || nombreArchivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoStr);

            // Genera archivo con Steam
            steam.reportForClient(codigo, nombreArchivo);

            // Muestra preview usando getDownloadsForPlayer
            ArrayList<String[]> descargas = steam.getDownloadsForPlayer(codigo);
            areaReporte.setText("=== REPORTE DEL CLIENTE ===\n\n");
            areaReporte.append("Código: " + codigo + "\n");
            areaReporte.append("Total de descargas: " + descargas.size() + "\n\n");
            areaReporte.append("Juegos descargados:\n");
            for (String[] d : descargas) {
                areaReporte.append("  - " + d[0] + " (" + d[1] + ")\n");
            }
            areaReporte.append("\nArchivo generado: " + nombreArchivo + ".txt\n");

            JOptionPane.showMessageDialog(this, "REPORTE CREADO");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El código debe ser un número",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
