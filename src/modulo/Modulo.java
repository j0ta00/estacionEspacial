package modulo;

import java.util.UUID;

public class Modulo{

    //Atributos
     String id;
     String proposito;
     String nombrePaisPertenece;

    public Modulo(String id, String proposito, String nombrePaisPertenece){
        if(id==null || id.length()<36) {
            this.id = UUID.randomUUID().toString();
        }else {
            this.id = id;
        }
        this.proposito = proposito;
        this.nombrePaisPertenece = nombrePaisPertenece;
    }

    public String getId() {
        return id;
    }

    public String getProposito() {
        return proposito;
    }

    public void setProposito(String proposito) {
        this.proposito = proposito;
    }

    public String getNombrePaisPertenece() {
        return nombrePaisPertenece;
    }

    public void setNombrePaisPertenece(String nombrePaisPertenece) {
        this.nombrePaisPertenece = nombrePaisPertenece;
    }
}
