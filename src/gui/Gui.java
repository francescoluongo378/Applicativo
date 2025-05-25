package gui;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import Hackathon.*;
import controller.*;

public class Gui extends JFrame {
    private Hackathon hackathon;
    private Controller controller;

    public Gui(Hackathon hackathon) {
        this.hackathon = hackathon;
        this.controller = new Controller(hackathon);
        setTitle("Dettagli Hackathon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        panel.add(new JLabel("Titolo:"));
        panel.add(new JLabel(hackathon.getTitolo()));

        panel.add(new JLabel("Sede:"));
        panel.add(new JLabel(hackathon.getSede()));

        panel.add(new JLabel("Data inizio:"));
        panel.add(new JLabel(hackathon.getDataInizio().format(formatter)));

        panel.add(new JLabel("Data fine:"));
        panel.add(new JLabel(hackathon.getDataFine().format(formatter)));

        panel.add(new JLabel("Iscrizioni dal:"));
        panel.add(new JLabel(hackathon.getInizioIscrizioni().format(formatter)));

        panel.add(new JLabel("Iscrizioni fino al:"));
        panel.add(new JLabel(hackathon.getFineIscrizioni().format(formatter)));

        panel.add(new JLabel("Max partecipanti:"));
        panel.add(new JLabel(String.valueOf(hackathon.getMaxPartecipanti())));

        panel.add(new JLabel("Max team:"));
        panel.add(new JLabel(String.valueOf(hackathon.getMaxTeam())));

        // Bottone per mostrare i team
        JButton btnVisualizzaTeam = new JButton("Visualizza Team");
        btnVisualizzaTeam.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Team t : hackathon.getTeams()) {
                sb.append(t.getNome()).append(" - Progresso: ")
                        .append(t.getProgresso()).append("% - Punteggio: ")
                        .append(t.getPunteggio()).append("\n");
            }
            if (sb.length() == 0) sb.append("Nessun team registrato.");
            JOptionPane.showMessageDialog(this, sb.toString(), "Team", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnCreaTeam = new JButton("Crea Team");
        btnCreaTeam.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome del team:");
            if (controller.creaTeam(nome)) {
                JOptionPane.showMessageDialog(this, "Team creato!");
            } else {
                JOptionPane.showMessageDialog(this, "Errore nella creazione del team.");
            }
        });

        JButton btnAggiungiGiudice = new JButton("Aggiungi Giudice");
        btnAggiungiGiudice.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome giudice:");
            String email = JOptionPane.showInputDialog(this, "Email giudice:");
            if (controller.aggiungiGiudice(nome, email)) {
                JOptionPane.showMessageDialog(this, "Giudice aggiunto!");
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'aggiunta del giudice.");
            }
        });

        JButton btnValutaTeam = new JButton("Valuta Team");
        btnValutaTeam.addActionListener(e -> {
            String nomeGiudice = JOptionPane.showInputDialog(this, "Nome giudice:");
            String nomeTeam = JOptionPane.showInputDialog(this, "Nome team:");
            String votoStr = JOptionPane.showInputDialog(this, "Voto (0-10):");
            try {
                int voto = Integer.parseInt(votoStr);
                if (controller.valutaTeam(nomeGiudice, nomeTeam, voto)) {
                    JOptionPane.showMessageDialog(this, "Team valutato!");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: documento non completo o voto non valido.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Voto non valido.");
            }
        });

        JButton btnAggiornaProgresso = new JButton("Aggiorna Progresso");
        btnAggiornaProgresso.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome team:");
            String pStr = JOptionPane.showInputDialog(this, "Progresso (0-100):");
            try {
                int p = Integer.parseInt(pStr);
                if (controller.aggiornaProgressoTeam(nome, p)) {
                    JOptionPane.showMessageDialog(this, "Progresso aggiornato!");
                } else {
                    JOptionPane.showMessageDialog(this, "Team non trovato.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valore non valido.");
            }
        });

        JButton btnClassifica = new JButton("Classifica");
        btnClassifica.addActionListener(e -> {
            List<Team> classifica = hackathon.getClassifica().getTeams();
            classifica.sort((a, b) -> b.getPunteggio() - a.getPunteggio());
            StringBuilder sb = new StringBuilder("=== Classifica ===\n");
            for (Team t : classifica) {
                sb.append(t.getNome()).append(" - ").append(t.getPunteggio()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Classifica", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(btnVisualizzaTeam);
        panel.add(btnCreaTeam);
        panel.add(btnAggiungiGiudice);
        panel.add(btnAggiornaProgresso);
        panel.add(btnValutaTeam);
        panel.add(btnClassifica);

        getContentPane().add(panel);
    }
}
