package gui;

import controller.Controller;
import model.Team;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FinestraOrganizzatore extends JFrame {
    private final Controller ctrl;
    private final JFrame loginWindow;

    public FinestraOrganizzatore(Controller ctrl, Utente u, JFrame loginWindow) {
        super("Organizzatore: " + u.getNome());
        this.ctrl = ctrl;
        this.loginWindow = loginWindow;

        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(6, 1, 10, 10));
        JButton b1 = new JButton("Crea Hackathon");
        JButton b2 = new JButton("Invita Giudice");
        JButton b3 = new JButton("Invita Partecipante");
        JButton b4 = new JButton("Visualizza Team");
        JButton b5 = new JButton("Classifica");
        JButton btnIndietro = new JButton("Logout");

        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        p.add(b5);
        p.add(btnIndietro);

        add(p);

        btnIndietro.addActionListener(e -> {
            loginWindow.setVisible(true);
            dispose();
        });

        b1.addActionListener(e -> {
            String titolo = JOptionPane.showInputDialog(this, "Titolo hackathon:");
            if (titolo != null && !titolo.isBlank()) {
                boolean creato = ctrl.creaHackathon(titolo, "Sede X", 100, 10);
                JOptionPane.showMessageDialog(this, creato ? "Hackathon creato!" : "Errore nella creazione.");
            }
        });

        b2.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome giudice:");
            String email = JOptionPane.showInputDialog(this, "Email giudice:");
            if (nome != null && email != null && !nome.isBlank() && !email.isBlank()) {
                boolean ok = ctrl.aggiungiGiudice(nome, email);
                JOptionPane.showMessageDialog(this, ok ? "Giudice invitato!" : "Errore invito giudice.");
            }
        });

        b3.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome partecipante:");
            String email = JOptionPane.showInputDialog(this, "Email partecipante:");
            if (nome != null && email != null && !nome.isBlank() && !email.isBlank()) {
                boolean ok = ctrl.aggiungiPartecipante(nome, email);
                JOptionPane.showMessageDialog(this, ok ? "Partecipante invitato!" : "Errore invito partecipante.");
            }
        });

        b4.addActionListener(e -> {
            ctrl.caricaTeamsDaDB();
            List<Team> teams = ctrl.getListaTeam();
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun team trovato.");
            } else {
                StringBuilder sb = new StringBuilder("Lista Team:\n");
                for (Team t : teams) {
                    sb.append("ID: ").append(t.getId()).append(", Nome: ")
                            .append(t.getNome()).append(", Progresso: ")
                            .append(t.getProgresso()).append("%\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            }
        });

        b5.addActionListener(e -> {
            var classifica = ctrl.getClassifica(ctrl.getHackathon().getId());
            if (classifica == null || classifica.getTeams().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna classifica.");
                return;
            }
            StringBuilder sb = new StringBuilder("Classifica:\n");
            int pos = 1;
            for (Team t : classifica.getTeams()) {
                sb.append(pos++).append(". ").append(t.getNome())
                        .append(" - Punti: ").append(t.getPuntiTotali()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });
    }}