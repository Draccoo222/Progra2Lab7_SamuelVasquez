package lab7_samuelvasquez;

import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

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
        this.caratula = cargarCaratula();

        // Calcular duración más precisa
        this.duracionSegundos = calcularDuracionMP3();
    }

    private ImageIcon cargarCaratula() {
        if (pathCaratula != null && !pathCaratula.isEmpty()
                && !pathCaratula.equals("No seleccionada...")
                && !pathCaratula.contains("No seleccionada")) {

            File archivoCaratula = new File(pathCaratula);
            if (archivoCaratula.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(pathCaratula);
                    // Redimensionar a 50x50
                    return new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                } catch (Exception e) {
                    return crearCaratulaDefault();
                }
            }
        }
        return crearCaratulaDefault();
    }

    private ImageIcon crearCaratulaDefault() {
        // Crear una imagen por defecto de 50x50
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(50, 50, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = img.createGraphics();
        g2d.setColor(new java.awt.Color(64, 64, 64));
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(java.awt.Color.WHITE);
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        g2d.drawString("♪", 18, 32);
        g2d.dispose();
        return new ImageIcon(img);
    }


    private boolean esMP3(byte[] header) {
        // Verificar ID3v2 tag
        if (header.length >= 3
                && header[0] == 'I' && header[1] == 'D' && header[2] == '3') {
            return true;
        }

        // Verificar MP3 frame header (11 bits de sync)
        if (header.length >= 2) {
            return (header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0;
        }

        return false;
    }
    
    private long calcularDuracionMP3() {
    try {
        File archivoMP3 = new File(this.path);
        if (!archivoMP3.exists()) {
            return 180; // Retorna 3 mins por defecto si no encuentra el archivo
        }

        // La librería lee el archivo y su cabecera de audio
        AudioFile audioFile = AudioFileIO.read(archivoMP3);
        AudioHeader audioHeader = audioFile.getAudioHeader();

        if (audioHeader != null) {
            // Obtenemos la duración exacta en segundos
            return audioHeader.getTrackLength();
        }
    } catch (Exception e) {
        // Si la librería falla, imprime el error y usa un valor por defecto
        System.err.println("jaudiotagger no pudo leer la duración de: " + this.path);
        return 180; // 3 mins por defecto
    }

    return 180; // 3 mins por defecto
}
    
    
    private long calcularDuracionPorTamano(long tamanioBytes) {

        double tamanioMB = tamanioBytes / (1024.0 * 1024.0);

        long duracionEstimada = (long) (tamanioBytes / 16000.0);

        duracionEstimada = (long) (duracionEstimada * 0.92);

        if (duracionEstimada < 10) {
            duracionEstimada = 60;
        }
        if (duracionEstimada > 3600) {
            duracionEstimada = 3600;
        }

        return duracionEstimada;
    }

    public String getDuracionFormateada() {
        if (duracionSegundos <= 0) {
            return "3:00";
        }

        long minutos = duracionSegundos / 60;
        long segundos = duracionSegundos % 60;

        if (minutos >= 60) {
            long horas = minutos / 60;
            minutos = minutos % 60;
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        }

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
        this.caratula = cargarCaratula();
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
        // Recalcular duración si cambia el path
        this.duracionSegundos = calcularDuracionMP3();
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
