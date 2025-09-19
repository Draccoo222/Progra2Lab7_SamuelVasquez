/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;

public class Menu extends JFrame {

    private ListaCanciones listaCanciones;
    private GestorArchivos gestorArchivos;

    private JList<Cancion> jlistCanciones;
    private DefaultListModel<Cancion> listModel;
    private JButton btnAgregar, btnReproducir, btnEliminar;

    public Menu() {

        listaCanciones = new ListaCanciones();
        gestorArchivos = new GestorArchivos();

        try {
            gestorArchivos.cargarListaCanciones(listaCanciones);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo de canciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Mi Reproductor de Música");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        jlistCanciones = new JList<>(listModel);

        Nodo actual = listaCanciones.getCabeza();
        while (actual != null) {
            listModel.addElement(actual.song);
            actual = actual.siguiente;
        }

        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnReproducir = new JButton("Reproducir");
        btnEliminar = new JButton("Eliminar");

        btnReproducir.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnReproducir);
        panelBotones.add(btnEliminar);

        add(new JScrollPane(jlistCanciones), BorderLayout.CENTER);

        add(panelBotones, BorderLayout.SOUTH);

        jlistCanciones.addListSelectionListener(e -> {
            boolean seleccionValida = !jlistCanciones.isSelectionEmpty();
            btnReproducir.setEnabled(seleccionValida);
            btnEliminar.setEnabled(seleccionValida);
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

        btnReproducir.addActionListener(e -> {
            Cancion cancionSeleccionada = jlistCanciones.getSelectedValue();

            // Si no hay nada seleccionado, no hagas nada.
            if (cancionSeleccionada == null) {
                return;
            }

            // Muestra una ventana para confirmar la acción
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que quieres eliminar la canción '" + cancionSeleccionada.getTitle() + "'?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Solo si el usuario hace clic en "Sí"
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    int codigoAEliminar = cancionSeleccionada.getCodigo();

                    // Llama a los métodos que programamos para borrar
                    gestorArchivos.eliminarCancion(codigoAEliminar); // Del archivo
                    listaCanciones.remove(codigoAEliminar);         // De la lista enlazada

                    // Finalmente, quita la canción de la lista visible en la pantalla
                    listModel.removeElement(cancionSeleccionada);

                    JOptionPane.showMessageDialog(this, "Canción eliminada correctamente.");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la canción.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // Imprime el error detallado en la consola
                }
            }
        });

        // Botón para Eliminar Canción
        btnEliminar.addActionListener(e -> {
            Cancion cancionSeleccionada = jlistCanciones.getSelectedValue();
            System.out.println("Eliminando: " + cancionSeleccionada.getTitle());
            // Aquí irá la lógica para eliminar del archivo, de la lista y del listModel.
        });
    }

    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}
