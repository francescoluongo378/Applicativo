package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import javax.swing.SpinnerNumberModel;

public class Gui extends JFrame {

    private final Controller controller;
    private Utente utenteLoggato;

    // Componenti base
    private JPanel panelLogin;
    private JTextField txtNome, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRuolo;

    private JPanel panelOperativo;

    public Gui() {

        controller = new Controller();

        setTitle("Hackathon Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        creaPanelLogin();
        creaPanelOperativo();

        add(panelLogin, BorderLayout.CENTER);
        panelOperativo.setVisible(false);

        setVisible(true);
    }

    private void creaPanelLogin() {
        panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitolo = new JLabel("Login / Registrazione", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panelLogin.add(lblTitolo, gbc);

        gbc.gridwidth=1;
        gbc.gridy++;

        // Nome
        panelLogin.add(new JLabel("Nome:"), gbc);
        gbc.gridx=1;
        txtNome = new JTextField();
        panelLogin.add(txtNome, gbc);

        // Email
        gbc.gridy++; gbc.gridx=0;
        panelLogin.add(new JLabel("Email:"), gbc);
        gbc.gridx=1;
        txtEmail = new JTextField();
        panelLogin.add(txtEmail, gbc);

        // Password
        gbc.gridy++; gbc.gridx=0;
        panelLogin.add(new JLabel("Password:"), gbc);
        gbc.gridx=1;
        txtPassword = new JPasswordField();
        panelLogin.add(txtPassword, gbc);

        // Ruolo
        gbc.gridy++; gbc.gridx=0;
        panelLogin.add(new JLabel("Ruolo:"), gbc);
        gbc.gridx=1;
        comboRuolo = new JComboBox<>(new String[]{"organizzatore","giudice","partecipante"});
        panelLogin.add(comboRuolo, gbc);

        // Bottone
        gbc.gridy++; gbc.gridx=0; gbc.gridwidth=2;
        JButton btnLogin = new JButton("Login / Registrati");
        panelLogin.add(btnLogin, gbc);

        btnLogin.addActionListener(_ -> {
            // Leggi dati form
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String pwd = new String(txtPassword.getPassword());
            String ruolo = (String) comboRuolo.getSelectedItem();

            // Verifica campi
            if (nome.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(Gui.this, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prova login
            Optional<Utente> risultato = controller.login(email, pwd);

            // Se login ok
            if (risultato.isPresent()) {
                utenteLoggato = risultato.get();

                // Verifica ruolo
                if (!utenteLoggato.getRuolo().equalsIgnoreCase(ruolo)) {
                    JOptionPane.showMessageDialog(Gui.this, "Ruolo errato!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            // Se login fallisce, prova registrazione
            else {
                risultato = controller.registraUtente(nome, email, pwd, ruolo);

                if (risultato.isPresent()) {
                    utenteLoggato = risultato.get();
                    JOptionPane.showMessageDialog(Gui.this, "Registrazione completata!");
                } else {
                    JOptionPane.showMessageDialog(Gui.this, "Errore nella registrazione!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Apri finestra ruolo
            Gui.this.setVisible(false);
            apriFinestraRuolo();
        });
    }

    private void creaPanelOperativo() {
        panelOperativo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JButton btnCreaTeam = new JButton("Crea Team");
        JButton btnListaGiudici = new JButton("Lista Giudici");
        JButton btnListaPartecipanti = new JButton("Lista Partecipanti");
        JButton btnValutaTeam = new JButton("Valuta Team");
        JButton btnLogout = new JButton("Logout");

        gbc.gridx=0; gbc.gridy=0; panelOperativo.add(btnCreaTeam, gbc);
        gbc.gridx=1; panelOperativo.add(btnListaGiudici, gbc);
        gbc.gridx=0; gbc.gridy=1; panelOperativo.add(btnListaPartecipanti, gbc);
        gbc.gridx=1; panelOperativo.add(btnValutaTeam, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panelOperativo.add(btnLogout, gbc);

        // listener
        btnCreaTeam.addActionListener(_ -> {
            String nomeTeam = JOptionPane.showInputDialog(this, "Nome team:");
            if (nomeTeam != null && !nomeTeam.isBlank()) {
                int idPartecipante = controller.getUtenteLoggato().getId();  // Prendi l'id del partecipante loggato
                boolean ok = controller.creaTeam(nomeTeam.trim(), idPartecipante);
                JOptionPane.showMessageDialog(this, ok ? "Team creato" : "Errore nella creazione del team");
            }
        });

        btnListaGiudici.addActionListener(_ -> {
            controller.caricaGiudiciDaDB();
            List<Giudice> listaGiudici = controller.getHackathon().getGiudici();

            if (listaGiudici.isEmpty()) {
                JOptionPane.showMessageDialog(Gui.this, "Nessun giudice");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Giudice g : listaGiudici) {
                    sb.append(g.getNome()).append("\n");
                }
                JOptionPane.showMessageDialog(Gui.this, sb.toString());
            }
        });

        btnListaPartecipanti.addActionListener(_ -> {
            controller.caricaTeamsDaDB();
            List<Partecipante> listaPartecipanti = controller.getHackathon().getPartecipanti();

            if (listaPartecipanti.isEmpty()) {
                JOptionPane.showMessageDialog(Gui.this, "Nessun partecipante");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Partecipante p : listaPartecipanti) {
                    sb.append(p.getNome()).append("\n");
                }
                JOptionPane.showMessageDialog(Gui.this, sb.toString());
            }
        });
        btnValutaTeam.addActionListener(_ -> {
            // Carica i team
            controller.caricaTeamsDaDB();
            List<Team> teams = controller.getListaTeam();

            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(Gui.this, "Nessun team disponibile da valutare.");
                return;
            }

            // Prepara array per combobox
            String[] teamOptions = new String[teams.size()];
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get(i);
                teamOptions[i] = t.getNome() + " (ID: " + t.getId() + ")";
            }

            // Crea pannello
            JComboBox<String> teamCombo = new JComboBox<>(teamOptions);
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Seleziona il team da valutare:"));
            panel.add(teamCombo);

            // Spinner per voto
            SpinnerNumberModel votoModel = new SpinnerNumberModel(7, 1, 10, 1);
            JSpinner votoSpinner = new JSpinner(votoModel);
            panel.add(new JLabel("Voto (1-10):"));
            panel.add(votoSpinner);

            // Mostra dialogo
            int result = JOptionPane.showConfirmDialog(Gui.this, panel, "Valuta Team",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Estrai ID team
                String selectedTeam = (String) teamCombo.getSelectedItem();
                if (selectedTeam == null) {
                    return;
                }

                int startIndex = selectedTeam.indexOf("ID: ") + 4;
                int endIndex = selectedTeam.indexOf(")");

                int teamId;
                try {
                    teamId = Integer.parseInt(selectedTeam.substring(startIndex, endIndex));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(Gui.this, "Errore nel formato ID team", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verifica team
                boolean teamEsiste = false;
                for (Team t : teams) {
                    if (t.getId() == teamId) {
                        teamEsiste = true;
                        break;
                    }
                }

                if (!teamEsiste) {
                    JOptionPane.showMessageDialog(Gui.this, "Team non trovato nel database", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ottieni voto
                int voto = (Integer) votoSpinner.getValue();

                // Assegna voto
                int idGiudice = controller.getUtenteLoggato().getId();
                boolean ok = controller.assegnaVoto(idGiudice, teamId, voto);

                if (ok) {
                    JOptionPane.showMessageDialog(Gui.this, "Voto assegnato con successo");
                } else {
                    JOptionPane.showMessageDialog(Gui.this, "Errore nell'assegnazione del voto", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogout.addActionListener(_ -> {
            utenteLoggato = null;
            panelOperativo.setVisible(false);
            panelLogin.setVisible(true);
        });
    }

    private void apriFinestraRuolo() {
        String ruolo = utenteLoggato.getRuolo().toLowerCase();

        switch (ruolo) {
            case "organizzatore" -> {
                FinestraOrganizzatore finestra = new FinestraOrganizzatore(controller, utenteLoggato, this);
                finestra.setVisible(true);
            }
            case "giudice" -> {
                FinestraGiudice finestra = new FinestraGiudice(controller, utenteLoggato, this);
                finestra.setVisible(true);
            }
            case "partecipante" -> {
                FinestraPartecipante finestra = new FinestraPartecipante(controller, utenteLoggato, this);
                finestra.setVisible(true);
            }
            default -> JOptionPane.showMessageDialog(this, "Ruolo non riconosciuto!");
        }

        this.setVisible(false);
    }

    public static void main() {
        SwingUtilities.invokeLater(Gui::new);
    }
}