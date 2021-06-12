package tripulantes;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Tripulante{
    //Atributos
    private String id;
    private String nombre;
    private String apellidos;
    private LocalDate FechaNacimiento;
    private String idDormitorio;

    //Constructor
    protected Tripulante(String id,String nombre, String apellidos, LocalDate fechaNacimiento,String  idDormitorio) {
        if(id==null || id.length()<36) {
            this.id = UUID.randomUUID().toString();
        }else {
            this.id = id;
        }
        this.nombre = nombre;
        this.apellidos = apellidos;
        FechaNacimiento = fechaNacimiento;
        this.idDormitorio = idDormitorio;
    }

    //Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getIdDormitorio() {
        return idDormitorio;
    }

    public void setIdDormitorio(String idDormitorio) {
        this.idDormitorio = idDormitorio;
    }
}
