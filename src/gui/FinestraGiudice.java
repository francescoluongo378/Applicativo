package gui;

import controller.Controller;
import model.Classifica;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class FinestraGiudice extends JFrame {
    private Controller controller;
    private Utente giudice;
    private JFrame finestraLogin;

    public FinestraGiudice(Controller controller, Utente giudice, JFrame finestraLogin) {
        this.controller = controller;
        this.giudice = giudice;
        this.finestraLogin = finestraLogin;

        setTitle("Finestra Giudice - Benvenuto " + giudice.getNome());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inizializzaGUI();
    }

    private void inizializzaGUI() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnValutaTeam = new JButton("Valuta un Team");
        JButton btnVisualizzaClassifica = new JButton("Visualizza Classifica");
        JButton btnLogout = new JButton("Logout");

        btnValutaTeam.addActionListener(e -> {
            String team = JOptionPane.showInputDialog(this, "Inserisci nome del team:");
            String votoStr = JOptionPane.showInputDialog(this, "Inserisci voto da 1 a 10:");
            try {
                int voto = Integer.parseInt(votoStr);
                controller.valutaTeam(giudice, team, voto);
                JOptionPane.showMessageDialog(this, "Team valutato con successo!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Voto non valido.");
            }
        });

        btnVisualizzaClassifica.addActionListener(e -> {
            Classifica classifica = controller.getClassifica();
            JOptionPane.showMessageDialog(this, classifica);
        });

        btnLogout.addActionListener(e -> {
            this.dispose();
            finestraLogin.setVisible(true);
        });

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnValutaTeam);
        panel.add(btnVisualizzaClassifica);
        panel.add(btnLogout);

        add(panel);
    }
}
