package tripulantes;

import tripulantes.Tripulante;

import java.time.LocalDate;

    public class Cientifico extends Tripulante {
        //Atributos
        private String nombreUniversidad;
        private int anhiosDeExperiencia;

        //Constructor
        public Cientifico(String id,String nombre, String apellidos, LocalDate fechaNacimiento, String idDormitorio,String nombreUniversidad,int anhiosDeExperiencia) {
            super(id,nombre, apellidos, fechaNacimiento, idDormitorio);
            this.nombreUniversidad=nombreUniversidad;
            this.anhiosDeExperiencia=anhiosDeExperiencia;
        }
        //Getters y Setters
        public String getNombreUniversidad() {
            return nombreUniversidad;
        }

        public void setNombreUniversidad(String nombreUniversidad) {
            this.nombreUniversidad = nombreUniversidad;
        }

        public int getAnhiosDeExperiencia() {
            return anhiosDeExperiencia;
        }

        public void setAnhiosDeExperiencia(int anhiosDeExperiencia) {
            this.anhiosDeExperiencia = anhiosDeExperiencia;
        }
    }
