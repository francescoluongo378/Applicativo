package controller;

import model.*;
import dao.*;
import postgresdao.*;

import java.util.List;
import java.util.Optional;

public class Controller {
    private Hackathon hackathon;
    private Utente utenteLoggato;

    private UtenteDAO utenteDAO;
    private GiudiceDAO giudiceDAO;
    private TeamDAO teamDAO;
    private PartecipanteDAO partecipanteDAO;
    private ClassificaDAO classificaDAO;
    private VotoDAO votoDAO;
    private DocumentoDAO documentoDAO;

    public Controller() {
        this.hackathon = new Hackathon();
        this.utenteDAO = new PostgresUtenteDAO();
        this.giudiceDAO = new PostgresGiudiceDAO();
        this.teamDAO = new PostgresTeamDAO();
        this.partecipanteDAO = new PostgresPartecipanteDAO();
        this.classificaDAO = new PostgresClassificaDAO();
        this.votoDAO = new PostgresVotoDAO();
        this.documentoDAO = new PostgresDocumentoDAO();
    }

    // --- AUTENTICAZIONE ---

    /** Registra un nuovo utente se l'email non esiste già */
    public Optional<Utente> registraUtente(String nome, String email, String password, String ruolo) {
        if (utenteDAO.trovaPerEmail(email) != null) {
            // Email già usata
            return Optional.empty();
        }
        Utente u = new Utente(nome, email, password, ruolo);
        if (utenteDAO.salva(u)) {
            return Optional.ofNullable(utenteDAO.trovaPerEmail(email));
        }
        return Optional.empty();
    }

    /** Login utente con email e password */
    public Optional<Utente> login(String email, String password) {
        Utente u = utenteDAO.trovaPerEmail(email);
        if (u == null || !u.getPassword().equals(password)) {
            return Optional.empty();
        }
        this.utenteLoggato = u;
        return Optional.of(u);
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    // --- ORGANIZZATORE ---

    /** Crea un hackathon in memoria (NON salva su DB) */
    public boolean creaHackathon(String titolo, String sede, int maxPartecipanti, int maxTeam) {
        if (titolo == null || titolo.isBlank() || sede == null || sede.isBlank()) return false;
        hackathon.setTitolo(titolo);
        hackathon.setSede(sede);
        hackathon.setMaxPartecipanti(maxPartecipanti);
        hackathon.setMaxTeam(maxTeam);
        return true;
    }

    /** Registra un organizzatore con password di default "changeme" */
    public boolean aggiungiOrganizzatore(String nome, String email) {
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "organizzatore");
        return opt.isPresent();
    }

    /** Registra un giudice, crea relativo Giudice e lo aggiunge all'hackathon */
    public boolean aggiungiGiudice(String nome, String email) {
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "giudice");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Giudice g = new Giudice(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        if (!giudiceDAO.salva(g)) return false;
        hackathon.getGiudici().add(g);
        return true;
    }

    /** Registra un partecipante, crea relativo Partecipante e lo aggiunge all'hackathon */
    public boolean aggiungiPartecipante(String nome, String email) {
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "partecipante");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Partecipante p = new Partecipante(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        if (!partecipanteDAO.salva(p)) return false;
        hackathon.getPartecipanti().add(p);
        return true;
    }

    /** Crea un nuovo team associato all'hackathon (senza partecipante) */
    public boolean creaTeam(String nome) {
        if (nome == null || nome.isBlank()) return false;
        Team t = new Team(0, nome, hackathon.getId());
        if (!teamDAO.salva(t)) return false;
        hackathon.aggiungiTeam(t);
        return true;
    }

    /** Crea un team associato all'hackathon e iscrive il partecipante al team */
    public boolean creaTeam(String nomeTeam, Utente partecipante) {
        if (nomeTeam == null || nomeTeam.isBlank()) return false;

        Team nuovoTeam = new Team(0, nomeTeam, hackathon.getId());
        if (!teamDAO.salva(nuovoTeam)) return false;

        hackathon.aggiungiTeam(nuovoTeam);

        // Iscrivi il partecipante al team appena creato
        if (partecipante != null && "partecipante".equalsIgnoreCase(partecipante.getRuolo())) {
            Partecipante p = partecipanteDAO.findById(partecipante.getId());
            if (p != null) {
                p.setTeamId(nuovoTeam.getId());
                partecipanteDAO.aggiorna(p);
            }
        }

        return true;
    }

    // --- OTTIENI LISTE E SINGOLI ---

