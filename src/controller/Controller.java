package controller;

import model.*;
import dao.*;
import postgresdao.*;

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
        // Registriamo direttamente il giudice senza invito
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "giudice");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Giudice g = new Giudice(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        if (!giudiceDAO.salva(g)) return false;
        hackathon.getGiudici().add(g);
        return true;
    }

    public boolean aggiungiPartecipante(String nome, String email) {
        // Registriamo direttamente il partecipante senza invito
        Optional<Utente> opt = registraUtente(nome, email, "changeme", "partecipante");
        if (opt.isEmpty()) return false;
        Utente u = opt.get();
        Partecipante p = new Partecipante(u.getId(), u.getNome(), u.getEmail(), u.getPassword());
        if (!partecipanteDAO.salva(p)) return false;
        hackathon.getPartecipanti().add(p);
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
            // Verifica che il team esista
            Team team = teamDAO.trovaPerId(idTeam);
            if (team == null) {
                // Ricarica i team dal database prima di verificare
                caricaTeamsDaDB();
                team = hackathon.getTeams().stream()
                    .filter(t -> t.getId() == idTeam)
                    .findFirst()
                    .orElse(null);
                
                if (team == null) {
                    System.err.println("Team non trovato con ID: " + idTeam);
                    return false;
                }
            }
            
            // Verifica che l'utente sia un giudice
            Utente giudice = utenteDAO.trovaPerId(idGiudice);
            if (giudice == null || !giudice.getRuolo().equalsIgnoreCase("giudice")) {
                System.err.println("Utente non trovato o non è un giudice. ID: " + idGiudice);
                return false;
            }
            
            // Crea e salva il voto
            Voto voto = new Voto(idGiudice, idTeam, punteggio);
            boolean ok = votoDAO.salvaVoto(voto);
            if (!ok) {
                System.err.println("Errore nel salvataggio del voto");
                return false;
            }
    
            // Aggiorna la classifica in memoria
            Classifica aggiornata = classificaDAO.getClassifica(hackathon.getId());
            hackathon.getTeams().clear();
            for (Team t : aggiornata.getTeams()) {
                hackathon.aggiungiTeam(t);
            }
            
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
        int idTeam = getTeamIdByPartecipante(idPartecipante);
        if (idTeam <= 0) return false;

        Documento doc = new Documento();
        doc.setIdTeam(idTeam);
        doc.setTitolo(nomeFile);
        return documentoDAO.salvaDocumento(doc);
    }

    public List<Documento> getDocumentiDiTeam(int idTeam) {
        return documentoDAO.findByTeam(idTeam);
    }

    // --- PROGRESSO ---
    public boolean aggiornaProgressoTeam(int idPartecipante, int incremento) {
        try {
            Partecipante p = partecipanteDAO.findById(idPartecipante);
            if (p == null) {
                System.err.println("Partecipante non trovato con ID: " + idPartecipante);
                return false;
            }
            
            int idTeam = p.getTeamId();
            if (idTeam <= 0) {
                System.err.println("Il partecipante non è associato a nessun team");
                return false;
            }
            
            // Ricarica i team dal database per assicurarsi di avere dati aggiornati
            caricaTeamsDaDB();
            
            Optional<Team> teamOpt = hackathon.getTeams().stream()
                    .filter(t -> t.getId() == idTeam)
                    .findFirst();
            
            if (teamOpt.isEmpty()) {
                // Se il team non è in memoria, proviamo a caricarlo direttamente dal database
                Team team = teamDAO.trovaPerId(idTeam);
                if (team == null) {
                    System.err.println("Team non trovato con ID: " + idTeam);
                    return false;
                }
                
                // Aggiorna il progresso
                int nuovoProgresso = Math.max(0, Math.min(100, team.getProgresso() + incremento));
                team.setProgresso(nuovoProgresso);
                boolean result = teamDAO.aggiorna(team);
                
                // Aggiorna anche la lista in memoria
                if (result) {
                    hackathon.getTeams().add(team);
                }
                
                return result;
            }
            
            Team team = teamOpt.get();
            int nuovoProgresso = Math.max(0, Math.min(100, team.getProgresso() + incremento));
            team.setProgresso(nuovoProgresso);
            return teamDAO.aggiorna(team);
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento del progresso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- SUPPORTO ---
    public int getTeamIdByPartecipante(int idPartecipante) {
        Partecipante p = partecipanteDAO.findById(idPartecipante);
        if (p != null && p.getTeamId() > 0) {
            return p.getTeamId();
        }
        return -1; // No team association found
    }
    
    public PartecipanteDAO getPartecipanteDAO() {
        return partecipanteDAO;
    }
    

    }

