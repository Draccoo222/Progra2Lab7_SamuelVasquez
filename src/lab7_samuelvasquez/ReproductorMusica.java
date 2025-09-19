package lab7_samuelvasquez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;

public class ReproductorMusica extends JFrame {
    private Cancion cancion;
    private Clip clip;
    private JProgressBar barraProgreso;
    private JLabel lblTiempo;
    private JButton btnPlay, btnPause, btnStop;
    private Timer timer;
    private boolean isPaused = false;
    private long pausePosition = 0;

    public ReproductorMusica(Cancion cancion) {
        this.cancion = cancion;
        initComponents();
        initReproductor();
    }

    private void initComponents() {
        setTitle("Reproduciendo: " + cancion.getTitle());
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de información
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblCaratula = new JLabel(cancion.getCaratula());
        
        JPanel panelTexto = new JPanel(new GridLayout(3, 1));
        panelTexto.add(new JLabel("Título: " + cancion.getTitle()));
        panelTexto.add(new JLabel("Artista: " + cancion.getArtist()));
        panelTexto.add(new JLabel("Duración: " + cancion.getDuracionFormateada()));
        
        panelInfo.add(lblCaratula, BorderLayout.WEST);
        panelInfo.add(panelTexto, BorderLayout.CENTER);

        // Panel de controles
        JPanel panelControles = new JPanel(new FlowLayout());
        btnPlay = new JButton("▶");
        btnPause = new JButton("⏸");
        btnStop = new JButton("⏹");
        
        btnPlay.setPreferredSize(new Dimension(50, 30));
        btnPause.setPreferredSize(new Dimension(50, 30));
        btnStop.setPreferredSize(new Dimension(50, 30));
        
        btnPause.setEnabled(false);
        
        panelControles.add(btnPlay);
        panelControles.add(btnPause);
        panelControles.add(btnStop);

        // Barra de progreso y tiempo
        JPanel panelProgreso = new JPanel(new BorderLayout());
        barraProgreso = new JProgressBar(0, (int)cancion.getDuracionSegundos());
        barraProgreso.setStringPainted(true);
        barraProgreso.setString("0:00 / " + cancion.getDuracionFormateada());
        
        lblTiempo = new JLabel("0:00 / " + cancion.getDuracionFormateada());
        
        panelProgreso.add(barraProgreso, BorderLayout.CENTER);
        panelProgreso.add(lblTiempo, BorderLayout.EAST);

        add(panelInfo, BorderLayout.NORTH);
        add(panelProgreso, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        // Action Listeners
        btnPlay.addActionListener(e -> play());
        btnPause.addActionListener(e -> pause());
        btnStop.addActionListener(e -> stop());
        
        // Timer para actualizar la barra de progreso
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clip != null && clip.isRunning()) {
                    long position = clip.getMicrosecondPosition() / 1000000; // convertir a segundos
                    barraProgreso.setValue((int)position);
                    
                    long minutos = position / 60;
                    long segundos = position % 60;
                    String tiempoActual = String.format("%d:%02d", minutos, segundos);
                    String tiempoTexto = tiempoActual + " / " + cancion.getDuracionFormateada();
                    
                    barraProgreso.setString(tiempoTexto);
                    lblTiempo.setText(tiempoTexto);
                    
                    // Si terminó la canción
                    if (position >= cancion.getDuracionSegundos()) {
                        stop();
                    }
                }
            }
        });
    }

    private void initReproductor() {
        try {
            File archivoAudio = new File(cancion.getPath());
            if (!archivoAudio.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "El archivo de audio no existe: " + cancion.getPath(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoAudio);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar el archivo de audio: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void play() {
        if (clip != null) {
            if (isPaused) {
                // Continuar desde donde se pausó
                clip.setMicrosecondPosition(pausePosition);
                isPaused = false;
            } else {
                // Empezar desde el principio
                clip.setFramePosition(0);
            }
            
            clip.start();
            timer.start();
            
            btnPlay.setEnabled(false);
            btnPause.setEnabled(true);
        }
    }

    private void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
            isPaused = true;
            timer.stop();
            
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
        }
    }

    private void stop() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            isPaused = false;
            pausePosition = 0;
            timer.stop();
            
            barraProgreso.setValue(0);
            barraProgreso.setString("0:00 / " + cancion.getDuracionFormateada());
            lblTiempo.setText("0:00 / " + cancion.getDuracionFormateada());
            
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
        }
    }

    @Override
    public void dispose() {
        if (timer != null) {
            timer.stop();
        }
        if (clip != null) {
            clip.close();
        }
        super.dispose();
    }
}