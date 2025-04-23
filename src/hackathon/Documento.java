package hackathon;

public class Documento {
    private int progresso;
    private static final int PROGRESSO_MASSIMO = 100;

    public Documento() {
        this.progresso = 0;
    }

    public void setProgresso(int progresso) {
        if (progresso >= 0 && progresso <= PROGRESSO_MASSIMO) {
            this.progresso = progresso;
        }
    }

    public int getProgresso() {
        return progresso;
    }

    public boolean isCompleto() {
        return progresso == PROGRESSO_MASSIMO;
    }
}
