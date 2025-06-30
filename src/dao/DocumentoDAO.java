package dao;

import model.Documento;
import java.util.List;

public interface DocumentoDAO {
    boolean salvaDocumento(Documento documento);
    List<Documento> getDocumentiPerPartecipante(int idPartecipante);


    Documento findByPartecipante(int idPartecipante);
}
