package dao;

import model.Documento;
import java.util.List;

public interface DocumentoDAO {
    boolean salvaDocumento(Documento documento);
    List<Documento> findByTeam(int idTeam);

    boolean aggiorna(Documento documento);
    boolean elimina(int id);
}
