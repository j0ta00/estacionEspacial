import javax.crypto.Cipher;
import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class main{


    public static void main(String[] args) {
        List cientificos = new LinkedList<Cientifico>();
        Cientifico h=null;
        Cientifico c=new Cientifico(null,"Junajo","sinsaho", LocalDate.of(2000,07,06),"NULL","NULLAZO",4);
        Cientifico b=new Cientifico(null,"PELETEIROS","sinsaho", LocalDate.of(2000,07,06),"NULL","NULLAZO",4);
        DAOCientifico d=new DAOCientifico();
        try {
            d.insertarObjeto(c);
            cientificos=d.obtenerTodosLosObjetos();
            h=d.obtenerObjeto(c.getId());
            cientificos.forEach(x-> System.out.println(x.toString()));
            System.out.println(h.toString());
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

}
