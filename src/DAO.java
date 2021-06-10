

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public abstract class DAO<T>{
    //PATH
    public final String PROPERTIESFILEPATH= "configuracion.properties";
    //Propiedades de la clase DAO
    protected Properties configuracion;
    private InputStream is;


    //Método para iniciar/crear la conexión con la base de datos
    public Connection crearConexion(){
        configuracion =new Properties();
        Connection miConexion=null;
            try {
                is=new FileInputStream(PROPERTIESFILEPATH);
                configuracion.load(is);
                miConexion = getConnection(configuracion.getProperty("URL"), configuracion.getProperty("USUARIO"), configuracion.getProperty("CLAVE"));
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
            return miConexion;
    }

    public abstract void insertarObjeto(T objeto) throws DAOException;
    public abstract void modificarObjeto(T objeto) throws DAOException;
    public abstract void eliminarObjeto(T objeto) throws DAOException;
    public abstract List<T> obtenerTodosLosObjetos() throws DAOException;
    public abstract T obtenerObjeto(String id) throws DAOException;
}
