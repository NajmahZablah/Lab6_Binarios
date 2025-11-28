/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labbinario;

/**
 *
 * @author DELL
 */
public class Player {

    private int code;
    private String userName;
    private String password;
    private String name;
    private long nacimiento;
    private int contadorDownloads;
    private String imagen;
    private String tipoUsuario;

    public Player(int code, String userName, String password, String name, long nacimiento,
            int contadorDownloads, String imagen, String tipoUsuario) {

        this.code = code;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.nacimiento = nacimiento;
        this.contadorDownloads = contadorDownloads;
        this.imagen = imagen;
        this.tipoUsuario = tipoUsuario;
    }

    public int getCode() {
        return code;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public long getNacimiento() {
        return nacimiento;
    }

    public int getContadorDownloads() {
        return contadorDownloads;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

}
