package controller;

import model.*;
import dao.*;
import postgresdao.*;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            return hackathons.getFirst(); // Prendi il primo hackathon trovato
        } else {
            // Se non esiste, crea un nuovo hackathon vuoto
            return new Hackathon();
        }
    }

    // --- AUTENTICAZIONE ---
    public Optional<Utente> registraUtente(String nome, String email, String password, String ruolo) {
        // Verifica se l'utente esiste già
        if (utenteDAO.trovaPerEmail(email) != null) return Optional.empty();
        
        // Procedi con la registrazione (senza verifica invito)
        Utente u = new Utente(nome, email, password, ruolo);
        if (utenteDAO.salva(u)) return Optional.ofNullable(utenteDAO.trovaPerEmail(email));
        return Optional.empty();
    }

    public Optional<Utente> login(String email, String password) {
        Utente u = utenteDAO.trovaPerEmail(email);
        if (u == null || !u.getPassword().equals(password)) return Optional.empty();
        this.utenteLoggato = u;
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

        boolean salvato = hackathonDAO.salva(hackathon);
        if (salvato) {
            Hackathon h = hackathonDAO.trovaPerId(hackathon.getId());
            if (h != null) hackathon = h;
        }
        return salvato;
    }

    // Metodi rimossi: invitaGiudice e invitaPartecipante
    
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
    public boolean creaTeam(String nomeTeam, int idPartecipante) {
        if (nomeTeam == null || nomeTeam.isBlank()) return false;
        
        // Se non esiste un hackathon, ne creiamo uno di default
        if (hackathon.getId() <= 0) {
            boolean hackathonCreato = creaHackathonDefault();
            if (!hackathonCreato) {
                System.err.println("Impossibile creare un hackathon di default");
                return false;
            }
        }

        // Controlla se esiste già un team con quel nome nel DB
        if (teamDAO.trovaTeamPerNome(nomeTeam) != null) {
            System.err.println("Esiste già un team con questo nome");
            return false;
        }

        // Crea il nuovo team
        Team nuovoTeam = new Team(0, nomeTeam, hackathon);
        
        // Imposta una descrizione di default e progresso iniziale
        nuovoTeam.setDescrizione("Team " + nomeTeam);
        nuovoTeam.setProgresso(0);

        // Salva il team, teamDAO.salva ritorna il Team salvato con ID valorizzato
        Team teamSalvato = teamDAO.salva(nuovoTeam);
        if (teamSalvato == null) {
            System.err.println("Errore nel salvataggio del team");
            return false;
        }

        // Aggiungi il team all'hackathon in memoria
        hackathon.aggiungiTeam(teamSalvato);

        try {
            // Verifica che il team non abbia già 3 partecipanti
            int numPartecipanti = ((PostgresPartecipanteDAO)partecipanteDAO).contaPartecipantiInTeam(teamSalvato.getId());
            if (numPartecipanti >= 3) {
                System.err.println("Il team ha già raggiunto il numero massimo di 3 partecipanti");
                return false;
            }
            
            // Associa partecipante al team
            Partecipante p = partecipanteDAO.findById(idPartecipante);
            if (p != null) {
                p.setTeamId(teamSalvato.getId());
                partecipanteDAO.aggiorna(p);
            } else {
                // Se il partecipante non esiste nella tabella partecipante, ma esiste nella tabella utente,
                // lo creiamo nella tabella partecipante
                Utente u = utenteDAO.trovaPerId(idPartecipante);
                if (u != null && u.getRuolo().equalsIgnoreCase("partecipante")) {
                    Partecipante nuovoP = new Partecipante(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
                    nuovoP.setTeamId(teamSalvato.getId());
                    if (partecipanteDAO.salvaPartecipante(nuovoP)) {
                        System.out.println("Partecipante creato e associato al team");
                        

                    } else {
                        System.err.println("Errore nella creazione del partecipante");
                    }
                } else {
                    System.err.println("Utente non trovato o non è un partecipante con ID: " + idPartecipante);
                }
            }
        } catch (Exception e) {

            System.err.println("Avviso: Impossibile associare il partecipante al team: " + e.getMessage());
            System.err.println("Il team è stato creato, ma l'associazione con il partecipante potrebbe non essere stata salvata.");
        }

        return true;
    }
    
    private boolean creaHackathonDefault() {
        // Crea un hackathon con valori di default
        hackathon.setTitolo("Hackathon Default");
        hackathon.setSede("Sede Default");
        hackathon.setMaxPartecipanti(50);
        hackathon.setMaxTeam(10);
        
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
        // Validazione del punteggio
        if (punteggio < 1 || punteggio > 10) {
            System.err.println("Il punteggio deve essere compreso tra 1 e 10");
            return false;
        }
        
        try {
            // Verifica diretta nel database che il team esista
            boolean teamEsiste = false;
            try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT id FROM team WHERE id = ?")) {
                stmt.setInt(1, idTeam);
                ResultSet rs = stmt.executeQuery();
                teamEsiste = rs.next();
            } catch (SQLException e) {
                System.err.println("Errore nella verifica dell'esistenza del team: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (!teamEsiste) {
                System.err.println("Team con ID " + idTeam + " non esiste nel database");
                return false;
            }
            
            // Verifica che l'utente sia un giudice
            Giudice giudice = giudiceDAO.trovaGiudicePerId(idGiudice);
            if (giudice == null) {
                // Prova a cercare l'utente e verificare che sia un giudice
                Utente utente = utenteDAO.trovaPerId(idGiudice);
                if (utente == null || !utente.getRuolo().equalsIgnoreCase("giudice")) {
                    System.err.println("Utente non trovato o non è un giudice. ID: " + idGiudice);
                    return false;
                }
            }
            
            // Crea e salva il voto
            Voto voto = new Voto(idTeam, idGiudice, punteggio);
            
            // Stampa informazioni di debug
            System.out.println("Tentativo di salvare voto: Giudice ID=" + idGiudice + 
                              ", Team ID=" + idTeam + ", Punteggio=" + punteggio);
            
            boolean ok = votoDAO.salvaVoto(voto);
            if (!ok) {
                System.err.println("Errore nel salvataggio del voto");
                return false;
            }
    
            // Aggiorna la classifica in memoria

            // Aggiorna i team nell'hackathon con i dati aggiornati
            caricaTeamsDaDB();
            
            System.out.println("Voto assegnato con successo: Giudice ID " + idGiudice + 
                               " ha votato il Team ID " + idTeam + " con punteggio " + punteggio);
            return true;
        } catch (Exception e) {
            System.err.println("Errore durante l'assegnazione del voto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- DOCUMENTI ---
    public boolean inviaDocumento(int idPartecipante, String nomeFile) {
        // Verifica che il partecipante esista
        Partecipante p = partecipanteDAO.findById(idPartecipante);
        if (p == null) {
            System.err.println("Partecipante non trovato con ID: " + idPartecipante);
            return false;
        }
        
        // Ottieni l'ID del team del partecipante
        int idTeam = p.getTeamId();
        if (idTeam <= 0) {
            // Prova a recuperare l'ID del team direttamente dal database
            idTeam = getTeamIdByPartecipante(idPartecipante);
            if (idTeam <= 0) {
                System.err.println("Il partecipante non è associato a nessun team");
                return false;
            }
        }

        // Verifica che il team esista
        Team team = teamDAO.trovaPerId(idTeam);
        if (team == null) {
            System.err.println("Team non trovato con ID: " + idTeam);
            return false;
        }

        // Crea e salva il documento
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
        try {
            // Prima prova a ottenere il partecipante dal DAO
            Partecipante p = partecipanteDAO.findById(idPartecipante);
            if (p != null && p.getTeamId() > 0) {
                return p.getTeamId();
            }
            
            // Se non ha un team_id, prova a cercarlo direttamente nel database
            try (Connection conn = database.ConnessioneDatabase.getInstance().getConnection()) {
                String sql = "SELECT team_id FROM partecipante WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idPartecipante);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int teamId = rs.getInt("team_id");
                            if (!rs.wasNull()) {
                                return teamId;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Errore durante la ricerca del team del partecipante: " + e.getMessage());
                e.printStackTrace();
            }
            
            return -1; // Nessuna associazione a team trovata
        } catch (Exception e) {
            System.err.println("Errore durante il recupero del team del partecipante: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    public PartecipanteDAO getPartecipanteDAO() {
        return partecipanteDAO;
    }
}

