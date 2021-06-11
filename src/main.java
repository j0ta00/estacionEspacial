import DAO.DAOCientifico;
import DAO.DAOIngeniero;
import excepciones.DAOException;
import tripulantes.Cientifico;
import tripulantes.Ingeniero;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class main{


    public static void main(String[] args) {
        List<Ingeniero> ingenieros = new LinkedList<>();
        Ingeniero h=null;
        Ingeniero c=new Ingeniero(null,"Junajo","sinsaho", LocalDate.of(2000,07,06),"NULL","NULLAZO");
        Ingeniero b=new Ingeniero(null,"PELETEIROS","sinsaho", LocalDate.of(2000,07,06),"NULL","NULLAZO");
        DAOIngeniero d=new DAOIngeniero();
        try {
            d.insertarObjeto(c);
            d.eliminarObjeto(c);
            h=d.obtenerObjeto(c.getId());
            ingenieros=d.obtenerTodosLosObjetos();
            ingenieros.forEach(x-> System.out.println(x.getId()));
            System.out.println(h.getId());
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

}
