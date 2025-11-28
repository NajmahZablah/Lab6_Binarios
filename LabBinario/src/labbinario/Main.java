/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labbinario;

import GUI.SteamGUI;
import javax.swing.SwingUtilities;
/**
 *
 * @author najma
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SteamGUI gui = new SteamGUI();
            gui.setVisible(true);
        });
    }
}
