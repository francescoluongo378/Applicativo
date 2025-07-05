package gui;

import controller.Controller;
import model.*;
import model.Utente;
import postgresdao.PostgresPartecipanteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class FinestraPartecipante extends JFrame {

    public FinestraPartecipante(Controller controller, Utente partecipante, JFrame finestraLogin) {

        setTitle("Partecipante: " + partecipante.getNome());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(6, 1, 10, 10));
        JButton b1 = new JButton("Crea Team");
        JButton b2 = new JButton("Invia Documento");
        JButton b4 = new JButton("Visualizza Documenti");
        JButton b5 = new JButton("Visualizza Membri dei Team");
        JButton b6 = new JButton("Unisciti a un Team");
        JButton b7 = new JButton("Logout");

        p.add(b1);
        p.add(b2);
        p.add(b4);
        p.add(b5);
        p.add(b6);
        p.add(b7);
        add(p);

        b1.addActionListener(_ -> {
            String nomeTeam = JOptionPane.showInputDialog(this, "Nome del nuovo Team:");
            if (nomeTeam != null && !nomeTeam.isBlank()) {
                boolean ok = controller.creaTeam(nomeTeam.trim(), partecipante.getId());
                JOptionPane.showMessageDialog(this, ok ? "Team creato!" : "Errore nella creazione.");
            }
        });

        b2.addActionListener(_ -> {
            // Verifica prima se il partecipante è associato a un team
            int idTeam = controller.getTeamIdByPartecipante(partecipante.getId());
            if (idTeam <= 0) {
                JOptionPane.showMessageDialog(this, "Devi prima essere associato a un team per inviare documenti.",
                        "Nessun team", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Creiamo un pannello per l'inserimento dei dettagli del documento
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nome del documento:"));
            JTextField nomeField = new JTextField();
            panel.add(nomeField);

            // Aggiungiamo una descrizione opzionale
            panel.add(new JLabel("Tipo di documento:"));
            String[] tipiDocumento = {"Presentazione", "Codice sorgente", "Documentazione", "Altro"};
            JComboBox<String> tipoCombo = new JComboBox<>(tipiDocumento);
            panel.add(tipoCombo);

            int result = JOptionPane.showConfirmDialog(this, panel, "Carica Documento",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String nomeFile = nomeField.getText().trim();
                String tipoDoc = (String) tipoCombo.getSelectedItem();

                if (nomeFile.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Il nome del documento è obbligatorio.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Aggiungiamo il tipo al nome del documento
                String nomeCompleto = tipoDoc + " - " + nomeFile;

                boolean ok = controller.inviaDocumento(partecipante.getId(), nomeCompleto);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Documento '" + nomeCompleto + "' inviato con successo!",
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nell'invio del documento. Riprova.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        b4.addActionListener(_ -> {
            int idTeam = controller.getTeamIdByPartecipante(partecipante.getId());
            if (idTeam <= 0) {
                JOptionPane.showMessageDialog(this, "Non sei associato a nessun team.",
                        "Nessun team", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Documento> documenti = controller.getDocumentiDiTeam(idTeam);
            if (documenti.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun documento presente per il tuo team.");
                return;
            }

            StringBuilder sb = new StringBuilder("Documenti del team:\n");
            for (Documento doc : documenti) {
                sb.append("- ").append(doc.getTitolo()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // Nuovo pulsante: Visualizza Membri dei Team
        b5.addActionListener(_ -> {
            try {
                // Carichiamo i team dal database
                controller.caricaTeamsDaDB();

                List<Team> teams = controller.getListaTeam();
                if (teams.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Non ci sono team registrati.");
                    return;
                }

                // Creiamo una mappa di partecipanti per team
                StringBuilder sb = new StringBuilder("Membri dei Team:\n\n");

                for (Team team : teams) {
                    sb.append("Team: ").append(team.getNome()).append(" (ID: ").append(team.getId()).append(")\n");

                    // Otteniamo i partecipanti di questo team direttamente dal database
                    List<Partecipante> membriTeam = new ArrayList<>();

                    try (Connection conn = database.ConnessioneDatabase.getInstance().getConnection()) {
                        String sql = "SELECT p.id, u.nome, u.email FROM partecipante p JOIN utente u ON p.id = u.id WHERE p.team_id = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setInt(1, team.getId());
                            try (ResultSet rs = stmt.executeQuery()) {
                                while (rs.next()) {
                                    int pid = rs.getInt("id");
                                    String nome = rs.getString("nome");
                                    String email = rs.getString("email");

                                    Partecipante membro = new Partecipante(pid, nome, email);
                                    membro.setTeamId(team.getId());
                                    membriTeam.add(membro);
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (membriTeam.isEmpty()) {
                        sb.append("  Nessun membro\n");
                    } else {
                        for (Partecipante membro : membriTeam) {
                            sb.append("  - ").append(membro.getNome()).append(" (").append(membro.getEmail()).append(")\n");
                        }
                    }
                    sb.append("  Membri: ").append(membriTeam.size()).append("/3\n\n");
                }

                // Mostriamo i risultati in una finestra di dialogo con scrolling
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(this, scrollPane, "Membri dei Team", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore nel caricamento dei membri dei team: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nuovo pulsante: Unisciti a un Team
        b6.addActionListener(_ -> {
            // Verifichiamo se il partecipante è già in un team
            int idTeamAttuale = controller.getTeamIdByPartecipante(partecipante.getId());
            if (idTeamAttuale > 0) {
                int conferma = JOptionPane.showConfirmDialog(this,
                        "Sei già membro di un team. Vuoi lasciare il team attuale e unirti a un altro?",
                        "Conferma", JOptionPane.YES_NO_OPTION);
                if (conferma != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Carichiamo i team dal database
            controller.caricaTeamsDaDB();
            List<Team> teams = controller.getListaTeam();

            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Non ci sono team disponibili a cui unirti.");
                return;
            }

            // Filtriamo i team che hanno meno di 3 membri
            List<Team> teamsDisponibili = new ArrayList<>();
            for (Team team : teams) {
                int numMembri = ((PostgresPartecipanteDAO)controller.getPartecipanteDAO()).contaPartecipantiInTeam(team.getId());
                if (numMembri < 3) {
                    teamsDisponibili.add(team);
                }
            }

            if (teamsDisponibili.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tutti i team hanno già raggiunto il numero massimo di 3 membri.");
                return;
            }

            // Creiamo un array di stringhe per il combobox
            String[] opzioniTeam = new String[teamsDisponibili.size()];
            for (int i = 0; i < teamsDisponibili.size(); i++) {
                Team t = teamsDisponibili.get(i);
                int numMembri = ((PostgresPartecipanteDAO)controller.getPartecipanteDAO()).contaPartecipantiInTeam(t.getId());
                opzioniTeam[i] = t.getNome() + " (ID: " + t.getId() + ", Membri: " + numMembri + "/3)";
            }

            // Mostriamo la finestra di dialogo per la selezione del team
            String teamSelezionato = (String) JOptionPane.showInputDialog(this,
                    "Seleziona il team a cui vuoi unirti:",
                    "Unisciti a un Team",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opzioniTeam,
                    opzioniTeam[0]);

            if (teamSelezionato != null) {
                // Estraiamo l'ID del team dalla stringa selezionata
                int idxStart = teamSelezionato.indexOf("ID: ") + 4;
                int idxEnd = teamSelezionato.indexOf(",", idxStart);
                int idTeamSelezionato = Integer.parseInt(teamSelezionato.substring(idxStart, idxEnd));


                int currentTeamId = controller.getTeamIdByPartecipante(partecipante.getId());

                // Associamo il partecipante al team selezionato
                controller.getPartecipanteDAO().salvaPartecipanteNelTeam(partecipante.getId(), idTeamSelezionato);

                if (currentTeamId > 0 && currentTeamId != idTeamSelezionato) {
                    JOptionPane.showMessageDialog(this, "Hai cambiato team con successo!");
                } else {
                    JOptionPane.showMessageDialog(this, "Ti sei unito con successo al team selezionato!");
                }
            }
        });

        b7.addActionListener(_ -> {
            finestraLogin.setVisible(true);
            dispose();
        });
    }
}
