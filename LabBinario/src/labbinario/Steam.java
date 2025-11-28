package labbinario;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Steam {

    private RandomAccessFile rcods, rgames, rplayer;
    private static final Steam INSTANCE = new Steam();

    private Steam() {
        try {
            File dir = new File("steam/downloads");
            if (!dir.mkdirs() && !dir.exists()) {
                throw new IOException("No se pudo crear steam/downloads");
            }
            rcods = new RandomAccessFile("steam/codes.stm", "rw");
            rgames = new RandomAccessFile("steam/games.stm", "rw");
            rplayer = new RandomAccessFile("steam/player.stm", "rw");
            initCodes();
        } catch (IOException e) {
            throw new RuntimeException("Fallo inicializando archivos: " + e.getMessage());
        }
    }

    private void initCodes() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1);
            rcods.writeInt(1);
            rcods.writeInt(1);
        }
    }

    private int getCode(int opt) throws IOException {
        rcods.seek(0);
        int codeJuego = rcods.readInt();
        long posCliente = rcods.getFilePointer();
        int codeClientes = rcods.readInt();
        long posDownloads = rcods.getFilePointer();
        int codeDownloads = rcods.readInt();
        int code;
        switch (opt) {
            case 1:
                code = codeJuego;
                rcods.seek(0);
                rcods.writeInt(codeJuego + 1);
                return code;
            case 2:
                code = codeClientes;
                rcods.seek(posCliente);
                rcods.writeInt(codeClientes + 1);
                return code;
            case 3:
                code = codeDownloads;
                rcods.seek(posDownloads);
                rcods.writeInt(codeDownloads + 1);
                return code;
            default:
                return -1;
        }
    }

    /* Estrucutra games.stm:
    int code
    String titulo
    char SistemaOperativo
    int edadMinima
    double precio
    int contadorDownloads
    String imagen
     */
    public void addGame(String titulo, char so, int edadMinima, double precio,
            String path) throws IOException {
        rgames.seek(rgames.length());
        rgames.writeInt(getCode(1));
        rgames.writeUTF(titulo);
        rgames.writeChar(so);
        rgames.writeInt(edadMinima);
        rgames.writeDouble(precio);
        rgames.writeInt(0); //contador de descargas (inicia en 0)
        rgames.writeUTF(path);
    }

    /* Estrucutra player.stm:
    int code
    String username
    String password
    String nombre
    long nacimiento
    int contadorDownloads
    String imagen
    String tipoUsuario
     */
    public void addPlayer(String u, String pswrd, String nombre, long nacimiento, String path,
            String tipoUsuario) throws IOException {
        rplayer.seek(rplayer.length());
        rplayer.writeInt(getCode(2)); //2 pues es la segunda opción del switch
        rplayer.writeUTF(u);
        rplayer.writeUTF(pswrd);
        rplayer.writeUTF(nombre);
        rplayer.writeLong(nacimiento);
        rplayer.writeInt(0);
        rplayer.writeUTF(path);
        rplayer.writeUTF(tipoUsuario);
        //talvez un boolean? para borrar cuenta (pero es idea nomás)
    }

    public boolean downloadGame(int gameCode, int clientCode, char sistemaOperativo) throws IOException {
    }

}
