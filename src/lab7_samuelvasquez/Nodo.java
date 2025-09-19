/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_samuelvasquez;

/**
 *
 * @author unwir
 */
public class Nodo {

    public Cancion song;
    Nodo siguiente;

    public Nodo(Cancion song) {
        this.song = song;
        siguiente = null;
    }

}
