/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

/**
 *
 * @author unwir
 */
public class ListaCanciones {

    private Nodo inicio;

    public ListaCanciones() {
        inicio = null;
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public void add(Cancion cancion) {
        Nodo nuevoNodo = new Nodo(cancion);

        if (isEmpty()) {
            inicio = nuevoNodo;
        } else {
            Nodo actual = inicio;
            // Buscar el último nodo
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            // Agregar el nuevo nodo al final
            actual.siguiente = nuevoNodo;
        }
    }

    public void remove(int codigo) {
        if (isEmpty()) {
            return;
        }

        // Si el nodo a eliminar es el primero
        if (inicio.song.getCodigo() == codigo) {
            inicio = inicio.siguiente;
            return;
        }
        
        // Buscar el nodo a eliminar
        Nodo actual = inicio;
        while (actual.siguiente != null) {
            if (actual.siguiente.song.getCodigo() == codigo) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public Nodo getCabeza() {
        return inicio;
    }
    
    public int size() {
        int count = 0;
        Nodo actual = inicio;
        while (actual != null) {
            count++;
            actual = actual.siguiente;
        }
        return count;
    }
    
    public Cancion get(int index) {
        if (index < 0 || isEmpty()) {
            return null;
        }
        
        Nodo actual = inicio;
        int currentIndex = 0;
        
        while (actual != null && currentIndex < index) {
            actual = actual.siguiente;
            currentIndex++;
        }
        
        return actual != null ? actual.song : null;
    }
}