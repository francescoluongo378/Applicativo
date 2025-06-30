package dao;

import model.Voto;
import java.util.List;

public interface VotoDAO {
    boolean salvaVoto(Voto voto);
    boolean salvaVoto(int idGiudice, int idTeam, int punteggio);
    List<Voto> listaVoti();
    List<Voto> listaVotiPerTeam(int idTeam);
}


