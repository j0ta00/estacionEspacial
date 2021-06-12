package provision;

import java.util.UUID;

public class Provision{
    //Atributos
    private String id;//podría haber usado el tipo UUID pero como no he trabajado demasiado con este dato he preferido usar String y al asignar el UUID simplemente le hago el .toString y es exactamente igual
    private String nombre;

    //Constructor
    public Provision(String id, String nombre, int cantidad) {
        if(id==null || id.length()<36) {
            this.id = UUID.randomUUID().toString();
        }else {
            this.id = id;
        }
        this.nombre = nombre;
        this.cantidad = cantidad;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
