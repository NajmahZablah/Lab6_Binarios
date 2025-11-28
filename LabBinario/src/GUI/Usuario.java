/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author najma
 */
public class Usuario {
    
    private String username;
    private String tipoUsuario;
    
    public Usuario(String username, String tipoUsuario) {
        this.username = username;
        this.tipoUsuario = tipoUsuario;
    }

    public String getUsername() {
        return username;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public boolean esAdmin() {
        return "admin".equals(tipoUsuario);
    }

    int getCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}