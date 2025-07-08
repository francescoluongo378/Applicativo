Documentazione Dettagliata - Sistema Gestione Hackathon
Struttura del Progetto
Il sistema è organizzato nei seguenti package:

model: Contiene le classi che rappresentano gli oggetti di dominio
dao: Contiene le interfacce per l'accesso ai dati
postgresdao: Contiene le implementazioni PostgreSQL delle interfacce DAO
controller: Contiene la logica di business
gui: Contiene le interfacce utente
database: Contiene le classi per la gestione della connessione al database
Descrizione Dettagliata delle Classi
Package model
Classe Hackathon
Responsabilità: Rappresenta un evento Hackathon.

Attributi:

id (int): Identificativo univoco dell'Hackathon
titolo (String): Nome dell'evento
sede (String): Luogo dove si svolge l'evento
maxPartecipanti (int): Numero massimo di partecipanti ammessi
maxTeam (int): Numero massimo di team ammessi
dataInizio (LocalDateTime): Data e ora di inizio dell'Hackathon
dataFine (LocalDateTime): Data e ora di fine dell'Hackathon
inizioIscrizioni (LocalDateTime): Data e ora di apertura delle iscrizioni
fineIscrizioni (LocalDateTime): Data e ora di chiusura delle iscrizioni
teams (List): Lista dei team partecipanti
partecipanti (List): Lista dei partecipanti iscritti
giudici (List): Lista dei giudici assegnati
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per tutti gli attributi
aggiungiTeam(Team): Aggiunge un team alla lista
aggiungiPartecipante(Partecipante): Aggiunge un partecipante alla lista
aggiungiGiudice(Giudice): Aggiunge un giudice alla lista
Classe Team
Responsabilità: Rappresenta un gruppo di partecipanti che lavorano insieme a un progetto.

Attributi:

id (int): Identificativo univoco del team
nome (String): Nome del team
descrizione (String): Descrizione del progetto o del team
progresso (int): Percentuale di completamento del progetto
hackathon (Hackathon): Riferimento all'Hackathon a cui partecipa
voti (List): Lista dei voti ricevuti dai giudici
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per tutti gli attributi
getPuntiTotali(): Calcola la somma di tutti i punteggi ricevuti
getPunteggioMedio(): Calcola la media dei punteggi ricevuti
Nota importante: Un team può avere al massimo 3 partecipanti. Questo limite è implementato con controlli nel Controller e nella GUI, non nella classe Team stessa.

Classe Partecipante (estende Utente)
Responsabilità: Rappresenta un utente che partecipa all'Hackathon.

Attributi:

Eredita tutti gli attributi da Utente (id, nome, email, password, ruolo)
teamId (int): ID del team a cui appartiene il partecipante
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per l'attributo teamId
Classe Giudice (estende Utente)
Responsabilità: Rappresenta un utente che valuta i progetti dei team.

Attributi:

Eredita tutti gli attributi da Utente (id, nome, email, password, ruolo)
hackathonId (int): ID dell'Hackathon a cui è assegnato
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per l'attributo hackathonId
Classe Utente
Responsabilità: Classe base per tutti gli utenti del sistema.

Attributi:

id (int): Identificativo univoco dell'utente
nome (String): Nome completo dell'utente
email (String): Indirizzo email dell'utente
password (String): Password per l'accesso
ruolo (String): Ruolo dell'utente (partecipante, giudice, organizzatore)
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per tutti gli attributi
Classe Voto
Responsabilità: Rappresenta la valutazione assegnata da un giudice a un team.

Attributi:

idGiudice (int): ID del giudice che ha assegnato il voto
idTeam (int): ID del team valutato
punteggio (int): Punteggio assegnato (tipicamente da 1 a 10)
Metodi principali:

Costruttori per inizializzare l'oggetto
Getter e setter per tutti gli attributi
Classe Classifica
Responsabilità: Rappresenta la graduatoria dei team in base ai voti ricevuti.

Attributi:

teams (final List): Lista dei team ordinati per punteggio
Metodi principali:

Costruttore che inizializza la lista vuota
aggiungiTeam(Team): Aggiunge un team alla lista
getTeams(): Restituisce la lista dei team
toString(): Genera una rappresentazione testuale della classifica
Nota importante: La lista teams è dichiarata come final, il che significa che il riferimento alla lista non può essere modificato dopo l'inizializzazione. Questo non impedisce di modificare il contenuto della lista (aggiungere/rimuovere team).

Package dao
Interfaccia HackathonDAO
Responsabilità: Definisce i metodi per l'accesso ai dati degli Hackathon.

Metodi:

