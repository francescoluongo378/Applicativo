package main;

import gui.Gui;

import javax.swing.SwingUtilities;

/**
 * Classe principale per l'avvio
 * Questa classe contiene il metodo main che serve come entry point dell'applicazione.
 */
public class Main {
    /**
     * Argomenti da riga di comando passati all'applicazione
     */
    public static String[] args;
    
    /**
     * Metodo main - entry point dell'applicazione.
     * Avvia l'interfaccia grafica dell'applicazione Hackathon Manager.
     * 
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {
        Main.args = args;
        // Avvia l'applicazione GUI nel thread di Event Dispatch
        SwingUtilities.invokeLater(Gui::new);
    }
}