package dao;

import model.Classifica;

public interface ClassificaDAO {
    Classifica getClassifica();

    Classifica getClassifica(int hackathonId);
}
