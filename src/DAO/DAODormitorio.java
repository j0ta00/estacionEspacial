package DAO;

import despensa.Despensa;
import dormitorio.Dormitorio;
import excepciones.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DAODormitorio extends DAO<Dormitorio>{

    //CONSULTAS
    public static final String INSERTARDORMITORIO="INSERT INTO Dormitorios(IdSala,metrosCubicos,NumeroDeCamasDisponibles,IdModulo) VALUES (?,?,?,?)";
    public static final String MODIFICARDORMITORIO="UPDATE Dormitorios SET metrosCubicos=?,NumeroDeCamasDisponibles=?,IdModulo=? WHERE IdSala=?";
    public static final String ELIMINARDORMITORIO="DELETE FROM Dormitorios WHERE IdSala=?";
    public static final String OBTENERTODOSELEMENTOSDORMITORIO="SELECT*FROM Dormitorios";
    public static final String OBTENERDORMITORIO="SELECT*FROM Dormitorios WHERE IdSala=?";
    //Atributo
    private PreparedStatement miStatement;

    /**
     * Cabecera:public void insertarObjeto(Despensa objeto)
     * Proposito:Método que nos permite insertar un objeto despensa en nuestra base de datos
     * Precondición:El objeto provision no debe ser nulo
     * Postcondiciónn:Se trata de un procedimiento que inserta un elemento en la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    @Override
    public void insertarObjeto(Dormitorio objeto) throws DAOException {
        try {
            miStatement= preparedStatementDespensa(objeto,INSERTARDORMITORIO,true);
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
     * Cabecera: public PreparedStatement preparedStatementDespensa(Despensa objeto, String consulta, boolean idNecesario)
     * Proposito:Se trata de un método que nos devulve una consulta preparada llena con los elementos del objeto que le hemos pasado por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondiciónn:Se trata de una función que devulve un objeto PreparedStatement
     * @author: jjmza
     * @param objeto,consulta,idNecesario
     * @throws SQLException
     */
    public PreparedStatement preparedStatementDespensa(Dormitorio objeto, String consulta, boolean idNecesario) throws SQLException {//este método nos ahorra la repetición de código en la inserción y actualización
        miStatement = miConexion.prepareStatement(consulta);
        int valorPreparado1=1,valorPreparado2=2,valorPreparado3=3;//si necesitamos el id este va a ocupar la primera posición de la consulta preparada, si no es el caso el primer lugar lo ocuparía el nombre de la universidad
        if (idNecesario) {
            miStatement.setString(1, objeto.getId());
            valorPreparado1++;
            valorPreparado2++;
            valorPreparado3++;
        }
        miStatement.setString(valorPreparado1,String.valueOf(objeto.getMetrosCubicos()));
        miStatement.setString(valorPreparado2, String.valueOf(objeto.getNumeroDeCamasDisponibles()));
        miStatement.setString(valorPreparado3, objeto.getIdModulo());
        return miStatement;
    } /**
     * Cabecera:  public void modificarObjeto(Despensa objeto)
     * Propsito:Método que nos permite modificar un elemento de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */

    @Override
    public void modificarObjeto(Dormitorio objeto) throws DAOException {
        try {
            miStatement= preparedStatementDespensa(objeto,MODIFICARDORMITORIO,false);
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
     * Cabecera: public void eliminarObjeto(Despensa objeto)
     * Propsito:Método que nos permite eliminar de nuestra base de datos
     * Precondición:El objeto provision no debe ser nulo
     * Postcondición:Se trata de un procedimiento que elimina un elemento de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    @Override
    public void eliminarObjeto(Dormitorio objeto) throws DAOException{
        try {
            miStatement= miConexion.prepareStatement(ELIMINARDORMITORIO);
            miStatement.setString(1, objeto.getId());
            miStatement.executeUpdate();
        } catch (SQLException e){
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
     * @return List despensas
     * @throws DAOException
     */
    @Override
    public List obtenerTodosLosObjetos() throws DAOException {
        ResultSet rs=null;
        Dormitorio d=null;
        List dormitorios= new LinkedList<Dormitorio>();
        try{
            miStatement=miConexion.prepareStatement(OBTENERTODOSELEMENTOSDORMITORIO);
            rs=miStatement.executeQuery();
            while(rs.next()){
                d = crearDormitorio(rs);
                dormitorios.add(d);
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
        return dormitorios;
    }
    /**
     * Cabecera: public Despensa obtenerObjeto(String id)
     * Proposito:Se trata de un método que obtiene un objeto de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de una función que devuelve un objeto asociado a su nombre
     * @author: jjmza
     * @param id
     * @return despensa
     * @throws DAOException
     */
    @Override
    public Dormitorio obtenerObjeto(String id) throws DAOException {
        ResultSet rs=null;
        Dormitorio dormitorio=null;
        try{
            miStatement=miConexion.prepareStatement(OBTENERDORMITORIO);
            rs=miStatement.executeQuery();
            while(rs.next()){
                dormitorio = crearDormitorio(rs);
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
        return dormitorio;
    }

    /**
     * Cabecera: public Despensa crearDespensa(ResultSet rs)
     * Proposito:Este método crea el objeto Provision con los datos que le pasemos por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondición:Se trata de una función que devuelve un objeto provision asociado a su nombre
     * @param rs
     * @return new Despensa
     * @throws SQLException
     * */
    public Dormitorio crearDormitorio(ResultSet rs) throws SQLException {
        return new Dormitorio(rs.getString(1),Double.parseDouble(rs.getString(2)),rs.getString(3),Integer.parseInt(rs.getString(4)));
    }

}
