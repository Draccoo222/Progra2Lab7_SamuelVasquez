package lab7_samuelvasquez;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AgregarCancionDialog extends JDialog {

    // Campos de texto y botones
    private JTextField txtCodigo, txtTitulo, txtArtista, txtGenero;
    private JTextField txtRutaMp3, txtRutaCaratula;
    private JButton btnGuardar, btnSeleccionarMp3, btnSeleccionarCaratula;

    // Variable para retornar la canción creada
    private Cancion cancionAgregada = null;

    // Paleta de colores estilo Spotify
    private final Color BACKGROUND_COLOR = new Color(40, 40, 40);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color ACCENT_COLOR = new Color(29, 185, 84);
    private final Color FIELD_BACKGROUND = new Color(60, 60, 60);
    private final Color BORDER_COLOR = new Color(80, 80, 80);

    public AgregarCancionDialog(JFrame parent, GestorArchivos gestorArchivos) {
        super(parent, "Agregar Nueva Canción", true);

        // --- Configuración General del Diálogo ---
        setSize(500, 350);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Creación y Estilizado de Componentes ---
        // Labels
        JLabel lblCodigo = crearLabelEstilizado("Código Único:");
        JLabel lblTitulo = crearLabelEstilizado("Título:");
        JLabel lblArtista = crearLabelEstilizado("Artista:");
        JLabel lblGenero = crearLabelEstilizado("Género:");
        JLabel lblMp3 = crearLabelEstilizado("Archivo MP3:");
        JLabel lblCaratula = crearLabelEstilizado("Archivo Carátula:");

        // TextFields
        txtCodigo = crearTextFieldEstilizado();
        txtTitulo = crearTextFieldEstilizado();
        txtArtista = crearTextFieldEstilizado();
        txtGenero = crearTextFieldEstilizado();

        // Botones
        btnSeleccionarMp3 = crearBotonBusqueda("Buscar MP3");
        btnSeleccionarCaratula = crearBotonBusqueda("Buscar PNG");
        btnGuardar = new JButton("Guardar Canción");
        estilizarBotonPrincipal(btnGuardar);

        // --- Añadir Componentes con GridBagLayout ---
        // Fila 0: Código
        gbc.gridx = 0; gbc.gridy = 0; add(lblCodigo, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; add(txtCodigo, gbc);
        
        // Fila 1: Título
        gbc.gridx = 0; gbc.gridy = 1; add(lblTitulo, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(txtTitulo, gbc);

        // Fila 2: Artista
        gbc.gridx = 0; gbc.gridy = 2; add(lblArtista, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(txtArtista, gbc);

        // Fila 3: Género
        gbc.gridx = 0; gbc.gridy = 3; add(lblGenero, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(txtGenero, gbc);

        // Fila 4: MP3
        gbc.gridx = 0; gbc.gridy = 4; add(lblMp3, gbc);
        gbc.gridx = 1; gbc.gridy = 4; add(btnSeleccionarMp3, gbc);

        // Fila 5: Carátula
        gbc.gridx = 0; gbc.gridy = 5; add(lblCaratula, gbc);
        gbc.gridx = 1; gbc.gridy = 5; add(btnSeleccionarCaratula, gbc);

        // Fila 6: Botón Guardar
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST; // Alinea el botón a la derecha
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 10, 10, 10);
        add(btnGuardar, gbc);
        
        // --- Lógica de los Eventos ---
        setupListeners(gestorArchivos);
    }

    // --- Métodos de Estilizado (Helpers) ---
    private JLabel crearLabelEstilizado(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JTextField crearTextFieldEstilizado() {
        JTextField textField = new JTextField();
        textField.setBackground(FIELD_BACKGROUND);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(ACCENT_COLOR); // Color del cursor
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        // Padding interno y borde
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border line = BorderFactory.createLineBorder(BORDER_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(line, padding));
        return textField;
    }

    private JButton crearBotonBusqueda(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(90, 90, 90));
        boton.setForeground(TEXT_COLOR);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        return boton;
    }
    
    private void estilizarBotonPrincipal(JButton boton) {
        boton.setBackground(ACCENT_COLOR);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 35));
    }
    
    // --- Lógica de Listeners ---
    private void setupListeners(GestorArchivos gestorArchivos) {
        // Inicializar rutas como vacías
        txtRutaMp3 = new JTextField("");
        txtRutaCaratula = new JTextField("");

        btnSeleccionarMp3.addActionListener(e -> seleccionarArchivo(txtRutaMp3, "mp3"));
        btnSeleccionarCaratula.addActionListener(e -> seleccionarArchivo(txtRutaCaratula, "png", "jpg", "jpeg"));

        btnGuardar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().isEmpty() || txtRutaMp3.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El código y el archivo MP3 son obligatorios.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int codigo = Integer.parseInt(txtCodigo.getText());

                if (gestorArchivos.buscarCancion(codigo)) {
                    JOptionPane.showMessageDialog(this, "El código ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cancionAgregada = new Cancion(
                        codigo,
                        txtTitulo.getText(),
                        txtArtista.getText(),
                        txtGenero.getText(),
                        txtRutaMp3.getText(),
                        txtRutaCaratula.getText()
                );
                gestorArchivos.guardarCancion(cancionAgregada);
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El código debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar la canción.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void seleccionarArchivo(JTextField campoRuta, String... extensiones) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos (" + String.join(", ", extensiones) + ")", extensiones));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            campoRuta.setText(chooser.getSelectedFile().getAbsolutePath());
            // Opcional: podrías cambiar el texto del botón para mostrar el nombre del archivo
        }
    }

    public Cancion getCancionAgregada() {
        return cancionAgregada;
    }
}