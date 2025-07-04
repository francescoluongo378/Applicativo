package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

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
        // usa il costruttore no-arg del Controller
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
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String pwd   = new String(txtPassword.getPassword());
            String ruolo = (String) comboRuolo.getSelectedItem();

            if (nome.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Provo login
            Optional<Utente> opt = controller.login(email, pwd);
            if (opt.isPresent()) {
                utenteLoggato = opt.get();
                if (!utenteLoggato.getRuolo().equalsIgnoreCase(ruolo)) {
                    JOptionPane.showMessageDialog(this,
                            "Ruolo errato rispetto alla registrazione precedente!",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Se login fallisce, procediamo direttamente con la registrazione
                opt = controller.registraUtente(nome, email, pwd, ruolo);
                if (opt.isPresent()) {
                    utenteLoggato = opt.get();
                    JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nella registrazione! Verifica che l'email non sia già in uso.", 
                                                 "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Passo alla finestra del ruolo
            this.setVisible(false);
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
            List<Giudice> list = controller.getHackathon().getGiudici();
            String s = list.isEmpty() ? "Nessun giudice" :
                    String.join("\n", list.stream().map(Giudice::getNome).toArray(String[]::new));
            JOptionPane.showMessageDialog(this, s);
        });
        btnListaPartecipanti.addActionListener(_ -> {
            controller.caricaTeamsDaDB();
            List<Partecipante> pp = controller.getHackathon().getPartecipanti();
            String s = pp.isEmpty() ? "Nessun partecipante" :
                    String.join("\n", pp.stream().map(Partecipante::getNome).toArray(String[]::new));
            JOptionPane.showMessageDialog(this, s);
        });
        btnValutaTeam.addActionListener(_ -> {
            // Prima carichiamo i team disponibili
            controller.caricaTeamsDaDB();
            List<Team> teams = controller.getListaTeam();
            if (teams.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun team disponibile da valutare.");
                return;
            }
            
            // Mostriamo la lista dei team
            StringBuilder teamList = new StringBuilder("Team disponibili:\n");
            for (Team t : teams) {
                teamList.append("- ").append(t.getNome()).append(" (ID: ").append(t.getId()).append(")\n");
            }
            JOptionPane.showMessageDialog(this, teamList.toString());
            
            // Chiediamo l'ID del team e il voto
            String teamIdStr = JOptionPane.showInputDialog(this, "ID del team da valutare:");
            String v = JOptionPane.showInputDialog(this, "Voto 1-10:");
            
            try {
                int teamId = Integer.parseInt(teamIdStr);
                int voto = Integer.parseInt(v);
                
                // Utilizziamo l'ID del giudice loggato
                int idGiudiceLoggato = controller.getUtenteLoggato().getId();
                boolean ok = controller.assegnaVoto(idGiudiceLoggato, teamId, voto);
                JOptionPane.showMessageDialog(this, ok ? "Voto assegnato con successo" : "Errore nell'assegnazione del voto");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Voto o ID team non valido");
            }
        });
        btnLogout.addActionListener(_ -> {
            utenteLoggato = null;
            panelOperativo.setVisible(false);
            panelLogin.setVisible(true);
        });
    }

    private void apriFinestraRuolo() {
        switch (utenteLoggato.getRuolo().toLowerCase()) {
            case "organizzatore" -> new FinestraOrganizzatore(controller, utenteLoggato, this).setVisible(true);
            case "giudice" -> new FinestraGiudice(controller, utenteLoggato, this).setVisible(true);
            case "partecipante" -> new FinestraPartecipante(controller, utenteLoggato, this).setVisible(true);
            default -> JOptionPane.showMessageDialog(this, "Ruolo non riconosciuto!");
        }
        this.setVisible(false); // Nasconde la finestra di login
    }

    public static void main() {
        SwingUtilities.invokeLater(Gui::new);
    }
}