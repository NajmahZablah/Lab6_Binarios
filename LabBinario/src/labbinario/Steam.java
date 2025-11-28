package labbinario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
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
    
    public static Steam getInstance() {
        return INSTANCE;
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

        if (game == null || player == null) 
            return false;
        if (game.getSO() != sisO) 
            return false;

        int edad = calcularEdad(player.getNacimiento());
        if (edad < game.getEdadMinima()) 
            return false;

        int downloadCode = getCode(3);
        File f = new File("steam/downloads/download_" + downloadCode + ".stm");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ahora = new Date();

        try (FileWriter fw = new FileWriter(f)) {
            fw.write(df.format(ahora) + "\n");   
            fw.write(game.getPath() + "\n");           
            fw.write("Download #" + downloadCode + "\n");  
            fw.write(player.getName() + " ha bajado " + game.getTitulo() + "\n"); 
            fw.write("a un precio de $ " + game.getPrecio() + "\n");             
            fw.write(clientCode + "\n");                 
            fw.write(sisO + "\n");                         
        }

        incrementarDescargas(gameCode, clientCode);
        return true;
    }
    
    public ArrayList<String[]> getDownloadsForPlayer(int playerCode) {
        ArrayList<String[]> descargas = new ArrayList<>();
        File dir = new File("steam/downloads");
        if (!dir.exists() || !dir.isDirectory()) return descargas;

        File[] files = dir.listFiles((d, name) -> name.startsWith("download_") && name.endsWith(".stm"));
        if (files == null) return descargas;

        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String fecha = br.readLine();         
                br.readLine();                         
                br.readLine();                         
                String lineaDesc = br.readLine();    
                br.readLine();                         
                int filePlayerCode = Integer.parseInt(br.readLine());
                char so = br.readLine().charAt(0);   

                if (filePlayerCode != playerCode) continue;

                String juego = lineaDesc.split("ha bajado")[1].trim();

                String precio = br.readLine().replace("a un precio de $", "").trim();

                descargas.add(new String[]{juego, precio, fecha, String.valueOf(so)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return descargas;
    }

    public boolean updatePriceFor(int gameCode, double nuevoPrecio) throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            rgames.getFilePointer();
            int code = rgames.readInt();
            rgames.readUTF();
            rgames.readChar();
            rgames.readInt();

            if (code == gameCode) {
                rgames.writeDouble(nuevoPrecio);
                return true;
            }

            //se salta estos campos si el juego no es el mismo
            rgames.readDouble();
            rgames.readInt();
            rgames.readUTF();
        }
        return false;
    }

    public void reportForClient(int code, String filename) {
        try {
            Player p = findPlayerByCode(code);
            if (p == null) {
                System.out.println("Player no encontrado");
                return;
            }

            File f = new File("steam/" + filename + ".txt");  // Agregado .txt
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                int edad = calcularEdad(p.getNacimiento());

                pw.println("REPORTE DEL CLIENTE");
                pw.println("====================");
                pw.println("Código: " + p.getCode());
                pw.println("Nombre: " + p.getName());
                pw.println("Username: " + p.getUserName());
                pw.println("Tipo de usuario: " + p.getTipoUsuario());
                pw.println("Fecha de nacimiento: " + df.format(new Date(p.getNacimiento())));
                pw.println("Edad: " + edad);
                pw.println("Total de descargas: " + p.getContadorDownloads());
                pw.println("Imagen: " + p.getImagen());
                pw.println();
                pw.println("JUEGOS DESCARGADOS:");
                pw.println("-------------------");
                
                ArrayList<String[]> descargas = getDownloadsForPlayer(code);
                for (String[] d : descargas) {
                    pw.println("- " + d[0] + " ($" + d[1] + ") - " + d[2]);
                }
            }

        } catch (IOException e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }

    public boolean modificarPlayer(int codePlayer, String newPass, String newName, String newPhoto, String newTypeUser) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            long inicio = rplayer.getFilePointer();
            Player p = readPlayerAtCurrent();
            if (p.getCode() == codePlayer) {
                rplayer.seek(inicio);
                rplayer.readInt();
                rplayer.readUTF();
                rplayer.writeUTF(newPass);
                rplayer.writeUTF(newName);
                rplayer.skipBytes(8 + 4);
                rplayer.writeUTF(newPhoto);
                rplayer.writeUTF(newTypeUser);
                return true;
            }
        }
        return false;
    }

    public boolean deletePlayer(int code) throws IOException {
        File original = new File("steam/player.stm");
        File temporal = new File("steam/player_temp.stm");
        boolean eliminado = false;

        try (
                RandomAccessFile origen = new RandomAccessFile(original, "r"); 
                RandomAccessFile destino = new RandomAccessFile(temporal, "rw")) {
            while (origen.getFilePointer() < origen.length()) {
                long pos = origen.getFilePointer();
                Player p = readPlayerAtCurrent(origen);

                if (p.getCode() != code) {
                    origen.seek(pos);
                    copiarRegistro(origen, destino);
                } else {
                    eliminado = true;
                }
            }
        }

        if (eliminado) {
            if (original.delete()) {
                temporal.renameTo(original);
                rplayer = new RandomAccessFile("steam/player.stm", "rw");
            } else {
                throw new IOException("No se pudo eliminar el archivo original.");
            }
        } else {
            temporal.delete();
        }

        return eliminado;
    }

    private Player readPlayerAtCurrent(RandomAccessFile raf) throws IOException {
        int code = raf.readInt();
        String user = raf.readUTF();
        String pass = raf.readUTF();
        String nombre = raf.readUTF();
        long nacimiento = raf.readLong();
        int contador = raf.readInt();
        String img = raf.readUTF();
        String tipo = raf.readUTF();
        return new Player(code, user, pass, nombre, nacimiento, contador, img, tipo);
    }

    private void copiarRegistro(RandomAccessFile origen, RandomAccessFile destino) throws IOException {
        int code = origen.readInt();
        String user = origen.readUTF();
        String pass = origen.readUTF();
        String nombre = origen.readUTF();
        long nacimiento = origen.readLong();
        int contador = origen.readInt();
        String img = origen.readUTF();
        String tipo = origen.readUTF();

        destino.writeInt(code);
        destino.writeUTF(user);
        destino.writeUTF(pass);
        destino.writeUTF(nombre);
        destino.writeLong(nacimiento);
        destino.writeInt(contador);
        destino.writeUTF(img);
        destino.writeUTF(tipo);
    }

    public boolean modificarJuego(int codeGame, String newTitle, char newSO, int newEdad, String newImage) throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            long inicio = rgames.getFilePointer();
            Game g = readGameAtCurrent();
            if (g.getCode() == codeGame) {
                rgames.seek(inicio);
                rgames.writeInt(g.getCode());
                rgames.writeUTF(newTitle);
                rgames.writeChar(newSO);
                rgames.writeInt(newEdad);
                rgames.writeDouble(g.getPrecio());
                rgames.writeInt(g.getContadorDownloads());
                rgames.writeUTF(newImage);
                return true;
            }
        }
        return false;
    }

    public boolean deleteGame(int codeGame) throws IOException {
        File original = new File("steam/games.stm");
        File temporal = new File("steam/games_temp.stm");
        boolean eliminado = false;

        try (
                RandomAccessFile origen = new RandomAccessFile(original, "r"); 
                RandomAccessFile destino = new RandomAccessFile(temporal, "rw")) {
            while (origen.getFilePointer() < origen.length()) {
                long pos = origen.getFilePointer();
                Game g = readGameAtCurrent(origen);

                if (g.getCode() != codeGame) {
                    origen.seek(pos);
                    copiarRegistroJuego(origen, destino);
                } else {
                    eliminado = true;
                }
            }
        }

        if (eliminado) {
            if (original.delete()) {
                temporal.renameTo(original);
                rgames = new RandomAccessFile("steam/games.stm", "rw");
            } else {
                throw new IOException("No se pudo eliminar el archivo original.");
            }
        } else {
            temporal.delete();
        }

        return eliminado;
    }

    private Game readGameAtCurrent(RandomAccessFile raf) throws IOException {
        int code = raf.readInt();
        String titulo = raf.readUTF();
        char so = raf.readChar();
        int edadMinima = raf.readInt();
        double precio = raf.readDouble();
        int contador = raf.readInt();
        String path = raf.readUTF();
        return new Game(code, titulo, so, edadMinima, precio, contador, path);
    }

    private void copiarRegistroJuego(RandomAccessFile origen, RandomAccessFile destino) throws IOException {
        int code = origen.readInt();
        String titulo = origen.readUTF();
        char so = origen.readChar();
        int edad = origen.readInt();
        double precio = origen.readDouble();
        int contador = origen.readInt();
        String imagen = origen.readUTF();

        destino.writeInt(code);
        destino.writeUTF(titulo);
        destino.writeChar(so);
        destino.writeInt(edad);
        destino.writeDouble(precio);
        destino.writeInt(contador);
        destino.writeUTF(imagen);
    }

    public ArrayList<Game> getAllGames() throws IOException {
        ArrayList<Game> juegos = new ArrayList<>();
        rgames.seek(0);
        
        while (rgames.getFilePointer() < rgames.length()) {
            Game g = readGameAtCurrent();
            juegos.add(g);
        }
        
        return juegos;
    }

    public void printGames() throws IOException {
        System.out.println("\n========== JUEGOS REGISTRADOS ==========");
        rgames.seek(0);

        if (rgames.length() == 0) {
            System.out.println("No hay juegos registrados.");
            return;
        }

        while (rgames.getFilePointer() < rgames.length()) {
            Game g = readGameAtCurrent();
            System.out.println("----------------------------------------");
            System.out.println("Code: " + g.getCode());
            System.out.println("Título: " + g.getTitulo());
            System.out.println("Sistema Operativo: " + g.getSO());
            System.out.println("Edad Mínima: " + g.getEdadMinima() + " años");
            System.out.println("Precio: $" + String.format("%.2f", g.getPrecio()));
            System.out.println("Descargas: " + g.getContadorDownloads());
            System.out.println("Imagen: " + g.getPath());
        }

        System.out.println("========================================\n");
    }
}