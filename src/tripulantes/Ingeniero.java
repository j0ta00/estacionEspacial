package tripulantes;

import java.time.LocalDate;

public class Ingeniero extends Tripulante{

    //Atributos
    String especializacion;


    //Constructor
    public Ingeniero(String id, String nombre, String apellidos, LocalDate fechaNacimiento, String idDormitorio,String especializacion) {
        super(id, nombre, apellidos, fechaNacimiento, idDormitorio);
        this.especializacion=especializacion;
    }

    //Getters y setters
    public String getEspecializacion() {
        return especializacion;
    }
    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

}
