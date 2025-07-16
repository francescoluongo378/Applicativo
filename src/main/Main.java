package main;

import gui.Gui;

import javax.swing.SwingUtilities;

/**
 * Classe principale per avviare l'applicazione Hackathon Manager.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Gui(); // Avvia la GUI principale
            } catch (Exception e) {
                e.printStackTrace(); // Stampa eventuali errori in console
            }
        });
    }
}
