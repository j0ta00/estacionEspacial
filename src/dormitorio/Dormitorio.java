package dormitorio;

public class Dormitorio extends Sala{
    //Atributos
    private int numeroDeCamasDisponibles;

    public Dormitorio(String id, double metrosCubicos, String idModulo,int numeroDeCamasDisponibles) {
        super(id, metrosCubicos, idModulo);
        this.numeroDeCamasDisponibles=numeroDeCamasDisponibles;
    }

    public int getNumeroDeCamasDisponibles() {
        return numeroDeCamasDisponibles;
    }

    public void setNumeroDeCamasDisponibles(int numeroDeCamasDisponibles) {
        this.numeroDeCamasDisponibles = numeroDeCamasDisponibles;
    }
}
