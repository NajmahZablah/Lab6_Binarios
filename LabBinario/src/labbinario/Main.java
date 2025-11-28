/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labbinario;

import GUI.SteamGUI;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
/**
 *
 * @author najma
 */
public class Main {

    public static void main(String[] args) {
        try {
            // 1. Inicializar la clase Steam (Singleton)
            System.out.println("Inicializando sistema Steam...");
            
            // El constructor de Steam ya se ejecuta automáticamente por ser Singleton
            // Esto crea los directorios y archivos necesarios
            
            // 2. Asegurar que exista un usuario administrador por defecto
            asegurarAdminPorDefecto();
            
            // 3. Lanzar la interfaz gráfica
            SwingUtilities.invokeLater(() -> {
                System.out.println("Iniciando interfaz gráfica...");
                SteamGUI gui = new SteamGUI();
                gui.setVisible(true);
                System.out.println("Sistema Steam iniciado correctamente");
            });
            
            // 4. Permitir probar todas las funciones principales del sistema
            // (La GUI permite esto de forma interactiva)
            
        } catch (Exception e) {
            System.err.println("Error al inicializar el sistema: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar el sistema Steam:\n" + e.getMessage(), 
                "Error Fatal", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Asegura que exista un usuario administrador por defecto en el sistema
     */
    private static void asegurarAdminPorDefecto() {
        try {
            Steam steam = Steam.getInstance();
            
            // Verificar si ya existe un admin
            // Si el archivo player.stm está vacío o no existe admin, crear uno
            java.io.RandomAccessFile rplayer = new java.io.RandomAccessFile("steam/player.stm", "r");
            
            if (rplayer.length() == 0) {
                rplayer.close();
                System.out.println("No se encontraron usuarios. Creando administrador por defecto...");
                
                // Crear admin por defecto
                // Usuario: admin, Password: admin123
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(1990, 0, 1); // 1 de enero de 1990
                long fechaNacimiento = cal.getTimeInMillis();
                
                steam.addPlayer("admin", "admin123", "Administrador", fechaNacimiento, 
                              "default.png", "admin");
                
                System.out.println("✓ Usuario administrador creado:");
                System.out.println("  Username: admin");
                System.out.println("  Password: admin123");
                System.out.println("  Tipo: admin");
                
            } else {
                rplayer.close();
                System.out.println("✓ Archivo de usuarios existente");
            }
            
        } catch (IOException e) {
            System.err.println("Error al verificar/crear admin por defecto: " + e.getMessage());
        }
    }
}
