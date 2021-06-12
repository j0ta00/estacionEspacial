package DAO;

import excepciones.DAOException;
import provision.Provision;
import modulo.Modulo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DAOModulo extends DAO<Modulo>{
    //CONSULTAS
    public static final String INSERTARMODULO="INSERT INTO Modulos(IdModulo,Proposito,NombrePaisPertenece) VALUES (?,?,?)";
    public static final String MODIFICARMODULO="UPDATE Modulos SET Proposito=?,NombrePaisPertenece=? WHERE IdModulo=?";
    public static final String OBTENERTODOSELEMENTOSMODULOS="SELECT*FROM Modulos";
    public static final String ELIMINARMODUULO="DELETE FROM Modulos WHERE IdModulo=?";
    public static final String OBTENERMODULO="SELECT*FROM Modulos WHERE IdModulo=?";
    //Atributo
    private PreparedStatement miStatement;


    /**
     * Cabecera: public void insertarObjeto(Modulo objeto)
     * Proposito:Método que nos permite insertar un objeto modulo en nuestra base de datos
     * Precondición:El objeto provision no debe ser nulo
     * Postcondiciónn:Se trata de un procedimiento que inserta un elemento en la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    @Override
    public void insertarObjeto(Modulo objeto) throws DAOException {
        try {
            miStatement= preparedStatementModulo(objeto,INSERTARMODULO,true);
            miStatement.executeUpdate();
        }catch (SQLException e){
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
    /**
     * Cabecera: public PreparedStatement preparedStatementProvison(Provision objeto, String consulta, boolean idNecesario) throws SQLException
     * Proposito:Se trata de un método que nos devulve una consulta preparada llena con los elementos del objeto que le hemos pasado por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondiciónn:Se trata de una función que devulve un objeto PreparedStatement
     * @author: jjmza
     * @param objeto,consulta,idNecesario
     * @throws SQLException
     */
    public PreparedStatement preparedStatementModulo(Modulo objeto, String consulta, boolean idNecesario) throws SQLException {//este método nos ahorra la repetición de código en la inserción y actualización
        miStatement = miConexion.prepareStatement(consulta);
        int valorPreparado1=1,valorPreparado2=2;//si necesitamos el id este va a ocupar la primera posición de la consulta preparada, si no es el caso el primer lugar lo ocuparía el nombre de la universidad
        if (idNecesario) {
            miStatement.setString(1, objeto.getId());
            valorPreparado1++;
            valorPreparado2++;
        }
        miStatement.setString(valorPreparado1, objeto.getProposito());
        miStatement.setString(valorPreparado2, String.valueOf(objeto.getNombrePaisPertenece()));
        return miStatement;
    } /**
     * Cabecera: boolean modificarObjeto(Provision objeto)
     * Propsito:Método que nos permite modificar un elemento de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */

    @Override
    public void modificarObjeto(Modulo objeto) throws DAOException {
        try {
            miStatement= preparedStatementModulo(objeto,MODIFICARMODULO,false);
            miStatement.setString(3,objeto.getId());
            miStatement.executeUpdate();
        } catch (SQLException e) {
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
    /**
     * Cabecera:  public void eliminarObjeto(Provision objeto)
     * Propsito:Método que nos permite "eliminar" un elemento marcandolo en nuestra base de datos
     * Precondición:El objeto provision no debe ser nulo
     * Postcondición:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    @Override
    public void eliminarObjeto(Modulo objeto) throws DAOException{//debido a lo que te comente por correo las provisiones no se borraran como tal si no que se hará un borrado por marca poniendo la provision a 0 de esa forma no se podrá consumir ni almacenar
        try {
            miStatement= miConexion.prepareStatement(ELIMINARMODUULO);
            miStatement.setString(1,objeto.getId());
            miStatement.executeUpdate();
        } catch (SQLException e) {
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

    /**
     * Cabecera:public List obtenerTodosLosObjetos()
     * Proposito:Se trata de un método que obtiene una lista de objeto de nuestra base de datos
     * Precondición:Ninguna
     * Postcondiciónn:Se trata de una función que devuelve un objeto tipo List asociado a su nombre
     * @author: jjmza
     * @return List provisiones
     * @throws DAOException
     */
    @Override
    public List<Modulo> obtenerTodosLosObjetos() throws DAOException {
        ResultSet rs=null;
        Modulo m=null;
        List modulos= new LinkedList<Modulo>();
        try{
            miStatement=miConexion.prepareStatement(OBTENERTODOSELEMENTOSMODULOS);
            rs=miStatement.executeQuery();
            while(rs.next()){
                m = crearProvision(rs);
                modulos.add(m);
            }
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
        return modulos;
    }
    /**
     * Cabecera:public Ingeniero obtenerObjeto(String id)
     * Proposito:Se trata de un método que obtiene un objeto de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de una función que devuelve un objeto asociado a su nombre
     * @author: jjmza
     * @param id
     * @return provision
     * @throws DAOException
     */
    @Override
    public Modulo obtenerObjeto(String id) throws DAOException {
        ResultSet rs=null;
        Modulo modulo=null;
        try{
            miStatement=miConexion.prepareStatement(OBTENERMODULO);
            miStatement.setString(1,id);
            rs=miStatement.executeQuery();
            while(rs.next()){
                modulo = crearProvision(rs);
            }
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
        return modulo;
    }

    /**
     * Cabecera: public Provision crearProvision(ResultSet rs)
     * Proposito:Este método crea el objeto Provision con los datos que le pasemos por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondición:Se trata de una función que devuelve un objeto provision asociado a su nombre
     * @param rs
     * @return new Provision
     * @throws SQLException
     * */
    public Modulo crearProvision(ResultSet rs) throws SQLException {
        return new Modulo(rs.getString(1),rs.getString(2),rs.getString(3));
    }


}
