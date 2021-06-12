import DAO.DAOCientifico;
import DAO.DAOIngeniero;
import DAO.DAOProvision;
import excepciones.DAOException;
import provision.Provision;
import tripulantes.Cientifico;
import tripulantes.Ingeniero;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class main{


    public static void main(String[] args) {
        List<Provision> provisiones = new LinkedList<>();
        Provision h=null;
        Provision c=new Provision(null,"Junajo",3);
        Provision b=new Provision(null,"PELETEIROS",2);
        DAOProvision  d=new DAOProvision();
        try {
            d.insertarObjeto(c);
            h=d.obtenerObjeto(c.getId());
            provisiones=d.obtenerTodosLosObjetos();
            provisiones.forEach(x-> System.out.println(x.getId()));
            System.out.println(h.getId());
        } catch (DAOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
