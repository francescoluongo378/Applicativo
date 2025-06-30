package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;

public class FinestraPartecipante extends JFrame {
    private Controller controller;
    private Utente partecipante;
    private JFrame finestraLogin;

    public FinestraPartecipante(Controller controller, Utente partecipante, JFrame finestraLogin) {
        this.controller = controller;
        this.partecipante = partecipante;
        this.finestraLogin = finestraLogin;

        setTitle("Finestra Partecipante - Benvenuto " + partecipante.getNome());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inizializzaGUI();
    }

    private void inizializzaGUI() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton btnCreaTeam = new JButton("Crea Team");
        JButton btnAggiornaProgresso = new JButton("Aggiorna Progresso");
        JButton btnVisualizzaGiudici = new JButton("Visualizza Giudici");
        JButton btnLogout = new JButton("Logout");

        btnCreaTeam.addActionListener(e -> {
            String nomeTeam = JOptionPane.showInputDialog(this, "Inserisci nome del team:");
            controller.creaTeam(nomeTeam, partecipante);
            JOptionPane.showMessageDialog(this, "Team creato con successo!");
        });

        btnAggiornaProgresso.addActionListener(e -> {
            String nomeTeam = JOptionPane.showInputDialog(this, "Inserisci nome del team:");
            String progresso = JOptionPane.showInputDialog(this, "Inserisci nuovo stato del progresso:");
            controller.aggiornaProgressoTeam(nomeTeam, progresso);
            JOptionPane.showMessageDialog(this, "Progresso aggiornato!");
        });


        btnLogout.addActionListener(e -> {
            this.dispose();
            finestraLogin.setVisible(true);
        });

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnCreaTeam);
        panel.add(btnAggiornaProgresso);
        panel.add(btnVisualizzaGiudici);
        panel.add(btnLogout);

        add(panel);
    }
}
