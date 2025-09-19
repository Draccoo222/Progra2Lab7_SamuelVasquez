/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Menu extends JFrame {

    private ListaCanciones listaCanciones;
    private GestorArchivos gestorArchivos;

    private JList<Cancion> jlistCanciones;
    private DefaultListModel<Cancion> listModel;
    private JButton btnAgregar, btnReproducir, btnEliminar;
    
    // Reproductor integrado
    private ReproductorIntegrado reproductorIntegrado;

    public Menu() {

        listaCanciones = new ListaCanciones();
        gestorArchivos = new GestorArchivos();

        try {
            gestorArchivos.cargarListaCanciones(listaCanciones);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo de canciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        initializeUI();
        setupListeners();
        cargarCancionesEnLista();
    }

    private void initializeUI() {
        setTitle("Mi Reproductor de Música - Estilo Spotify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700); // Aumentamos el tamaño para el reproductor
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Configurar colores estilo Spotify
        Color backgroundColor = new Color(18, 18, 18);
        Color sidebarColor = new Color(0, 0, 0);
        Color textColor = Color.WHITE;
        Color accentColor = new Color(29, 185, 84);

        getContentPane().setBackground(backgroundColor);

        // Panel lateral
        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(sidebarColor);
        panelLateral.setLayout(new BorderLayout());
        panelLateral.setPreferredSize(new Dimension(200, 0));
        
        JLabel lblTitulo = new JLabel("♪ Mi Música", JLabel.CENTER);
        lblTitulo.setForeground(textColor);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        panelLateral.add(lblTitulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBackground(sidebarColor);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        btnAgregar = crearBotonEstilizado("+ Agregar Canción", accentColor);
        btnReproducir = crearBotonEstilizado("▶ Reproducir", new Color(70, 70, 70));
        btnEliminar = crearBotonEstilizado("🗑 Eliminar", new Color(220, 53, 69));

        btnReproducir.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnReproducir);
        panelBotones.add(btnEliminar);

        panelLateral.add(panelBotones, BorderLayout.CENTER);

        // Configurar la lista con renderer personalizado
        listModel = new DefaultListModel<>();
        jlistCanciones = new JList<>(listModel);
        jlistCanciones.setBackground(backgroundColor);
        jlistCanciones.setForeground(textColor);
        jlistCanciones.setSelectionBackground(new Color(40, 40, 40));
        jlistCanciones.setSelectionForeground(accentColor);
        jlistCanciones.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Renderer personalizado para mostrar carátula, título, artista y duración
        jlistCanciones.setCellRenderer(new CancionListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(jlistCanciones);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(backgroundColor);
        
        JLabel lblHeader = new JLabel("Biblioteca de Música");
        lblHeader.setForeground(textColor);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelPrincipal.add(lblHeader, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Crear el reproductor integrado
        reproductorIntegrado = new ReproductorIntegrado();

        // Panel contenedor principal
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.add(panelPrincipal, BorderLayout.CENTER);
        contenedorPrincipal.add(reproductorIntegrado, BorderLayout.SOUTH);

        add(panelLateral, BorderLayout.WEST);
        add(contenedorPrincipal, BorderLayout.CENTER);
    }

    private JButton crearBotonEstilizado(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = boton.getBackground();
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(originalColor.brighter());
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(originalColor);
            }
        });
        
        return boton;
    }

    private void setupListeners() {
        jlistCanciones.addListSelectionListener(e -> {
            boolean seleccionValida = !jlistCanciones.isSelectionEmpty();
            btnReproducir.setEnabled(seleccionValida);
            btnEliminar.setEnabled(seleccionValida);
        });

        // Doble clic para reproducir directamente
        jlistCanciones.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Cancion cancionSeleccionada = jlistCanciones.getSelectedValue();
                    if (cancionSeleccionada != null) {
                        reproductorIntegrado.cargarCancion(cancionSeleccionada);
                    }
                }
            }
        });

        // Botón para Agregar Canción
        btnAgregar.addActionListener(e -> {
            AgregarCancionDialog dialogo = new AgregarCancionDialog(Menu.this, gestorArchivos);
            dialogo.setVisible(true);

            Cancion cancionNueva = dialogo.getCancionAgregada();

            if (cancionNueva != null) {
                listaCanciones.add(cancionNueva);
                listModel.addElement(cancionNueva);
                JOptionPane.showMessageDialog(this, "Canción agregada con éxito.");
            }
        });

        // Botón para Reproducir Canción (modificado para usar reproductor integrado)
        btnReproducir.addActionListener(e -> {
            Cancion cancionSeleccionada = jlistCanciones.getSelectedValue();

            if (cancionSeleccionada == null) {
                return;
            }

            // Cargar la canción en el reproductor integrado
            reproductorIntegrado.cargarCancion(cancionSeleccionada);
        });

        // Botón para Eliminar Canción
        btnEliminar.addActionListener(e -> {
            Cancion cancionSeleccionada = jlistCanciones.getSelectedValue();

            if (cancionSeleccionada == null) {
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que quieres eliminar la canción '" + cancionSeleccionada.getTitle() + "'?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    int codigoAEliminar = cancionSeleccionada.getCodigo();

                    // Si la canción que se va a eliminar está siendo reproducida, detenerla
                    if (reproductorIntegrado.getCancionActual() != null && 
                        reproductorIntegrado.getCancionActual().getCodigo() == codigoAEliminar) {
                        reproductorIntegrado.limpiar();
                    }

                    gestorArchivos.eliminarCancion(codigoAEliminar);
                    listaCanciones.remove(codigoAEliminar);
                    listModel.removeElement(cancionSeleccionada);

                    JOptionPane.showMessageDialog(this, "Canción eliminada correctamente.");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la canción.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void cargarCancionesEnLista() {
        Nodo actual = listaCanciones.getCabeza();
        while (actual != null) {
            listModel.addElement(actual.song);
            actual = actual.siguiente;
        }
    }

    // Renderer personalizado para la lista
    private class CancionListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            
            if (value instanceof Cancion) {
                Cancion cancion = (Cancion) value;
                
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(true);
                
                // Verificar si es la canción que se está reproduciendo actualmente
                boolean isCurrentlyPlaying = reproductorIntegrado.getCancionActual() != null && 
                                           reproductorIntegrado.getCancionActual().getCodigo() == cancion.getCodigo();
                
                if (isSelected) {
                    panel.setBackground(new Color(40, 40, 40));
                } else if (isCurrentlyPlaying) {
                    panel.setBackground(new Color(25, 25, 25)); // Color especial para canción actual
                } else {
                    panel.setBackground(new Color(18, 18, 18));
                }
                
                // Carátula
                JLabel lblCaratula = new JLabel(cancion.getCaratula());
                lblCaratula.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Panel de información
                JPanel panelInfo = new JPanel(new GridLayout(2, 1));
                panelInfo.setOpaque(false);
                
                JLabel lblTitulo = new JLabel(cancion.getTitle());
                if (isCurrentlyPlaying) {
                    lblTitulo.setForeground(new Color(29, 185, 84)); // Verde si se está reproduciendo
                } else if (isSelected) {
                    lblTitulo.setForeground(new Color(29, 185, 84));
                } else {
                    lblTitulo.setForeground(Color.WHITE);
                }
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
                
                JLabel lblArtista = new JLabel(cancion.getArtist() + " • " + cancion.getGenero());
                lblArtista.setForeground(new Color(179, 179, 179));
                lblArtista.setFont(new Font("Arial", Font.PLAIN, 12));
                
                panelInfo.add(lblTitulo);
                panelInfo.add(lblArtista);
                
                // Duración e indicador de reproducción
                JPanel panelDerecha = new JPanel(new BorderLayout());
                panelDerecha.setOpaque(false);
                
                if (isCurrentlyPlaying) {
                    JLabel lblPlaying = new JLabel("♪");
                    lblPlaying.setForeground(new Color(29, 185, 84));
                    lblPlaying.setFont(new Font("Arial", Font.BOLD, 16));
                    lblPlaying.setHorizontalAlignment(SwingConstants.CENTER);
                    panelDerecha.add(lblPlaying, BorderLayout.NORTH);
                }
                
                JLabel lblDuracion = new JLabel(cancion.getDuracionFormateada());
                lblDuracion.setForeground(new Color(179, 179, 179));
                lblDuracion.setFont(new Font("Arial", Font.PLAIN, 12));
                lblDuracion.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
                lblDuracion.setHorizontalAlignment(SwingConstants.RIGHT);
                panelDerecha.add(lblDuracion, BorderLayout.SOUTH);
                
                panel.add(lblCaratula, BorderLayout.WEST);
                panel.add(panelInfo, BorderLayout.CENTER);
                panel.add(panelDerecha, BorderLayout.EAST);
                
                panel.setPreferredSize(new Dimension(0, 60));
                
                return panel;
            }
            
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    // Método para actualizar la visualización de la lista cuando cambie la canción actual
    public void actualizarVisualizacionLista() {
        repaint();
    }

    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        // Configurar Look and Feel para mejor apariencia
      
        
        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}