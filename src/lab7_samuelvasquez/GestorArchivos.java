/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

/**
 *
 * @author unwir
 */
public class GestorArchivos {

    private RandomAccessFile f;
    private String archivoPath = "data/canciones.fnf";

    public GestorArchivos() {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdir();
            }
            f = new RandomAccessFile(archivoPath, "rw");
        } catch (IOException e) {
            System.out.println("Error al inicializar GestorArchivos: " + e.getMessage());
        }
    }

    public void guardarCancion(Cancion cn) throws IOException {
        f.seek(f.length());
        f.writeInt(cn.getCodigo());
        f.writeUTF(cn.getTitle());
        f.writeUTF(cn.getArtist());
        f.writeUTF(cn.getGenero());
        f.writeUTF(cn.getPath());
        f.writeUTF(cn.getPathCaratula());
    }

    public boolean buscarCancion(int code) throws IOException {
        f.seek(0);
        while (f.getFilePointer() < f.length()) {
            int cod = f.readInt();
            if (cod == code) {
                return true;
            }
            // Saltar los strings
            f.readUTF(); // titulo
            f.readUTF(); // artista
            f.readUTF(); // genero
            f.readUTF(); // rutaMp3
            f.readUTF(); // rutaCaratula
        }
        return false;
    }

    public RandomAccessFile getFile() {
        return f;
    }

    public void cargarListaCanciones(ListaCanciones lista) throws IOException {
        f.seek(0);

        while (f.getFilePointer() < f.length()) {
            int code = f.readInt();
            String titulo = f.readUTF();
            String artista = f.readUTF();
            String genero = f.readUTF();
            String rutaMp3 = f.readUTF();
            String rutaCaratula = f.readUTF();

            Cancion cancion = new Cancion(code, titulo, artista, genero, rutaMp3, rutaCaratula);
            lista.add(cancion);
        }
    }

    public void eliminarCancion(int code) throws IOException {
        File tempFile = new File("data/canciones_temp.fnf");
        RandomAccessFile tempRAF = new RandomAccessFile(tempFile, "rw");

        f.seek(0);

        while (f.getFilePointer() < f.length()) {
            int codigoActual = f.readInt();
            String titulo = f.readUTF();
            String artista = f.readUTF();
            String genero = f.readUTF();
            String rutaMp3 = f.readUTF();
            String rutaCaratula = f.readUTF();

            // Solo copiar si NO es el código a eliminar
            if (codigoActual != code) {
                tempRAF.writeInt(codigoActual);
                tempRAF.writeUTF(titulo);
                tempRAF.writeUTF(artista);
                tempRAF.writeUTF(genero);
                tempRAF.writeUTF(rutaMp3);
                tempRAF.writeUTF(rutaCaratula);
            }
        }

        f.close();
        tempRAF.close();

        // Corregir el nombre del archivo original
        File originalFile = new File(archivoPath);
        originalFile.delete();
        tempFile.renameTo(originalFile);

        // Reabrir el archivo
        f = new RandomAccessFile(originalFile, "rw");
    }
}