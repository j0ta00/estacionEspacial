package despensa;

import dormitorio.Sala;

import java.util.UUID;

public class Despensa extends Sala {
    //Atributos
    private int capacidad;


    //Constructor
    public Despensa(String id, double metrosCubicos,String idModulo,int capacidad) {
        super(id, metrosCubicos,idModulo);
        this.capacidad = capacidad;
    }

    //Getters y Setters
    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

}
