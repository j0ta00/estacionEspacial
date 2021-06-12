package DAO;

import excepciones.DAOException;
import tripulantes.Tripulante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DAOTripulante extends  DAO<Tripulante>{// es abstracta ya que los métodos  de obtener no puede implementarlos ya que la clase tripulante es abstracta y no puede devolver instacias de dicha clase así las hijas implementarán dichos métodos
    //CONSULTAS
    public static final String INSERTARTRIPULANTE="INSERT INTO Tripulantes(IdTripulante,Nombre,Apellidos,FechaNacimiento,IdDormitorio) VALUES (?,?,?,?,?)";
    public static final String MODIFICARTRIPULANTE="UPDATE Tripulantes SET IdTripulante=?,Nombre=?,Apellidos=?,FechaNacimiento=?,IdDormitorio=? WHERE IdTripulante=?";
    public static final String ELIMINARTRIPULANTE="DELETE FROM Tripulantes WHERE IdTripulante=?";
    public static final String OBTENERTODOSELEMENTOSTRIPULANTES="SELECT*FROM Tripulantes";//esta consulta y la de abajo pertenecen a tripulante pero se usarán en los hijos
    public static final String OBTENERTRIPULANTE="SELECT*FROM Tripulantes WHERE IdTripulante=?";
    //Atributos
    protected PreparedStatement miStatement;
    protected PreparedStatement miStatement2;

    /**
     * Cabecera:public boolean  insertarObjeto(Tripulante objeto)
     * Proposito:Método que nos permite insertar un objeto tripulante en nuestra base de datos
     * Precondición:El objeto no debe ser nulo
     * Postcondiciónn:Se trata de un procedimiento que inserta un elemento en la base de datos
     * @author: jjmza
     * @param objeto
     * @throws SQLException
     */
    @Override
    public void insertarObjeto(Tripulante objeto) throws SQLException {//el statement se cierra en las hijas
            miStatement = preparedStatementTripulantes(objeto, INSERTARTRIPULANTE);
            miStatement.executeUpdate();
    }

    /**
     * Cabecera: public PreparedStatement preparedStatementTripulantes(Tripulante objeto,String consulta)
     * Proposito:Se trata de un método que nos devulve una consulta preparada llena con los elementos del objeto que le hemos pasado por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondiciónn:Se trata de una función que devulve un objeto PreparedStatement
     * @author: jjmza
     * @param objeto,consulta
     * @throws SQLException
     */
    public PreparedStatement preparedStatementTripulantes(Tripulante objeto, String consulta) throws SQLException {//el statement se cierra en las hijas
        miStatement=miConexion.prepareStatement(consulta);
        miStatement.setString(1,objeto.getId());
        miStatement.setString(2,objeto.getNombre());
        miStatement.setString(3,objeto.getApellidos());
        miStatement.setString(4,objeto.getFechaNacimiento().toString());
        if(objeto.getIdDormitorio()==null || objeto.getIdDormitorio().length()<36){//pongo 36 ya que la longitud de los uniqueidentifier es esa por lo tanto si es menor, el id no es uniqueidentifier, a su vez pongo el if aquí ya que
            miStatement.setNull(5,0);                               // la instrucción cambia de setString a setNull ambos métodos son de Statement por eso me encargo aquí de esto.
        }else{
            miStatement.setString(5,objeto.getIdDormitorio());
        }
        return miStatement;
    }
    /**
     * Cabecera: boolean modificarObjeto(Tripulante objeto)
     * Propsito:Método que nos permite modificar un elemento de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws SQLException
     */
    @Override
    public void modificarObjeto(Tripulante objeto) throws SQLException {//el statement se cierra en las hijas
            miStatement = preparedStatementTripulantes(objeto, MODIFICARTRIPULANTE);
            miStatement.setString(6,objeto.getId());
            miStatement.executeUpdate();
    }
    /**
     * Cabecera: boolean eliminarObjeto(Tripulante objeto)
     * Propsito:Se trata de un método que elimina un objeto pasado por parámetros de la base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que  elimina un elemento de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    @Override
    public void eliminarObjeto(Tripulante objeto) throws DAOException {
        try{
            miStatement=miConexion.prepareStatement(ELIMINARTRIPULANTE);
            miStatement.setString(1,objeto.getId());
            miStatement.executeUpdate();
        }catch(SQLException e){
            throw new DAOException();
        }finally{
            if(miStatement!=null){
                try {
                    miStatement.close();
                } catch (SQLException e) {
                    throw new DAOException();
                }
            }
        }
    }
}
