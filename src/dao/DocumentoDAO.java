package dao;

import model.Documento;
import java.util.List;

public interface DocumentoDAO {
    boolean salvaDocumento(Documento documento);
    List<Documento> findByTeam(int idTeam);
    List<Documento> getDocumentiPerPartecipante(int idPartecipante);

    boolean aggiorna(Documento documento);
    boolean elimina(int id);
}
