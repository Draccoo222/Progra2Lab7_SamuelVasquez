/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

import javax.swing.ImageIcon;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author unwir
 */
public class Cancion {
    
    private String title;
    private String artist;
    private String genero;
    private String path;
    private String pathCaratula;
    private int codigo;
    private ImageIcon caratula;
    private long duracionSegundos;

    public Cancion(int codigo, String title, String artist, String genero, String path, String pathCaratula) {
        this.title = title;
        this.artist = artist;
        this.genero = genero;
        this.path = path;
        this.codigo = codigo;
        this.pathCaratula = pathCaratula;
        
        // Cargar carátula si existe
        if (pathCaratula != null && !pathCaratula.isEmpty() && !pathCaratula.equals("No seleccionada...")) {
            File archivoCaratula = new File(pathCaratula);
            if (archivoCaratula.exists()) {
                this.caratula = new ImageIcon(new ImageIcon(pathCaratula).getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
            } else {
                this.caratula = crearCaratulaDefault();
            }
        } else {
            this.caratula = crearCaratulaDefault();
        }
        
        // Calcular duración
        this.duracionSegundos = calcularDuracion();
    }
    
    private ImageIcon crearCaratulaDefault() {
        // Crear una imagen por defecto de 50x50
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(50, 50, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = img.createGraphics();
        g2d.setColor(java.awt.Color.GRAY);
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(java.awt.Color.WHITE);
        g2d.drawString("♪", 20, 30);
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    private long calcularDuracion() {
        try {
            File archivoAudio = new File(path);
            if (!archivoAudio.exists()) {
                return 0;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoAudio);
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / audioInputStream.getFormat().getFrameRate();
            audioInputStream.close();
            
            return (long) durationInSeconds;
        } catch (Exception e) {
            System.err.println("Error al calcular duración: " + e.getMessage());
            return 0;
        }
    }
    
    public String getDuracionFormateada() {
        if (duracionSegundos <= 0) {
            return "0:00";
        }
        
        long minutos = duracionSegundos / 60;
        long segundos = duracionSegundos % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public String getPathCaratula() {
        return pathCaratula;
    }

    public void setPathCaratula(String pathCaratula) {
        this.pathCaratula = pathCaratula;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public ImageIcon getCaratula() {
        return caratula;
    }

    public void setCaratula(ImageIcon caratula) {
        this.caratula = caratula;
    }
    
    public long getDuracionSegundos() {
        return duracionSegundos;
    }

    @Override
    public String toString() {
        return title + " - " + artist + " (" + getDuracionFormateada() + ")";
    }
}