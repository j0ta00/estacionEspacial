package dormitorio;

import java.util.UUID;

public abstract class Sala {
    //Atributos
    private String id;
    private double metrosCubicos;
    private String idModulo;

    //Constructor
    protected Sala(String id, double metrosCubicos, String idModulo) {
        if(id==null || id.length()<36) {
            this.id = UUID.randomUUID().toString();
        }else {
            this.id = id;
        }
        this.metrosCubicos = metrosCubicos;
        this.idModulo = idModulo;
    }

    //Getters y Setters
    public String getId() {
        return id;
    }

    public double getMetrosCubicos() {
        return metrosCubicos;
    }

    public void setMetrosCubicos(double metrosCubicos) {
        this.metrosCubicos = metrosCubicos;
    }

    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(String idModulo) {
        this.idModulo = idModulo;
    }
}
