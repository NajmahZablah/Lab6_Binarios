package labbinario;

public class Game {

    private int code;
    private String titulo;
    private char SO;
    private int edadMinima;
    private double precio;
    private int contadorDownloads;
    private String path;

    public Game(int code, String titulo, char SO, int edadMinima, double precio, int contadorDownloads,
            String path) {
        this.code = code;
        this.titulo = titulo;
        this.SO = SO;
        this.edadMinima = edadMinima;
        this.precio = precio;
        this.contadorDownloads = contadorDownloads;
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public String getTitulo() {
        return titulo;
    }

    public char getSO() {
        return SO;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public double getPrecio() {
        return precio;
    }

    public int getContadorDownloads() {
        return contadorDownloads;
    }

    public String getPath() {
        return path;
    }

}