    public List<Team> getListaTeam() {
        return hackathon.getTeams();
    }

    public Team getTeamById(int id) {
        return teamDAO.findById(id);
    }

    public List<Giudice> getGiudici() {
        return hackathon.getGiudici();
    }

    public List<Partecipante> getPartecipanti() {
        return hackathon.getPartecipanti();
    }

    public Partecipante getPartecipanteById(int id) {
        return partecipanteDAO.findById(id);
    }

    // --- CARICAMENTO DA DATABASE ---

    /** Ricarica la lista di team da DB */
    public void caricaTeamsDaDB() {
        hackathon.getTeams().clear();
        hackathon.getTeams().addAll(teamDAO.findAll());
    }

    /** Ricarica la lista di giudici da DB */
    public void caricaGiudiciDaDB() {
        hackathon.getGiudici().clear();
        hackathon.getGiudici().addAll(giudiceDAO.findAll());
    }

    /** Ricarica la lista di partecipanti da DB */
    public void caricaPartecipantiDaDB() {
        hackathon.getPartecipanti().clear();
        hackathon.getPartecipanti().addAll(partecipanteDAO.findAll());
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    // --- CLASSIFICA ---

    public Classifica getClassifica(int hackathonId) {
        return classificaDAO.getClassifica(hackathonId);
    }

    public Classifica getClassifica() {
        return classificaDAO.getClassifica();
    }

    public void stampaClassifica(Classifica classifica) {
        List<Team> teams = classifica.getTeams();
        System.out.println("Classifica:");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.printf("%d. Team: %s - Punti: %d%n", i + 1, team.getNome(), team.getPuntiTotali());
        }
    }

    // --- VOTI ---

    /** Assegna un voto dato da un giudice a un team tramite id */
    public boolean assegnaVoto(int idGiudice, int idTeam, int punteggio) {
        if (punteggio < 0 || punteggio > 10) return false;

        Voto voto = new Voto();
        voto.setIdGiudice(idGiudice);
        voto.setIdTeam(idTeam);
        voto.setPunteggio(punteggio);

        return votoDAO.salvaVoto(voto);
    }

    /** Assegna un voto dato da giudice loggato a team tramite nome team */
    public boolean assegnaVoto(Utente idGiudice, String nomeTeam, int voto) {
        if (voto < 0 || voto > 10) return false;
        Optional<Team> teamOpt = hackathon.getTeams().stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nomeTeam))
                .findFirst();
        if (teamOpt.isEmpty() || utenteLoggato == null || !"giudice".equalsIgnoreCase(utenteLoggato.getRuolo())) {
            return false;
        }
        Team team = teamOpt.get();
        Voto nuovoVoto = new Voto(utenteLoggato.getId(), team.getId(), voto);
        boolean salvato = votoDAO.salvaVoto(nuovoVoto);
        if (salvato) {
            team.aggiungiVoto(nuovoVoto);
        }
        return salvato;
    }

    // --- AGGIORNAMENTO PROGRESSO TEAM ---

    /**
     * Aggiorna il progresso di un team.
     * @param idTeam id del team
     * @param progressoValore valore del progresso (es. % completamento)
     * @return true se aggiornato con successo
     */
    public boolean aggiornaProgressoTeam(int idTeam, int progressoValore) {
        Team team = teamDAO.findById(idTeam);
        if (team == null) return false;
        team.setProgresso(progressoValore);
        return teamDAO.aggiorna(team);
    }

    // --- VALUTAZIONE TEAM ---

    /**
     * Valuta un team con un voto da un giudice.
     * @param idGiudice id del giudice
     * @param idTeam id del team
     * @param punteggio voto da 0 a 10
     * @return true se salvato con successo
     */
    public boolean valutaTeam(Utente idGiudice, String idTeam, int punteggio) {
        return assegnaVoto(idGiudice, idTeam, punteggio);
    }

    // --- DOCUMENTI ---

    public boolean aggiungiDocumento(int idTeam, String nomeFile, int contenuto) {
        Documento doc = new Documento();
        doc.setId(idTeam);
        doc.setTitolo(nomeFile);
        doc.getIdPartecipante();
        return documentoDAO.salvaDocumento(doc);
    }

    public List<Documento> getDocumentiDiTeam(int idTeam) {
        return documentoDAO.findByPartecipante(idTeam);
    }

    public void aggiornaProgressoTeam(String nomeTeam, String progresso) {
    }
}






