package controller;

import model.*;
import dao.*;
import postgresdao.*;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Controller {
    private Hackathon hackathon;
    private Utente utenteLoggato;

    private final HackathonDAO hackathonDAO;
    private final UtenteDAO utenteDAO;
    private final GiudiceDAO giudiceDAO;
    private final TeamDAO teamDAO;
    private final PartecipanteDAO partecipanteDAO;
    private final ClassificaDAO classificaDAO;
    private final VotoDAO votoDAO;
    private final DocumentoDAO documentoDAO;

    public Controller() {
        // Inizializza i DAO
        this.hackathonDAO = new PostgresHackathonDAO();
        this.utenteDAO = new PostgresUtenteDAO();
        this.giudiceDAO = new PostgresGiudiceDAO();
        this.teamDAO = new PostgresTeamDAO();
        this.partecipanteDAO = new PostgresPartecipanteDAO();
        this.classificaDAO = new PostgresClassificaDAO();
        this.votoDAO = new PostgresVotoDAO();
        this.documentoDAO = new PostgresDocumentoDAO();
        
        // Carica l'hackathon esistente o ne crea uno nuovo
        this.hackathon = caricaHackathonEsistente();
    }
    
    private Hackathon caricaHackathonEsistente() {
        // Prova a caricare l'hackathon esistente (assumiamo che ce ne sia solo uno per semplicità)
        List<Hackathon> hackathons = hackathonDAO.findAll();
        if (!hackathons.isEmpty()) {
            return hackathons.getFirst();
        } else {
            // Se non esiste, crea un nuovo hackathon vuoto
            return new Hackathon();
        }
    }

    // --- AUTENTICAZIONE ---
    public Optional<Utente> registraUtente(String nome, String email, String password, String ruolo) {
        // Verifica se l'utente esiste già
        Utente esistente = utenteDAO.trovaPerEmail(email);
        if (esistente != null) {
            return Optional.empty();
        }
        
        // Crea nuovo utente
        Utente u = new Utente(nome, email, password, ruolo);
        
        // Salva utente
        boolean salvato = utenteDAO.salva(u);
        if (salvato) {
            Utente utenteCreato = utenteDAO.trovaPerEmail(email);
            return Optional.ofNullable(utenteCreato);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Utente> login(String email, String password) {
        // Cerca utente per email
        Utente u = utenteDAO.trovaPerEmail(email);
        
        // Verifica che esista e che la password sia corretta
        if (u == null) {
            return Optional.empty();
        }
        
        if (!u.getPassword().equals(password)) {
            return Optional.empty();
        }
        
        // Imposta utente loggato
        this.utenteLoggato = u;
        
        // Ritorna utente
        return Optional.of(u);
    }

    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    // --- ORGANIZZATORE ---
    public boolean creaHackathon(String titolo, String sede, int maxPartecipanti, int maxTeam) {
        if (titolo == null || titolo.isBlank() || sede == null || sede.isBlank()) return false;
        hackathon.setTitolo(titolo);
        hackathon.setSede(sede);
        hackathon.setMaxPartecipanti(maxPartecipanti);
        hackathon.setMaxTeam(maxTeam);

        boolean risultato;
        // Se l'hackathon ha già un ID, aggiorna invece di salvare
        if (hackathon.getId() > 0) {
            risultato = hackathonDAO.aggiorna(hackathon);
        } else {
            risultato = hackathonDAO.salva(hackathon);
        }
        
        if (risultato) {
            Hackathon h = hackathonDAO.trovaPerId(hackathon.getId());
            if (h != null) hackathon = h;
        }
        return risultato;
    }
    
    public boolean creaHackathon(String titolo, String sede, int maxPartecipanti, int maxTeam,
                               LocalDateTime dataInizio, LocalDateTime dataFine, 
                               LocalDateTime inizioIscrizioni, LocalDateTime fineIscrizioni) {
        if (titolo == null || titolo.isBlank() || sede == null || sede.isBlank()) return false;
        hackathon.setTitolo(titolo);
        hackathon.setSede(sede);
        hackathon.setMaxPartecipanti(maxPartecipanti);
        hackathon.setMaxTeam(maxTeam);
        hackathon.setDataInizio(dataInizio);
        hackathon.setDataFine(dataFine);
        hackathon.setInizioIscrizioni(inizioIscrizioni);
        hackathon.setFineIscrizioni(fineIscrizioni);

        boolean risultato;
        // Se l'hackathon ha già un ID, aggiorna invece di salvare
        if (hackathon.getId() > 0) {
            risultato = hackathonDAO.aggiorna(hackathon);
        } else {
            risultato = hackathonDAO.salva(hackathon);
        }
        
        if (risultato) {
            Hackathon h = hackathonDAO.trovaPerId(hackathon.getId());
            if (h != null) hackathon = h;
        }
        return risultato;
    }

    
    public boolean aggiungiGiudice(String nome, String email) {
        // Verifica che esista un hackathon
        if (hackathon.getId() <= 0) {
            boolean hackathonCreato = creaHackathonDefault();
            if (!hackathonCreato) {
                System.err.println("Impossibile creare un hackathon di default");
                return false;
            }
        }
        
        // Registriamo direttamente il giudice senza invito
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "giudice");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Giudice g = new Giudice(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        
        // Salva il giudice nel database
        if (!giudiceDAO.salva(g)) return false;
        
        // Aggiorna l'oggetto hackathon in memoria
        hackathon.getGiudici().add(g);
        
        // Ricarica i giudici dal database per assicurarsi che siano aggiornati
        caricaGiudiciDaDB();
        
        return true;
    }

    public boolean aggiungiPartecipante(String nome, String email) {
        // Verifica che esista un hackathon
        if (hackathon.getId() <= 0) {
            boolean hackathonCreato = creaHackathonDefault();
            if (!hackathonCreato) {
                System.err.println("Impossibile creare un hackathon di default");
                return false;
            }
        }
        
        // Registriamo direttamente il partecipante senza invito
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "partecipante");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Partecipante p = new Partecipante(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        
        // Salva il partecipante nel database
        if (!partecipanteDAO.salva(p)) return false;
        
        // Aggiorna l'oggetto hackathon in memoria
        hackathon.getPartecipanti().add(p);
        
        // Aggiorna la lista dei partecipanti in memoria
        List<Partecipante> partecipantiAggiornati = partecipanteDAO.findAll();
        hackathon.getPartecipanti().clear();
        hackathon.getPartecipanti().addAll(partecipantiAggiornati);
        
        return true;
    }

    // --- TEAM ---
    /**
     * Crea un nuovo team e associa un partecipante.
     * <p>
     * Questo metodo esegue diverse operazioni:
     * <ol>
     *   <li>Verifica che il nome del team sia valido</li>
     *   <li>Crea un Hackathon di default se necessario</li>
     *   <li>Verifica che il nome del team non sia già in uso</li>
     *   <li>Crea e salva il nuovo team</li>
     *   <li>Verifica che il team non abbia già raggiunto il limite di 3 partecipanti</li>
     *   <li>Associa il partecipante al team</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Nota importante:</strong> Un team può avere al massimo 3 partecipanti.
     * Questo limite è implementato con controlli espliciti in questo metodo.
     * </p>
     * 
     * @param nomeTeam Nome del nuovo team
     * @param idPartecipante ID del partecipante da associare al team
     * @return true se l'operazione è riuscita, false altrimenti
     */
    public boolean creaTeam(String nomeTeam, int idPartecipante) {
        // Controllo nome team
        if (nomeTeam == null || nomeTeam.isBlank()) {
            return false;
        }
        
        // Crea hackathon se non esiste
        if (hackathon.getId() <= 0) {
            if (!creaHackathonDefault()) {
                System.err.println("Impossibile creare hackathon");
                return false;
            }
        }

        // Controlla se il nome team esiste già
        if (teamDAO.trovaTeamPerNome(nomeTeam) != null) {
            System.err.println("Nome team già usato");
            return false;
        }

        // Crea team
        Team nuovoTeam = new Team(0, nomeTeam, hackathon);
        nuovoTeam.setDescrizione("Team " + nomeTeam);
        nuovoTeam.setProgresso(0);

        // Salva team
        Team teamSalvato = teamDAO.salva(nuovoTeam);
        if (teamSalvato == null) {
            System.err.println("Errore salvataggio team");
            return false;
        }

        // Aggiunge team all'hackathon
        hackathon.aggiungiTeam(teamSalvato);

        // Controlla numero partecipanti
        int numPartecipanti = 0;
        try {
            numPartecipanti = ((PostgresPartecipanteDAO)partecipanteDAO).contaPartecipantiInTeam(teamSalvato.getId());
        } catch (Exception e) {
            System.err.println("Errore conteggio partecipanti");
        }
        
        // Implementazione del limite di 3 partecipanti per team
        if (numPartecipanti >= 3) {
            System.err.println("Team già pieno (max 3)");
            return false;
        }
        
        // Associa partecipante
        Partecipante p = partecipanteDAO.findById(idPartecipante);
        if (p != null) {
            p.setTeamId(teamSalvato.getId());
            partecipanteDAO.aggiorna(p);
        } else {
            // Cerca utente
            Utente u = utenteDAO.trovaPerId(idPartecipante);
            if (u != null && u.getRuolo().equalsIgnoreCase("partecipante")) {
                // Crea partecipante
                Partecipante nuovoP = new Partecipante(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
                nuovoP.setTeamId(teamSalvato.getId());
                partecipanteDAO.salvaPartecipante(nuovoP);
            } else {
                System.err.println("Utente non trovato o non è partecipante");
            }
        }

        return true;
    }
    
    private boolean creaHackathonDefault() {
        // Crea un hackathon con valori di default
        hackathon.setTitolo("Hackathon Default");
        hackathon.setSede("Sede Default");
        hackathon.setMaxPartecipanti(50);
        hackathon.setMaxTeam(10);
        
        // Imposta date di default (oggi e tra un mese)
        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime traUnMese = oggi.plusMonths(1);
        LocalDateTime traUnaSett = oggi.plusWeeks(1);
        LocalDateTime traUnMeseEUnaSett = traUnMese.plusWeeks(1);
        
        hackathon.setDataInizio(traUnMese);
        hackathon.setDataFine(traUnMeseEUnaSett);
        hackathon.setInizioIscrizioni(oggi);
        hackathon.setFineIscrizioni(traUnaSett);
        
        boolean salvato = hackathonDAO.salva(hackathon);
        if (salvato) {
            Hackathon h = hackathonDAO.trovaPerId(hackathon.getId());
            if (h != null) hackathon = h;
        }
        return salvato;
    }

    public List<Team> getListaTeam() {
        return hackathon.getTeams();
    }

    public void caricaTeamsDaDB() {
        hackathon.getTeams().clear();
        hackathon.getTeams().addAll(teamDAO.findAllByHackathonId(hackathon.getId()));
    }

    public void caricaGiudiciDaDB() {
        hackathon.getGiudici().clear();
        hackathon.getGiudici().addAll(giudiceDAO.findAllByHackathonId(hackathon.getId()));
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    // --- CLASSIFICA ---
    public Classifica getClassifica() {
        return classificaDAO.getClassifica(hackathon.getId());
    }

    // --- VOTI ---
    public boolean assegnaVoto(int idGiudice, int idTeam, int punteggio) {
        // Controllo punteggio
        if (punteggio < 1 || punteggio > 10) {
            System.err.println("Il punteggio deve essere compreso tra 1 e 10");
            return false;
        }
        
        // Verifica che il team esista
        Team team = teamDAO.trovaPerId(idTeam);
        if (team == null) {
            System.err.println("Team non trovato con ID: " + idTeam);
            return false;
        }
        
        // Verifica che l'utente sia un giudice
        Giudice giudice = giudiceDAO.trovaGiudicePerId(idGiudice);
        if (giudice == null) {
            // Se non lo trova nella tabella giudici, controlla nella tabella utenti
            Utente utente = utenteDAO.trovaPerId(idGiudice);
            if (utente == null || !utente.getRuolo().equalsIgnoreCase("giudice")) {
                System.err.println("Utente non trovato o non è un giudice");
                return false;
            }
        }
        
        // Crea e salva il voto
        Voto voto = new Voto(idTeam, idGiudice, punteggio);
        
        System.out.println("Salvataggio voto: Giudice=" + idGiudice + ", Team=" + idTeam + ", Voto=" + punteggio);
        
        if (!votoDAO.salvaVoto(voto)) {
            System.err.println("Errore nel salvataggio del voto");
            return false;
        }
        
        // Aggiorna i team
        caricaTeamsDaDB();
        
        System.out.println("Voto assegnato con successo");
        return true;
    }

    // --- DOCUMENTI ---
    public boolean inviaDocumento(int idPartecipante, String nomeFile) {
        // Cerca partecipante
        Partecipante p = partecipanteDAO.findById(idPartecipante);
        if (p == null) {
            System.err.println("Partecipante non trovato");
            return false;
        }
        
        // Trova team del partecipante
        int idTeam = p.getTeamId();
        
        // Se non ha team, cerca nel database
        if (idTeam <= 0) {
            idTeam = getTeamIdByPartecipante(idPartecipante);
            if (idTeam <= 0) {
                System.err.println("Partecipante senza team");
                return false;
            }
        }

        // Verifica team
        Team team = teamDAO.trovaPerId(idTeam);
        if (team == null) {
            System.err.println("Team non trovato");
            return false;
        }

        // Salva documento
        Documento doc = new Documento();
        doc.setIdTeam(idTeam);
        doc.setTitolo(nomeFile);

        return documentoDAO.salvaDocumento(doc);
    }

    public List<Documento> getDocumentiDiTeam(int idTeam) {
        return documentoDAO.findByTeam(idTeam);
    }

    // --- SUPPORTO ---
    public int getTeamIdByPartecipante(int idPartecipante) {
        // Prima prova a ottenere il partecipante dal DAO
        Partecipante p = partecipanteDAO.findById(idPartecipante);
        if (p != null && p.getTeamId() > 0) {
            return p.getTeamId();
        }
        
        // Se non lo trova, cerca direttamente nel database
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnessioneDatabase.getInstance().getConnection();
            String sql = "SELECT team_id FROM partecipante WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPartecipante);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int teamId = rs.getInt("team_id");
                if (!rs.wasNull()) {
                    return teamId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore database: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Errore chiusura connessione: " + e.getMessage());
            }
        }
        
        return -1; // Nessuna associazione a team trovata
    }
    
    public PartecipanteDAO getPartecipanteDAO() {
        return partecipanteDAO;
    }
}

