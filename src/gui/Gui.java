package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class Gui extends JFrame {

    private Controller controller;
    private Utente utenteLoggato;

    // Componenti base
    private JPanel panelLogin;
    private JTextField txtNome, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRuolo;
    private JButton btnLogin;

    private JPanel panelOperativo;
    private JButton btnCreaTeam, btnListaGiudici, btnListaPartecipanti, btnValutaTeam, btnLogout;
    private int idGiudice;

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
        btnLogin = new JButton("Login / Registrati");
        panelLogin.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
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
                // Se login fallisce, registro
                opt = controller.registraUtente(nome, email, pwd, ruolo);
                if (opt.isPresent()) {
                    utenteLoggato = opt.get();
                    JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nella registrazione!", "Errore", JOptionPane.ERROR_MESSAGE);
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

        btnCreaTeam        = new JButton("Crea Team");
        btnListaGiudici    = new JButton("Lista Giudici");
        btnListaPartecipanti = new JButton("Lista Partecipanti");
        btnValutaTeam      = new JButton("Valuta Team");
        btnLogout          = new JButton("Logout");

        gbc.gridx=0; gbc.gridy=0; panelOperativo.add(btnCreaTeam, gbc);
        gbc.gridx=1; panelOperativo.add(btnListaGiudici, gbc);
        gbc.gridx=0; gbc.gridy=1; panelOperativo.add(btnListaPartecipanti, gbc);
        gbc.gridx=1; panelOperativo.add(btnValutaTeam, gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panelOperativo.add(btnLogout, gbc);

        // listener
        btnCreaTeam.addActionListener(e -> {
            String nomeTeam = JOptionPane.showInputDialog(this, "Nome team:");
            if (nomeTeam!=null && !nomeTeam.isBlank()) {
                boolean ok = controller.creaTeam(nomeTeam.trim());
                JOptionPane.showMessageDialog(this, ok ? "Team creato" : "Errore");
            }
        });
        btnListaGiudici.addActionListener(e -> {
            controller.caricaGiudiciDaDB();
            List<Giudice> list = controller.getHackathon().getGiudici();
            String s = list.isEmpty() ? "Nessun giudice" :
                    String.join("\n", list.stream().map(Giudice::getNome).toArray(String[]::new));
            JOptionPane.showMessageDialog(this, s);
        });
        btnListaPartecipanti.addActionListener(e -> {
            controller.caricaTeamsDaDB();
            List<Partecipante> pp = controller.getHackathon().getPartecipanti();
            String s = pp.isEmpty() ? "Nessun partecipante" :
                    String.join("\n", pp.stream().map(Partecipante::getNome).toArray(String[]::new));
            JOptionPane.showMessageDialog(this, s);
        });
        btnValutaTeam.addActionListener(e -> {
            String team = JOptionPane.showInputDialog(this,"Nome team:");
            String v    = JOptionPane.showInputDialog(this,"Voto 1-10:");
            try {
                int voto = Integer.parseInt(v);

                boolean ok = controller.assegnaVoto(idGiudice, Integer.parseInt(team),voto);
                JOptionPane.showMessageDialog(this, ok?"Votato":"Errore voto");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this,"Voto non valido");
            }
        });
        btnLogout.addActionListener(e -> {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui::new);
    }
}
