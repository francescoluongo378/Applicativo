package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FinestraOrganizzatore extends JFrame {

    public FinestraOrganizzatore(Controller ctrl, Utente u, JFrame loginWindow) {
        super("Organizzatore: " + u.getNome());

        setSize(500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        JButton b1 = new JButton("Crea Hackathon");
        JButton b2 = new JButton("Invita Giudice");
        JButton b3 = new JButton("Invita Partecipante");
        JButton b4 = new JButton("Visualizza Team");
        JButton b5 = new JButton("Classifica");
        JButton b6 = new JButton("Informazioni Hackathon");
        JButton btnIndietro = new JButton("Logout");

        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        p.add(b5);
        p.add(b6);
        p.add(btnIndietro);

        add(p);

        btnIndietro.addActionListener(_ -> {
            loginWindow.setVisible(true);
            dispose();
        });

        // Implementazione dei listener per i pulsanti b1, b2, b3, b4
        b1.addActionListener(_ -> {
            String titolo = JOptionPane.showInputDialog(this, "Titolo dell'Hackathon:");
            String sede = JOptionPane.showInputDialog(this, "Sede dell'Hackathon:");
            String maxPartecipantiStr = JOptionPane.showInputDialog(this, "Numero massimo di partecipanti:");
            String maxTeamStr = JOptionPane.showInputDialog(this, "Numero massimo di team:");

            try {
                int maxPartecipanti = Integer.parseInt(maxPartecipantiStr);
                int maxTeam = Integer.parseInt(maxTeamStr);

                boolean ok = ctrl.creaHackathon(titolo, sede, maxPartecipanti, maxTeam);
                JOptionPane.showMessageDialog(this, ok ? "Hackathon creato con successo!" : "Errore nella creazione dell'Hackathon.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Inserisci valori numerici validi per i limiti.");
            }
        });

        b2.addActionListener(_ -> {
            // Chiedi nome e email per aggiungere direttamente il giudice
            String nome = JOptionPane.showInputDialog(this, "Nome del giudice:");
            if (nome != null && !nome.isBlank()) {
                String email = JOptionPane.showInputDialog(this, "Email del giudice:");
                if (email != null && !email.isBlank()) {
                    boolean ok = ctrl.aggiungiGiudice(nome, email);
                    JOptionPane.showMessageDialog(this, ok ?
                            "Giudice aggiunto con successo!" :
                            "Errore nell'aggiunta del giudice.");
                } else {
                    JOptionPane.showMessageDialog(this, "L'email è obbligatoria.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Il nome è obbligatorio.");
            }
        });

        b3.addActionListener(_ -> {
            // Chiedi nome e email per aggiungere direttamente il partecipante
            String nome = JOptionPane.showInputDialog(this, "Nome del partecipante:");
            if (nome != null && !nome.isBlank()) {
                String email = JOptionPane.showInputDialog(this, "Email del partecipante:");
                if (email != null && !email.isBlank()) {
                    boolean ok = ctrl.aggiungiPartecipante(nome, email);
                    JOptionPane.showMessageDialog(this, ok ?
                            "Partecipante aggiunto con successo!" :
                            "Errore nell'aggiunta del partecipante.");
                } else {
                    JOptionPane.showMessageDialog(this, "L'email è obbligatoria.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Il nome è obbligatorio.");
            }
        });

        b4.addActionListener(_ -> {
            ctrl.caricaTeamsDaDB();
            List<Team> teams = ctrl.getListaTeam();
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun team presente.");
                return;
            }

            StringBuilder sb = new StringBuilder("Teams:\n");
            for (Team t : teams) {
                sb.append("- ").append(t.getNome())
                        .append(" (Progresso: ").append(t.getProgresso()).append("%)\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        b5.addActionListener(_ -> {
            Classifica classifica = ctrl.getClassifica();
            if (classifica.getTeams().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna classifica.");
                return;
            }
            StringBuilder sb = new StringBuilder("Classifica (basata sulla media dei voti):\n");
            int pos = 1;
            for (Team t : classifica.getTeams()) {
                sb.append(pos++).append(". ")
                        .append(t.getNome()).append(" - Media: ")
                        .append(String.format("%.2f", t.getPunteggioMedio()))
                        .append(" (").append(t.getVoti().size()).append(" voti)").append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        b6.addActionListener(_ -> {
            // Ottieni l'hackathon corrente
            Hackathon hackathon = ctrl.getHackathon();
            if (hackathon == null) {
                JOptionPane.showMessageDialog(this, "Nessun hackathon disponibile.");
                return;
            }

            // Carica i dati aggiornati
            ctrl.caricaTeamsDaDB();
            ctrl.caricaGiudiciDaDB();

            try {
                // Usa un approccio diverso per caricare i partecipanti
                List<Partecipante> partecipanti = ctrl.getPartecipanteDAO().findAll();

                // Crea un messaggio con le informazioni dell'hackathon

                JScrollPane scrollPane = getJScrollPane(hackathon, partecipanti);

                JOptionPane.showMessageDialog(this, scrollPane, "Informazioni Hackathon", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore nel caricamento delle informazioni: " + ex.getMessage());
            }
        });
    }

    private static JScrollPane getJScrollPane(Hackathon hackathon, List<Partecipante> partecipanti) {
        String sb = "Informazioni Hackathon:\n\n" + "Titolo: " + hackathon.getTitolo() + "\n" +
                "Sede: " + hackathon.getSede() + "\n" +
                "Max Partecipanti: " + hackathon.getMaxPartecipanti() + "\n" +
                "Max Team: " + hackathon.getMaxTeam() + "\n\n" +
                "Numero di Team: " + hackathon.getTeams().size() + "\n" +
                "Numero di Giudici: " + hackathon.getGiudici().size() + "\n" +
                "Numero di Partecipanti: " + partecipanti.size() + "\n";

        // Mostra le informazioni
        JTextArea textArea = new JTextArea(sb);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        return scrollPane;
    }
}