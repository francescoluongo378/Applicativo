package gui;

import controller.Controller;
import javax.swing.SwingUtilities;

public class GuiLauncher {
    public static void main(String[] args) {
        // Avvia l'interfaccia grafica in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller(); // Assicurati che Controller abbia un costruttore senza argomenti
            new Gui(); // Lancia la GUI
        });
    }
}
