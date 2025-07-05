package dao;

import model.Voto;

public interface VotoDAO {
    boolean salvaVoto(Voto voto);
    boolean salvaVoto(int idGiudice, int idTeam, int punteggio);
    boolean aggiorna(Voto voto);
    boolean elimina(int id);

}

