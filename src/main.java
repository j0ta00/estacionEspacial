import java.io.*;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class main {


    public static void main(String[] args) {
        Properties p = new Properties();
        InputStream is;
        try {
            is = new FileInputStream("configuracion.properties");
            p.load(is);
            System.out.println(p.getProperty("URL"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
