package dao;

import model.Classifica;

public interface ClassificaDAO {
    Classifica getClassifica(int hackathonId);
    Classifica getClassifica();
}
