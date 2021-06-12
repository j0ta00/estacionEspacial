package DAO;
import excepciones.DAOException;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public abstract class DAO<T>{
    //PATH
    public static final String PROPERTIESFILEPATH= "configuracion.properties";
    //Propiedades de la clase
    protected Connection miConexion=crearConexion();



    //Método para iniciar/crear la conexión con la base de datos
    public Connection crearConexion(){
        InputStream is=null;
        Properties configuracion =new Properties();
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

    public void cerrarConexion() throws DAOException {
        try {
            miConexion.close();
        } catch (SQLException throwables) {
            throw new DAOException();
        }
    }


    public abstract void insertarObjeto(T objeto) throws Throwable;//pongo que lanza throwable porque es una excepción más general pues dentro de cada clase que sobreescirba dicho método lanzará una excepción diferente
    public  abstract void modificarObjeto(T objeto) throws Throwable;
    public  abstract void eliminarObjeto(T objeto) throws Throwable;
    public  abstract List<T> obtenerTodosLosObjetos() throws Throwable;
    public  abstract T obtenerObjeto(String id) throws Throwable;
}
