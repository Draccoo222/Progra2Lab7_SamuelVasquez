package lab7_samuelvasquez;

import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class ReproductorIntegrado extends JPanel {
    private Cancion cancionActual;
    private AdvancedPlayer player;
    private Thread reproductorThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    
    
    private JLabel lblCaratula;
    private JLabel lblTitulo;
    private JLabel lblArtista;
    private JButton btnPlayStop;
    private JProgressBar barraProgreso;
    private JLabel lblTiempo;
    
    
    private Timer timerUI;
    private int tiempoTranscurrido = 0;
    private int duracionTotal = 0;

    public ReproductorIntegrado() {
        initComponents();
        setupTimer();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(80, 80, 80)));
        setPreferredSize(new Dimension(0, 90));

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBackground(new Color(40, 40, 40));
        panelInfo.setPreferredSize(new Dimension(280, 0));

        lblCaratula = new JLabel();
        lblCaratula.setPreferredSize(new Dimension(70, 70));
        lblCaratula.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setBackground(new Color(40, 40, 40));
        panelTexto.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        lblTitulo = new JLabel("Selecciona una canción");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblArtista = new JLabel("");
        lblArtista.setForeground(new Color(179, 179, 179));
        lblArtista.setFont(new Font("Arial", Font.PLAIN, 12));

        panelTexto.add(lblTitulo);
        panelTexto.add(lblArtista);

        panelInfo.add(lblCaratula, BorderLayout.WEST);
        panelInfo.add(panelTexto, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(new Color(40, 40, 40));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

      
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelControles.setBackground(new Color(40, 40, 40));

        btnPlayStop = crearBotonControl("▶");
        btnPlayStop.setEnabled(false);
        panelControles.add(btnPlayStop);

        
        JPanel panelProgreso = new JPanel(new BorderLayout());
        panelProgreso.setBackground(new Color(40, 40, 40));
        panelProgreso.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setBackground(new Color(80, 80, 80));
        barraProgreso.setForeground(new Color(29, 185, 84));
        barraProgreso.setBorderPainted(false);
        barraProgreso.setPreferredSize(new Dimension(400, 4)); 

        lblTiempo = new JLabel("0:00 / 0:00");
        lblTiempo.setForeground(new Color(179, 179, 179));
        lblTiempo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTiempo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        panelProgreso.add(barraProgreso, BorderLayout.CENTER);
        panelProgreso.add(lblTiempo, BorderLayout.SOUTH);

        panelCentral.add(panelControles);
        panelCentral.add(panelProgreso);

        add(panelInfo, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);

        setupListeners();
    }

    private JButton crearBotonControl(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(29, 185, 84));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setPreferredSize(new Dimension(60, 60));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
     
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(new Color(35, 200, 95));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(29, 185, 84));
            }
        });
        
        return boton;
    }

    private void setupListeners() {
        btnPlayStop.addActionListener(e -> {
            if (isPlaying) {
                stop();
            } else {
                play();
            }
        });
    }

    private void setupTimer() {
        timerUI = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && !isPaused) {
                    tiempoTranscurrido++;
                    actualizarUI();
                    
                    if (tiempoTranscurrido >= duracionTotal) {
                        stop();
                    }
                }
            }
        });
    }

    public void cargarCancion(Cancion cancion) {
        if (cancion == null) return;
        
        stop();
        
        this.cancionActual = cancion;
        this.duracionTotal = (int) cancion.getDuracionSegundos();
        this.tiempoTranscurrido = 0;
       
        lblCaratula.setIcon(cancion.getCaratula());
        lblTitulo.setText(cancion.getTitle());
        lblArtista.setText(cancion.getArtist());
        
        btnPlayStop.setEnabled(true);
        btnPlayStop.setText("▶");
        
        actualizarUI();
    }

    private void play() {
        if (cancionActual == null) return;
        
        stop(); 
        
        try {
            FileInputStream fis = new FileInputStream(cancionActual.getPath());
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new AdvancedPlayer(bis);
            
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackStarted(PlaybackEvent evt) {
                    isPlaying = true;
                    SwingUtilities.invokeLater(() -> {
                        btnPlayStop.setText("⏹");
                        timerUI.start();
                    });
                }

                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    isPlaying = false;
                    SwingUtilities.invokeLater(() -> {
                        stop();
                    });
                }
            });
            
            reproductorThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, 
                            "Error al reproducir: " + e.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        stop();
                    });
                }
            });
            
            reproductorThread.start();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar el archivo: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stop() {
        if (player != null) {
            player.close();
            player = null;
        }
        
        if (reproductorThread != null && reproductorThread.isAlive()) {
            reproductorThread.interrupt();
        }
        
        isPlaying = false;
        isPaused = false;
        tiempoTranscurrido = 0;
        
        if (timerUI != null) {
            timerUI.stop();
        }
        
        btnPlayStop.setText("▶");
        
        actualizarUI();
    }

    private void actualizarUI() {
        if (duracionTotal > 0) {
            int progreso = (int) ((double) tiempoTranscurrido / duracionTotal * 100);
            barraProgreso.setValue(Math.min(progreso, 100));
        }
        
        String tiempoActual = formatearTiempo(tiempoTranscurrido);
        String tiempoTotal = formatearTiempo(duracionTotal);
        lblTiempo.setText(tiempoActual + " / " + tiempoTotal);
    }

    private String formatearTiempo(int segundos) {
        int minutos = segundos / 60;
        int segs = segundos % 60;
        return String.format("%d:%02d", minutos, segs);
    }

    public void limpiar() {
        stop();
        cancionActual = null;
        lblCaratula.setIcon(null);
        lblTitulo.setText("Selecciona una canción");
        lblArtista.setText("");
        lblTiempo.setText("0:00 / 0:00");
        barraProgreso.setValue(0);
        
        btnPlayStop.setEnabled(false);
        btnPlayStop.setText("▶");
    }

   
    public Cancion getCancionActual() {
        return cancionActual;
    }

    
    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }
}