/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.File;
    import java.io.IOException;
    import java.io.FileWriter;
    import java.text.SimpleDateFormat;
    import java.util.Date;
/**
 *
 * @author jerem
 */
public class FuncionesNORMALES {

    public void verCatalogo() throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            int code = rgames.readInt();
            String titulo = rgames.readUTF();
            String genero = rgames.readUTF();
            char so = rgames.readChar();
            int edadMin = rgames.readInt();
            double precio = rgames.readDouble();
            int contador = rgames.readInt();
            String path = rgames.readUTF();

            System.out.println(code + " | " + titulo + " | " + genero + " | " + so
                    + " | Lps." + precio + " | Edad mínima: " + edadMin);
        }
    }

    public boolean downloadGame(int gameCode, int clientCode, char sisO) throws IOException {
        // Buscar juego
        rgames.seek(0);
        int gamePos = -1;
        int edadMin = 0;
        double precio = 0;
        String titulo = "";
        String path = "";
        char so = 0;

        while (rgames.getFilePointer() < rgames.length()) {
            long start = rgames.getFilePointer();
            int code = rgames.readInt();
            String t = rgames.readUTF();
            String g = rgames.readUTF();
            char s = rgames.readChar();
            int edad = rgames.readInt();
            double p = rgames.readDouble();
            int cont = rgames.readInt();
            String pa = rgames.readUTF();

            if (code == gameCode) {
                gamePos = (int) start;
                titulo = t;
                so = s;
                edadMin = edad;
                precio = p;
                path = pa;
                break;
            }
        }
        if (gamePos == -1) return false;
        if (so != sisO) return false;

        // Buscar player
        rplayer.seek(0);
        long playerPos = -1;
        long nacimiento = 0;
        int contadorDescargas = 0;
        String nombre = "";

        while (rplayer.getFilePointer() < rplayer.length()) {
            long start = rplayer.getFilePointer();
            int code = rplayer.readInt();
            rplayer.readUTF(); // username
            rplayer.readUTF(); // password
            String n = rplayer.readUTF();
            long birth = rplayer.readLong();
            int cont = rplayer.readInt();
            rplayer.readUTF(); // imagen
            rplayer.readUTF(); // tipoUsuario
            rplayer.skipBytes(1); // activo

            if (code == clientCode) {
                playerPos = start;
                nacimiento = birth;
                contadorDescargas = cont;
                nombre = n;
                break;
            }
        }
        if (playerPos == -1) return false;

        // Verificar edad
        Calendar nacimientoCal = Calendar.getInstance();
        nacimientoCal.setTimeInMillis(nacimiento);
        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - nacimientoCal.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimientoCal.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        if (edad < edadMin) return false;

        // Guardar descarga en archivo txt
        File f = new File("steam/downloads/download_" + clientCode + "_" + gameCode + ".txt");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ahora = new Date();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(df.format(ahora) + "\n");
            fw.write("Usuario: " + nombre + "\n");
            fw.write("Juego: " + titulo + "\n");
            fw.write("Precio: " + precio + "\n");
            fw.write("Archivo: " + path + "\n");
        }

        // Incrementar contador descargas juego
        rgames.seek(gamePos);
        rgames.readInt();
        rgames.readUTF();
        rgames.readUTF();
        rgames.readChar();
        rgames.readInt();
        rgames.readDouble();
        int contJ = rgames.readInt();
        rgames.writeInt(contJ + 1);
        rgames.readUTF(); // path

        // Incrementar contador descargas player
        rplayer.seek(playerPos);
        rplayer.readInt();
        rplayer.readUTF();
        rplayer.readUTF();
        rplayer.readUTF();
        rplayer.readLong();
        int contP = rplayer.readInt();
        rplayer.writeInt(contP + 1);
        rplayer.readUTF();
        rplayer.readUTF();
        rplayer.skipBytes(1);

        return true;
    }

    // ----------------------------
    // 3. Ver juegos descargados
    // ----------------------------
    public void verDescargas(int clientCode) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            long start = rplayer.getFilePointer();
            int code = rplayer.readInt();
            rplayer.skipBytes(2); // username UTF
            rplayer.skipBytes(2); // password UTF
            String nombre = rplayer.readUTF();
            rplayer.readLong();
            int contador = rplayer.readInt();
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.skipBytes(1);

            if (code == clientCode) {
                System.out.println("Usuario: " + nombre + " | Descargas totales: " + contador);
                break;
            }
        }
    }

    // ----------------------------
    // 4. Configurar perfil (imagen, contraseña, nombre)
    // ----------------------------
    public boolean modificarPerfil(int clientCode, String newName, String newPass, String newImg) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            long start = rplayer.getFilePointer();
            int code = rplayer.readInt();
            rplayer.readUTF(); // username
            rplayer.readUTF(); // password
            String oldName = rplayer.readUTF();
            rplayer.readLong(); // nacimiento
            rplayer.readInt(); // contador descargas
            rplayer.readUTF(); // imagen
            rplayer.readUTF(); // tipoUsuario
            rplayer.skipBytes(1);

            if (code == clientCode) {
                rplayer.seek(start);
                rplayer.readInt();
                rplayer.readUTF(); // username
                rplayer.writeUTF(newPass);
                rplayer.writeUTF(newName);
                rplayer.skipBytes(8 + 4); // nacimiento + descargas
                rplayer.writeUTF(newImg);
                rplayer.skipBytes(2 + 1); // tipoUsuario + activo
                return true;
            }
        }
        return false;
    }

    // ----------------------------
    // 5. Ver contador de descargas
    // ----------------------------
    public int verContadorDescargas(int clientCode) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            int code = rplayer.readInt();
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.readLong();
            int contador = rplayer.readInt();
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.skipBytes(1);

            if (code == clientCode) return contador;
        }
        return -1;
    }
}

}
