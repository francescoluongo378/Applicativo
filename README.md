# 📊 Sistema di Gestione Hackathon

Un’applicazione Java desktop per la gestione completa di un Hackathon, sviluppata secondo l’**architettura BCE (Boundary-Control-Entity) + DAO**, con interfaccia grafica Swing e database PostgreSQL.

## 🎯 Obiettivo del Progetto

Permettere a un Organizzatore di gestire un evento Hackathon: dalla creazione, all’iscrizione dei partecipanti, assegnazione dei giudici, gestione dei team, valutazione e pubblicazione della classifica.

---

## 📐 Architettura

Il progetto è organizzato nei seguenti package:

- `model`: classi entità come `Hackathon`, `Utente`, `Team`, `Voto` ecc.
- `dao`: interfacce DAO per l’accesso ai dati
- `postgresdao`: implementazioni DAO per PostgreSQL
- `controller`: logica di coordinamento
- `gui`: interfaccia utente realizzata con Java Swing
- `database`: gestione della connessione PostgreSQL

Architettura **BCE + DAO**:

GUI → Controller → Model/DAO → Database PostgreSQL

---

## 🧩 Funzionalità

### ✅ Login e Registrazione
- Il **utente** registrato registrandosi è considerato **Organizzatore**
  **Partecipanti** o **Giudici**

### ✅ Organizzatore
- Crea l’Hackathon (sede, titolo, descrizione, numero massimo partecipanti, date, tema)
- Aggiunge giudici
- Aggiunge partecipanti
- Visualizza i team
- Visualizza classifica finale

### ✅ Partecipante
- Si registra e può unirsi a un team esistente (massimo 3 membri per team)
- Può aggiornare il **progresso (%)** del proprio team
- Non può modificare team già pieni

### ✅ Giudice
- Può assegnare **voti numerici** (1-10) ai team partecipanti
- Ogni giudice può votare ogni team una sola volta

### ✅ Valutazione & Classifica
- La classifica si basa sulla **media dei voti** assegnati dai giudici
- Mostrata in ordine decrescente (dal team con media più alta al più bassa)

---

## 🧠 Classi Principali

- `Utente`: classe base, estesa da `Organizzatore`, `Partecipante`, `Giudice`
- `Hackathon`: contiene info generali sull’evento e liste collegate (partecipanti, team, giudici)
- `Team`: gruppo con massimo 3 partecipanti e un progresso percentuale
- `Voto`: rappresenta il punteggio assegnato da un giudice a un team
- `Classifica`: calcola e ordina i team in base alla media voti

---

## 💽 Database

Database PostgreSQL, gestito da `ConnessioneDatabase.java`.

### Tabelle principali:
- `utente(id, nome, email, password, ruolo)`
- `hackathon(id, titolo, sede, tema, data_inizio, data_fine, max_partecipanti)`
- `team(id, nome, descrizione, progresso, id_hackathon)`
- `partecipante(id_utente, id_team)`
- `giudice(id_utente, id_hackathon)`
- `voto(id_giudice, id_team, punteggio)`
