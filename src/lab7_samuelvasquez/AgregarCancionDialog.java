/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author unwir
 */
public class AgregarCancionDialog extends JDialog {

    private JTextField txtCodigo, txtTitulo, txtArtista, txtGenero;
    private JTextField txtRutaMp3, txtRutaCaratula;
    private JButton btnGuardar;

    private Cancion cancionAgregada = null;

    public AgregarCancionDialog(JFrame parent, GestorArchivos gestorArchivos) {

        super(parent, "Agregar Nueva Canción", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 10, 10));

        txtCodigo = new JTextField();
        txtTitulo = new JTextField();
        txtArtista = new JTextField();
        txtGenero = new JTextField();
        txtRutaMp3 = new JTextField("No seleccionado...");
        txtRutaMp3.setEditable(false);
        txtRutaCaratula = new JTextField("No seleccionada...");
        txtRutaCaratula.setEditable(false);

        JButton btnSeleccionarMp3 = new JButton("Buscar MP3");
        JButton btnSeleccionarCaratula = new JButton("Buscar Carátula");
        btnGuardar = new JButton("Guardar Canción");

        add(new JLabel("Código Único:"));
        add(txtCodigo);
        add(new JLabel("Título:"));
        add(txtTitulo);
        add(new JLabel("Artista:"));
        add(txtArtista);
        add(new JLabel("Género:"));
        add(txtGenero);
        add(new JLabel("Archivo MP3:"));
        add(btnSeleccionarMp3);
        add(new JLabel("Archivo Carátula:"));
        add(btnSeleccionarCaratula);
        add(new JLabel());
        add(btnGuardar);

        btnSeleccionarMp3.addActionListener(e -> seleccionarArchivo(txtRutaMp3, "mp3"));
        btnSeleccionarCaratula.addActionListener(e -> seleccionarArchivo(txtRutaCaratula, "png", "jpg"));

        btnGuardar.addActionListener(e -> {
            try {

                if (txtCodigo.getText().isEmpty() || txtRutaMp3.getText().contains("No seleccionado")) {
                    JOptionPane.showMessageDialog(this, "El código y el archivo MP3 son obligatorios.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int codigo = Integer.parseInt(txtCodigo.getText());

                if (gestorArchivos.buscarCancion(codigo)) {
                    JOptionPane.showMessageDialog(this, "El código ingresado ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear la canción
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
                JOptionPane.showMessageDialog(this, "El código debe ser un número v enálido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar la canción.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void seleccionarArchivo(JTextField campoTexto, String... extensiones) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos", extensiones));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            campoTexto.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public Cancion getCancionAgregada() {
        return cancionAgregada;
    }
}