boolean salva(Hackathon): Salva un nuovo Hackathon
Hackathon trovaPerId(int): Trova un Hackathon per ID
List<Hackathon> findAll(): Trova tutti gli Hackathon
boolean aggiorna(Hackathon): Aggiorna un Hackathon esistente
boolean elimina(int): Elimina un Hackathon per ID
Interfaccia TeamDAO
Responsabilità: Definisce i metodi per l'accesso ai dati dei Team.

Metodi:

Team salva(Team): Salva un nuovo Team
Team trovaPerId(int): Trova un Team per ID
Team trovaTeamPerNome(String): Trova un Team per nome
List<Team> findAll(): Trova tutti i Team
List<Team> findAllByHackathonId(int): Trova tutti i Team di un Hackathon
boolean aggiorna(Team): Aggiorna un Team esistente
boolean elimina(int): Elimina un Team per ID
Interfaccia PartecipanteDAO
Responsabilità: Definisce i metodi per l'accesso ai dati dei Partecipanti.

Metodi:

Partecipante salvaPartecipante(Partecipante): Salva un nuovo Partecipante
Partecipante findById(int): Trova un Partecipante per ID
List<Partecipante> findAll(): Trova tutti i Partecipanti
List<Partecipante> findAllByTeamId(int): Trova tutti i Partecipanti di un Team
boolean aggiorna(Partecipante): Aggiorna un Partecipante esistente
boolean elimina(int): Elimina un Partecipante per ID
Interfaccia GiudiceDAO
Responsabilità: Definisce i metodi per l'accesso ai dati dei Giudici.

Metodi:

Giudice salvaGiudice(Giudice): Salva un nuovo Giudice
Giudice trovaGiudicePerId(int): Trova un Giudice per ID
List<Giudice> findAll(): Trova tutti i Giudici
List<Giudice> findAllByHackathonId(int): Trova tutti i Giudici di un Hackathon
boolean aggiorna(Giudice): Aggiorna un Giudice esistente
boolean elimina(int): Elimina un Giudice per ID
Interfaccia VotoDAO
Responsabilità: Definisce i metodi per l'accesso ai dati dei Voti.

Metodi:

boolean salvaVoto(Voto): Salva un nuovo Voto
List<Voto> findAllByTeamId(int): Trova tutti i Voti di un Team
List<Voto> findAllByGiudiceId(int): Trova tutti i Voti di un Giudice
boolean eliminaVoto(int, int): Elimina un Voto per ID Giudice e ID Team
Interfaccia ClassificaDAO
Responsabilità: Definisce i metodi per l'accesso ai dati della Classifica.

Metodi:

Classifica getClassifica(int): Ottiene la classifica per un Hackathon
Interfaccia UtenteDAO
Responsabilità: Definisce i metodi per l'accesso ai dati degli Utenti.

Metodi:

Utente salvaUtente(Utente): Salva un nuovo Utente
Utente trovaPerId(int): Trova un Utente per ID
Utente trovaPerEmail(String): Trova un Utente per email
List<Utente> findAll(): Trova tutti gli Utenti
boolean aggiorna(Utente): Aggiorna un Utente esistente
boolean elimina(int): Elimina un Utente per ID
Package postgresdao
Questo package contiene le implementazioni PostgreSQL di tutte le interfacce DAO. Ogni classe implementa i metodi definiti nell'interfaccia corrispondente, utilizzando query SQL specifiche per PostgreSQL.

Classe PostgresHackathonDAO
Responsabilità: Implementa HackathonDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in HackathonDAO
Utilizza query SQL per interagire con la tabella hackathon
Classe PostgresTeamDAO
Responsabilità: Implementa TeamDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in TeamDAO
Utilizza query SQL per interagire con la tabella team
Classe PostgresPartecipanteDAO
Responsabilità: Implementa PartecipanteDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in PartecipanteDAO
Utilizza query SQL per interagire con la tabella partecipante
contaPartecipantiInTeam(int): Conta il numero di partecipanti in un team (usato per verificare il limite di 3)
Classe PostgresGiudiceDAO
Responsabilità: Implementa GiudiceDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in GiudiceDAO
Utilizza query SQL per interagire con la tabella giudice
Classe PostgresVotoDAO
Responsabilità: Implementa VotoDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in VotoDAO
Utilizza query SQL per interagire con la tabella voto
Classe PostgresClassificaDAO
Responsabilità: Implementa ClassificaDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in ClassificaDAO
Utilizza query SQL per recuperare i team ordinati per punteggio
Classe PostgresUtenteDAO
Responsabilità: Implementa UtenteDAO per PostgreSQL.

Metodi principali:

Implementa tutti i metodi definiti in UtenteDAO
Utilizza query SQL per interagire con la tabella utente
Package controller
Classe Controller
Responsabilità: Gestisce tutta la logica di business dell'applicazione.

Attributi:

Riferimenti a tutti i DAO necessari
hackathon (Hackathon): Riferimento all'Hackathon corrente
Metodi principali:

