/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import labbinario.Player;
import labbinario.Steam;

/**
 *
 * @author jerem
 */
public class FuncionesADMIN {
    
    public void addPlayer(String username, String pass, String nombre, Date nacimiento, String path, String tipoUsuario) throws IOException {
        rplayer.seek(rplayer.length());
        rplayer.writeInt(getCode(2));
        rplayer.writeUTF(username);
        rplayer.writeUTF(pass);
        rplayer.writeUTF(nombre);
        rplayer.writeLong(nacimiento.getTime());
        rplayer.writeInt(0);
        rplayer.writeUTF(path);
        rplayer.writeUTF(tipoUsuario);
        rplayer.writeBoolean(true);
    }

    public boolean modificarPlayer(int codePlayer, String newName, String newPass, String newPhoto, String newTypeUser) throws IOException {
        rplayer.seek(0);
        while(rplayer.getFilePointer() < rplayer.length()) {
            long inicio = rplayer.getFilePointer();
            Player p = readPlayerAtCurrent();
            if(p.getCode() == codePlayer) {
                rplayer.seek(inicio);
                rplayer.readInt();
                rplayer.readUTF();
                rplayer.writeUTF(newPass);
                rplayer.writeUTF(newName);
                rplayer.skipBytes(8 + 4);
                rplayer.writeUTF(newPhoto);
                rplayer.writeUTF(newTypeUser);
                rplayer.skipBytes(1);
                return true;
            }
        }
        return false;
    }

    public boolean deletePlayer(int codePlayer) throws IOException {
        File orig = new File("steam/player.stm");
        File tmp = new File("steam/player_temp.stm");
        boolean deleted = false;

        try (RandomAccessFile rafTmp = new RandomAccessFile(tmp, "rw")) {
            rplayer.seek(0);
            while(rplayer.getFilePointer() < rplayer.length()) {
                long start = rplayer.getFilePointer();
                Player p = readPlayerAtCurrent();
                if(p.getCode() != codePlayer) {
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

    public void addJuego(String titulo, char so, int edadMinima, double precio, String path) throws IOException {
        rgames.seek(rgames.length());
        rgames.writeInt(getCode(1));
        rgames.writeUTF(titulo);
        rgames.writeChar(so);
        rgames.writeInt(edadMinima);
        rgames.writeDouble(precio);
        rgames.writeInt(0);
        rgames.writeUTF(path);
    }

    public boolean modificarJuego(int codeGame, String newTitulo, char newSO, int newEdad, double newPrecio, String newPath) throws IOException {
        long pos = findGamePosition(codeGame);
        if(pos == -1) return false;
        rgames.seek(pos);
        rgames.readInt();
        rgames.writeUTF(newTitulo);
        rgames.writeChar(newSO);
        rgames.writeInt(newEdad);
        rgames.writeDouble(newPrecio);
        rgames.skipBytes(4);
        rgames.writeUTF(newPath);
        return true;
    }

    public boolean eliminarJuego(int codeGame) throws IOException {
        File orig = new File("steam/games.stm");
        File tmp = new File("steam/games_temp.stm");
        boolean deleted = false;
        try(RandomAccessFile rafTmp = new RandomAccessFile(tmp, "rw")) {
            rgames.seek(0);
            while(rgames.getFilePointer() < rgames.length()) {
                long start = rgames.getFilePointer();
                Game g = readGameAtCurrent();
                if(g.getCode() != codeGame) {
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

    public boolean cambiarPrecioJuego(int codeGame, double newPrice) throws IOException {
        long pos = findGamePosition(codeGame);
        if(pos == -1) return false;
        rgames.seek(pos);
        rgames.readInt();
        skipUTF(rgames);
        rgames.skipBytes(2);
        rgames.skipBytes(4 + 4); 
        rgames.writeDouble(newPrice);
        return true;
    }

    public void listaJuegos() throws IOException {
        rgames.seek(0);
        while(rgames.getFilePointer() < rgames.length()) {
            Game g = readGameAtCurrent();
            System.out.println(g.getCode() + " | " + g.getTitulo() + " | " + g.getSO() + " | " + g.getEdadMinima() + " | Lps." + g.getPrecio() + " | Descargas: " + g.getContadorDownloads());
        }
    }

    public void verReportes() {
        File folder = new File("steam/");
        File[] files = folder.listFiles();
        if(files != null){
            for(File f: files){
                if(f.isFile() && f.getName().endsWith(".txt")) System.out.println(f.getName());
            }
        }
    }

    public boolean reporteCliente(int codeClient, String nombreArchivo) throws IOException {
        Player p = findPlayerByCode(codeClient);
        if(p == null) return false;
        File f = new File("steam/" + nombreArchivo);
        try(FileWriter fw = new FileWriter(f)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fw.write("Cliente: " + p.getName() + "\n");
            fw.write("Usuario: " + p.getUserName() + "\n");
            fw.write("Nacimiento: " + sdf.format(new Date(p.getNacimiento())) + "\n");
            fw.write("Descargas: " + p.getContadorDownloads() + "\n");
        }
        return true;
    }

    public void verDescargasGeneradas() {
        File folder = new File("steam/downloads");
        File[] files = folder.listFiles();
        if(files != null){
            for(File f: files){
                if(f.isFile() && f.getName().startsWith("download_")) System.out.println(f.getName());
            }
        }
    }


    }
