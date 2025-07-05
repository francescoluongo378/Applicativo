package gui;

import controller.Controller;
import model.Classifica;
import model.Team;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.SpinnerNumberModel;

public class FinestraGiudice extends JFrame {

    public FinestraGiudice(Controller controller, Utente giudice, JFrame finestraLogin) {

        setTitle("Giudice: " + giudice.getNome());
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton b1 = new JButton("Visualizza Team");
        JButton b2 = new JButton("Assegna Voto a un Team");
        JButton b3 = new JButton("Visualizza Classifica");
        JButton b4 = new JButton("Logout");

        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        add(p);

        b1.addActionListener(_ -> {
            controller.caricaTeamsDaDB();
            List<Team> teams = controller.getHackathon().getTeams();
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun team presente.");
                return;
            }
            StringBuilder sb = new StringBuilder("Team:\n");
            for (Team t : teams) {
                sb.append("- ").append(t.getNome())
                        .append(" (Progresso: ").append(t.getProgresso()).append("%)\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        b2.addActionListener(_ -> {
            // Prima mostriamo la lista dei team disponibili
            controller.caricaTeamsDaDB();
            List<Team> teams = controller.getHackathon().getTeams();
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun team presente da valutare.");
                return;
            }

            // Creiamo un array di stringhe per il JComboBox
            String[] teamOptions = new String[teams.size()];
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get(i);
                teamOptions[i] = t.getNome() + " (ID: " + t.getId() + ")";
            }

            // Mostriamo un JComboBox per selezionare il team
            JComboBox<String> teamCombo = new JComboBox<>(teamOptions);
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Seleziona il team da valutare:"));
            panel.add(teamCombo);

            // Aggiungiamo uno spinner per il voto
            SpinnerNumberModel votoModel = new SpinnerNumberModel(7, 1, 10, 1);
            JSpinner votoSpinner = new JSpinner(votoModel);
            panel.add(new JLabel("Voto (1-10):"));
            panel.add(votoSpinner);

            int result = JOptionPane.showConfirmDialog(this, panel, "Valuta Team",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Estraiamo l'ID del team dalla stringa selezionata
                    String selectedTeam = (String) teamCombo.getSelectedItem();
                    assert selectedTeam != null;
                    int startIndex = selectedTeam.indexOf("ID: ") + 4;
                    int endIndex = selectedTeam.indexOf(")");
                    int teamId = Integer.parseInt(selectedTeam.substring(startIndex, endIndex));

                    // Verifichiamo che il team esista nella lista caricata
                    boolean teamEsiste = false;
                    for (Team t : teams) {
                        if (t.getId() == teamId) {
                            teamEsiste = true;
                            break;
                        }
                    }

                    if (!teamEsiste) {
                        JOptionPane.showMessageDialog(this,
                                "Il team selezionato (ID: " + teamId + ") non esiste più nel database. Ricarica la lista dei team.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Otteniamo il voto dallo spinner
                    int voto = (Integer) votoSpinner.getValue();

                    // Assegniamo il voto
                    boolean ok = controller.assegnaVoto(giudice.getId(), teamId, voto);

                    if (ok) {
                        JOptionPane.showMessageDialog(this,
                                "Voto assegnato correttamente al team.",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Errore nell'assegnazione del voto. Verifica che il team esista.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Si è verificato un errore: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        b3.addActionListener(_ -> {
            Classifica classifica = controller.getClassifica();
            if (classifica.getTeams().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna classifica disponibile.");
                return;
            }
            StringBuilder sb = new StringBuilder("Classifica (basata sulla media dei voti):\n");
            int pos = 1;
            for (Team t : classifica.getTeams()) {
                sb.append(pos++).append(". ").append(t.getNome())
                        .append(" - Media: ").append(String.format("%.2f", t.getPunteggioMedio()))
                        .append(" (").append(t.getVoti().size()).append(" voti)").append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        b4.addActionListener(_ -> {
            finestraLogin.setVisible(true);
            dispose();
        });
    }
}