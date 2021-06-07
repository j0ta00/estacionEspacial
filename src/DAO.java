

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public abstract class DAO{
    //PATH
    public final String PROPERTIESFILEPATH= "configuracion.properties";
    //Propiedades de la clase DAO
    protected Properties configuracion;
    InputStream is;


    //Método para iniciar/crear la conexión con la base de datos
    public Statement crearConexion(){
        configuracion =new Properties();
        Connection miConexion=null;
        Statement miStatement=null;
            try {
                is =new FileInputStream(PROPERTIESFILEPATH);
                configuracion.load(is);
                miConexion = getConnection(configuracion.getProperty("URL"), configuracion.getProperty("USUARIO"), configuracion.getProperty("CLAVE"));
                miStatement = miConexion.createStatement();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(SQLException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return miStatement;
    }
}
