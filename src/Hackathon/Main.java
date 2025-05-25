package Hackathon;

import java.time.LocalDateTime;
import gui.*;
public class Main {
    public static void main(String[] args) {
        Hackathon hackathon = new Hackathon(
                "Hackathon Universitario",
                "Napoli",
                100, // maxPartecipanti
                LocalDateTime.of(2025, 6, 1, 9, 0), // dataInizio
                LocalDateTime.of(2025, 6, 2, 18, 0), // dataFine
                LocalDateTime.of(2025, 5, 1, 0, 0), // inizioIscrizioni
                LocalDateTime.of(2025, 5, 25, 23, 59), // fineIscrizioni
                20 // maxTeam
        );new Gui(hackathon).setVisible(true);

    }}
