package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class FinestraLogin extends JFrame {
    private final Controller ctrl;
    private final JTextField tfNome, tfEmail;
    private final JPasswordField pfPassword;
    private final JComboBox<String> cbRuolo;

    public FinestraLogin(Controller ctrl) {
        super("Login / Registrazione");
        this.ctrl = ctrl;
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Nome:"));
        tfNome = new JTextField();
        add(tfNome);

        add(new JLabel("Email:"));
        tfEmail = new JTextField();
        add(tfEmail);

        add(new JLabel("Password:"));
        pfPassword = new JPasswordField();
        add(pfPassword);

        add(new JLabel("Ruolo:"));
        cbRuolo = new JComboBox<>(new String[]{"organizzatore", "giudice", "partecipante"});
        add(cbRuolo);

        JButton btn = new JButton("Login / Registrati");
        btn.addActionListener(e -> onSubmit());
        add(new JLabel()); // spazio vuoto
        add(btn);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void onSubmit() {
        String nome = tfNome.getText().trim();
        String email = tfEmail.getText().trim();
        String pwd = new String(pfPassword.getPassword());
        String ruolo = (String) cbRuolo.getSelectedItem();

        if (email.isBlank() || pwd.isBlank()) {
            JOptionPane.showMessageDialog(this, "Email e password sono obbligatori.");
            return;
        }

        // 1. Provo login
        Optional<Utente> opt = ctrl.login(email, pwd);
        if (opt.isPresent()) {
            JOptionPane.showMessageDialog(this, "Login riuscito!");
            this.setVisible(false);
            apriFinestraRuolo(opt.get());
            return;
        }

        // 2. Provo registrazione (se nome è compilato)
        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(this, "Nome richiesto per la registrazione.");
            return;
        }

        Optional<Utente> nuovo = ctrl.registraUtente(nome, email, pwd, ruolo);
        if (nuovo.isPresent()) {
            JOptionPane.showMessageDialog(this, "Registrazione riuscita!");
            this.setVisible(false);
            apriFinestraRuolo(nuovo.get());
        } else {
            JOptionPane.showMessageDialog(this, "Registrazione fallita (email già usata?).");
        }
    }

    private void apriFinestraRuolo(Utente u) {
        switch (u.getRuolo().toLowerCase()) {
            case "organizzatore" -> new FinestraOrganizzatore(ctrl, u, this);
            case "giudice" -> new FinestraGiudice(ctrl, u, this);
            case "partecipante" -> new FinestraPartecipante(ctrl, u, this);
            default -> JOptionPane.showMessageDialog(this, "Ruolo sconosciuto!");
        }
    }
}
