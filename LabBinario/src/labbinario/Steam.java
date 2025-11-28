package labbinario;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    }

    private Game readGameAtCurrent() throws IOException {
        int code = rgames.readInt();
        String titulo = rgames.readUTF();
        char so = rgames.readChar();
        int edadMinima = rgames.readInt();
        double precio = rgames.readDouble();
        int contador = rgames.readInt();
        String path = rgames.readUTF();
        return new Game(code, titulo, so, edadMinima, precio, contador, path);
    }

    private Player readPlayerAtCurrent() throws IOException {
        int code = rplayer.readInt();
        String user = rplayer.readUTF();
        String pass = rplayer.readUTF();
        String nombre = rplayer.readUTF();
        long nacimiento = rplayer.readLong();
        int contador = rplayer.readInt();
        String img = rplayer.readUTF();
        String tipo = rplayer.readUTF();
        return new Player(code, user, pass, nombre, nacimiento, contador, img, tipo);
    }

    private Game findGameByCode(int codeBuscado) throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            Game g = readGameAtCurrent();
            if (g.getCode() == codeBuscado) {
                return g;
            }
        }
        return null;
    }

    private Player findPlayerByCode(int codeBuscado) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            Player p = readPlayerAtCurrent();
            if (p.getCode() == codeBuscado) {
                return p;
            }
        }
        return null;
    }

    private int calcularEdad(long nacimientoMillis) {
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTimeInMillis(nacimientoMillis);
        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return edad;
    }

    private void incrementarDescargas(int gameCode, int clientCode) throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            long inicio = rgames.getFilePointer();
            Game g = readGameAtCurrent();
            if (g.getCode() == gameCode) {
                int nuevasDescargas = g.getContadorDownloads() + 1;
                rgames.seek(inicio);
                rgames.writeInt(g.getCode());
                rgames.writeUTF(g.getTitulo());
                rgames.writeChar(g.getSO());
                rgames.writeInt(g.getEdadMinima());
                rgames.writeDouble(g.getPrecio());
                rgames.writeInt(nuevasDescargas);
                rgames.writeUTF(g.getPath());
                break;
            }
        }

        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            long inicio = rplayer.getFilePointer();
            Player p = readPlayerAtCurrent();
            if (p.getCode() == clientCode) {
                int nuevasDescargas = p.getContadorDownloads() + 1;
                rplayer.seek(inicio);
                rplayer.writeInt(p.getCode());
                rplayer.writeUTF(p.getUserName());
                rplayer.writeUTF(p.getPassword());
                rplayer.writeUTF(p.getName());
                rplayer.writeLong(p.getNacimiento());
                rplayer.writeInt(nuevasDescargas);
                rplayer.writeUTF(p.getImagen());
                rplayer.writeUTF(p.getTipoUsuario());
                break;
            }
        }
    }

    public boolean downloadGame(int gameCode, int clientCode, char sisO) throws IOException {
        Game game = findGameByCode(gameCode);
        Player player = findPlayerByCode(clientCode);

        if (game == null || player == null) {
            return false;
        }

        if (game.getSO() != sisO) {
            return false;
        }

        int edad = calcularEdad(player.getNacimiento());
        if (edad < game.getEdadMinima()) {
            return false;
        }

        int downloadCode = getCode(3);
        File f = new File("steam/downloads/download_" + downloadCode + ".stm");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ahora = new Date();

        try (FileWriter fw = new FileWriter(f)) {
            fw.write(df.format(ahora) + "\n");
            fw.write(game.getPath() + "\n");
            fw.write("Descarga #" + downloadCode + "\n");
            fw.write(player.getName() + " ha bajado " + game.getTitulo() + "\n");
            fw.write("a un precio de $ " + game.getPrecio() + "." + "\n");
        }

        incrementarDescargas(gameCode, clientCode);
        return true;
    }

    public boolean updatePriceFor(int gameCode) throws IOException {
        Game g = findGameByCode(gameCode);

        if (g == null) {
            return false;
        } else {

        }
    }

}