Gestione Hackathon:

creaHackathon(String, String, int, int): Crea un nuovo Hackathon
creaHackathon(String, String, int, int, LocalDateTime, LocalDateTime, LocalDateTime, LocalDateTime): Crea un Hackathon con date specifiche
creaHackathonDefault(): Crea un Hackathon con valori predefiniti
caricaHackathon(int): Carica un Hackathon esistente
Gestione Utenti:

registraUtente(String, String, String, String): Registra un nuovo utente
login(String, String): Autentica un utente
Gestione Team:

creaTeam(String, int): Crea un nuovo team e associa un partecipante
caricaTeamsDaDB(): Carica tutti i team dell'Hackathon corrente
getListaTeam(): Restituisce la lista dei team
Gestione Partecipanti:

iscriviPartecipante(int): Iscrive un utente come partecipante all'Hackathon
getPartecipanteDAO(): Restituisce il DAO dei partecipanti (usato per contare i membri di un team)
Gestione Giudici:

assegnaGiudice(int): Assegna un utente come giudice all'Hackathon
caricaGiudiciDaDB(): Carica tutti i giudici dell'Hackathon corrente
Gestione Voti:

assegnaVoto(int, int, int): Assegna un voto da un giudice a un team
Gestione Classifica:

getClassifica(): Ottiene la classifica dell'Hackathon corrente
Package gui
Classe FinestraOrganizzatore
Responsabilità: Interfaccia utente per gli organizzatori.

Funzionalità:

Creazione e configurazione di Hackathon
Visualizzazione dei team e partecipanti
Assegnazione di giudici
Visualizzazione della classifica
Classe FinestraPartecipante
Responsabilità: Interfaccia utente per i partecipanti.

Funzionalità:

Creazione di team
Unione a team esistenti (solo se hanno meno di 3 membri)
Aggiornamento del progresso del team
Visualizzazione della classifica
Classe FinestraGiudice
Responsabilità: Interfaccia utente per i giudici.

Funzionalità:

Visualizzazione dei team da valutare
Assegnazione di voti ai team
Visualizzazione della classifica
Package database
Classe ConnessioneDatabase
Responsabilità: Gestisce la connessione al database PostgreSQL.

Pattern: Singleton (garantisce una sola istanza della connessione)

Metodi principali:

getInstance(): Restituisce l'istanza unica della classe
getConnection(): Restituisce una connessione al database
Caratteristiche Tecniche Importanti
Lista final nella classe Classifica
private final List<Team> teams;
Significato: Il riferimento alla lista non può essere modificato dopo l'inizializzazione nel costruttore.

Vantaggi:

Previene la sostituzione accidentale dell'intera lista
Comunica l'importanza di questo oggetto per la classe
Garantisce che lo stesso oggetto lista venga utilizzato per tutta la vita dell'oggetto Classifica
Nota: Il contenuto della lista può essere modificato (aggiungere/rimuovere team) tramite i metodi della classe.

Limite di 3 Partecipanti per Team
Questo limite è implementato con controlli espliciti nel codice:

// Nel Controller.java
if (numPartecipanti >= 3) {
    System.err.println("Team già pieno (max 3)");
    return false;
}
// Nella FinestraPartecipante.java
int numMembri = ((PostgresPartecipanteDAO)controller.getPartecipanteDAO()).contaPartecipantiInTeam(team.getId());
if (numMembri < 3) {
    teamsDisponibili.add(team);
}
// Messaggio di errore nella GUI
JOptionPane.showMessageDialog(this, "Tutti i team hanno già raggiunto il numero massimo di 3 membri.");
Questo è un requisito di business dell'applicazione, non correlato alla dichiarazione final della lista teams nella classe Classifica.

Schema del Database
Il database PostgreSQL contiene le seguenti tabelle:

utente: Memorizza tutti gli utenti del sistema
hackathon: Memorizza gli eventi Hackathon
team: Memorizza i team partecipanti
partecipante: Memorizza i partecipanti e il loro team
giudice: Memorizza i giudici e il loro Hackathon
voto: Memorizza i voti assegnati dai giudici ai team
Flussi Utente Principali
Flusso Organizzatore
Login come organizzatore
Creazione di un nuovo Hackathon
Configurazione delle date e dei limiti
Assegnazione dei giudici
Monitoraggio delle iscrizioni e dei team
Visualizzazione della classifica finale
Flusso Partecipante
Registrazione/Login come partecipante
Iscrizione a un Hackathon
Creazione di un nuovo team o unione a un team esistente (max 3 membri)
Aggiornamento del progresso del team
Visualizzazione della classifica
Flusso Giudice
Login come giudice
Visualizzazione dei team da valutare
Assegnazione di voti ai team (punteggio da 1 a 10)
Visualizzazione della classifica aggiornata
