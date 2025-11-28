/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;
import labbinario.Player;
import labbinario.Steam;

/**
 *
 * @author jerem
 */
public class FuncionesADMIN {
    
    // ----------------------------
    // 1. Registrar players
    // ----------------------------
    public void addPlayer(String username, String pass, String nombre, Date nacimiento, String path, String tipoUsuario) throws IOException {
        rplayer.seek(rplayer.length());
        rplayer.writeInt(getCode(2)); // Código player
        rplayer.writeUTF(username);
        rplayer.writeUTF(pass);
        rplayer.writeUTF(nombre);
        rplayer.writeLong(nacimiento.getTime()); // fecha en milisegundos
        rplayer.writeInt(0); // contador descargas
        rplayer.writeUTF(path); // foto
        rplayer.writeUTF(tipoUsuario);
        rplayer.writeBoolean(true); // activo
    }

    // ----------------------------
    // 2. Modificar players
    // ----------------------------
    public boolean modificarPlayer(int codePlayer, String newName, String newPass, String newPhoto, String newTypeUser) throws IOException {
        long pos = findPlayerPosition(codePlayer);
        if(pos == -1) return false;

        rplayer.seek(pos);
        rplayer.readInt();        // code
        rplayer.readUTF();        // username
        rplayer.writeUTF(newPass);
        rplayer.writeUTF(newName);
        rplayer.skipBytes(8);     // nacimiento
        rplayer.skipBytes(4);     // contador descargas
        rplayer.writeUTF(newPhoto);
        rplayer.writeUTF(newTypeUser);
        rplayer.skipBytes(1);     // activo
        return true;
    }

    // ----------------------------
    // 3. Eliminar players
    // ----------------------------
    public boolean deletePlayer(int codePlayer) throws IOException {
        File orig = new File("steam/player.stm");
        File tmp = new File("steam/player_temp.stm");
        boolean deleted = false;

        try (RandomAccessFile rafTmp = new RandomAccessFile(tmp, "rw")) {
            rplayer.seek(0);
            while (rplayer.getFilePointer() < rplayer.length()) {
                long start = rplayer.getFilePointer();
                Player p = readPlayer();
                if(p.code() != codePlayer){
                    rplayer.seek(start);
                    byte[] data = new byte[getPlayerRecordSize(p)];
                    rplayer.read(data);
                    rafTmp.write(data);
                } else deleted = true;
            }
        }

        if(orig.delete()) tmp.renameTo(orig);
        rplayer = new RandomAccessFile("steam/player.stm","rw");
        return deleted;
    }

    // ----------------------------
    // 4. Registrar juegos
    // ----------------------------
    public void addJuego(String title, String genre, char os, int edadMinima, double precio, String path) throws IOException {
        rgames.seek(rgames.length());
        rgames.writeInt(getCode(1));
        rgames.writeUTF(title);
        rgames.writeUTF(genre);
        rgames.writeChar(os);
        rgames.writeInt(edadMinima);
        rgames.writeDouble(precio);
        rgames.writeInt(0); // contador descargas
        rgames.writeUTF(path);
    }

    // ----------------------------
    // 5. Modificar juegos
    // ----------------------------
    public boolean modificarJuego(int codeGame, String newTitle, String newGenre, char newOS, int newAge, String newPath) throws IOException {
        long pos = findGamePosition(codeGame);
        if(pos==-1) return false;

        rgames.seek(pos);
        rgames.readInt();  // code
        rgames.writeUTF(newTitle);
        rgames.writeUTF(newGenre);
        rgames.writeChar(newOS);
        rgames.writeInt(newAge);
        rgames.skipBytes(8); // precio
        rgames.skipBytes(4); // contador descargas
        rgames.writeUTF(newPath);
        return true;
    }

    // ----------------------------
    // 6. Eliminar juegos
    // ----------------------------
    public boolean eliminarJuego(int codeGame) throws IOException {
        File orig = new File("steam/games.stm");
        File tmp = new File("steam/games_temp.stm");
        boolean deleted = false;

        try (RandomAccessFile rafTmp = new RandomAccessFile(tmp, "rw")) {
            rgames.seek(0);
            while(rgames.getFilePointer() < rgames.length()){
                long start = rgames.getFilePointer();
                GameNode g = readGameNode();
                if(g.code != codeGame){
                    rgames.seek(start);
                    byte[] data = new byte[getGameRecordSize(g)];
                    rgames.read(data);
                    rafTmp.write(data);
                } else deleted = true;
            }
        }

        if(orig.delete()) tmp.renameTo(orig);
        rgames = new RandomAccessFile("steam/games.stm","rw");
        return deleted;
    }

    // ----------------------------
    // 7. Cambiar precios de juegos
    // ----------------------------
    public boolean cambiosPrecioJuego(int codeGame, double newPrice) throws IOException {
        long pos = findGamePosition(codeGame);
        if(pos==-1) return false;

        rgames.seek(pos);
        rgames.skipBytes(4); // code
        skipUTF(rgames);     // title
        skipUTF(rgames);     // genre
        rgames.skipBytes(2 + 4); // char os + int edadMinima
        rgames.writeDouble(newPrice);
        return true;
    }

    // ----------------------------
    // 8. Ver lista completa de juegos
    // ----------------------------
    public void listaJuegos() throws IOException {
        GameNode g = printGames();
        while(g!=null){
            System.out.println(g.code + " | " + g.titulo + " | " + g.genero + " | " + g.so + " | Lps."+ g.precio + " | Descargas: "+g.dls);
            g = g.next;
        }
    }

    // ----------------------------
    // 9. Ver reportes
    // ----------------------------
    public void verReportes() {
        File folder = new File("steam/");
        File[] files = folder.listFiles();
        if(files!=null){
            for(File f: files){
                if(f.isFile() && f.getName().endsWith(".txt")) System.out.println(f.getName());
            }
        }
    }

    // ----------------------------
    // 10. Generar reportes de clientes
    // ----------------------------
    public boolean reportForClient(int codeClient, String txtFile) throws IOException {
        Player client = findPlayerByCode(codeClient);
        if(client == null) return false;

        File file = new File("steam/" + txtFile);
        try(PrintWriter writer = new PrintWriter(file)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            writer.println("REPORTE CLIENTE: " + client.fullName());
            writer.println("Codigo: " + client.code());
            writer.println("Nacimiento: " + sdf.format(new Date(client.birthDate())));
            writer.println("Descargas: " + client.downloads());
        }
        System.out.println("REPORTE CREADO");
        return true;
    }

    // ----------------------------
    // 11. Ver descargas generadas
    // ----------------------------
    public void showGeneratedDownloads() {
        File folder = new File("steam/downloads");
        File[] files = folder.listFiles();
        if(files!=null){
            for(File f: files){
                if(f.isFile() && f.getName().startsWith("download_")) System.out.println(f.getName());
            }
        }
    }

    }
