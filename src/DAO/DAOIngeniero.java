package DAO;

import excepciones.DAOException;
import tripulantes.Ingeniero;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DAOIngeniero extends DAOTripulante{
    //CONSULTAS
    public static final String INSERTARINGENIERO="INSERT INTO Ingenieros(IdTripulante,Especializacion) VALUES (?,?)";
    public static final String MODIFICARINGENIERO="UPDATE Ingenieros SET Especializacion=? WHERE IdTripulante=?";
    public static final String OBTENERTODOSELEMENTOSINGENIERO="SELECT*FROM Ingenieros";
    public static final String OBTENERINGENIERO="SELECT*FROM Ingenieros WHERE IdTripulante=?";
    //Todos los métodos de esta clase sobrecargan la de su clase padre DAOTripulante,excepto los métodos de obtenr objetos y obtener todos lso objetos pues son métodos
    //que devuelven instacias de Tripulante y al ser una clase abstracta no podía hacer eso,
    /**
     * Cabecera:public boolean  insertarObjeto(Ingeniero objeto)
     * Proposito:Método que nos permite insertar un objeto cientifíco en nuestra base de datos
     * Precondición:El objeto científico no debe ser nulo
     * Postcondiciónn:Se trata de un procedimiento que inserta un elemento en la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    public void insertarObjeto(Ingeniero objeto) throws DAOException {
        try {
            super.insertarObjeto(objeto);
            miStatement= preparedStatementIngenieros(objeto,INSERTARINGENIERO,true);
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
     * Cabecera: boolean modificarObjeto(Ingeniero objeto)
     * Propsito:Método que nos permite modificar un elemento de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    public void modificarObjeto(Ingeniero objeto) throws DAOException {
        try{
            super.modificarObjeto(objeto);
            miStatement= preparedStatementIngenieros(objeto,MODIFICARINGENIERO,false);
            miStatement.setString(3,objeto.getId());
            miStatement.executeUpdate();
        }catch(SQLException e){
            throw  new DAOException();
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
     * Cabecera:  public PreparedStatement preparedStatementIngenieros(Ingeniero objeto, String consulta, boolean idNecesario)
     * Proposito:Se trata de un método que nos devulve una consulta preparada llena con los elementos del objeto que le hemos pasado por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondiciónn:Se trata de una función que devulve un objeto PreparedStatement
     * @author: jjmza
     * @param objeto,consulta,idNecesario--booleano que sera true si necesitamos introducir a la consulta el id o false si no
     * @throws SQLException
     */
    public PreparedStatement preparedStatementIngenieros(Ingeniero objeto, String consulta, boolean idNecesario) throws SQLException {
        miStatement = miConexion.prepareStatement(consulta);
        int valorPreparado1=1;//si necesitamos el id este va a ocupar la primera posición de la consulta preparada, si no es el caso el primer lugar lo ocuparía el nombre de la universidad
        if (idNecesario) {
            miStatement.setString(1, objeto.getId());
            valorPreparado1++;
        }
        miStatement.setString(valorPreparado1, objeto.getEspecializacion());
        return miStatement;
    }

    /**
     * Cabecera: List obtenerTodosLosObjetos()
     * Proposito:Se trata de un método que obtiene una lista de objeto de nuestra base de datos
     * Precondición:Ninguna
     * Postcondiciónn:Se trata de una función que devuelve un objeto tipo List asociado a su nombre
     * @author: jjmza
     * @return List ingenieros
     * @throws DAOException
     */
    @Override
    public List obtenerTodosLosObjetos() throws DAOException {
        ResultSet rs=null;
        ResultSet rs2=null;
        Ingeniero i=null;
        String[] fechaNacimiento;
        List ingenieros= new LinkedList<Ingeniero>();
        try{
            miStatement=miConexion.prepareStatement(OBTENERTODOSELEMENTOSTRIPULANTES);
            miStatement2=miConexion.prepareStatement(OBTENERTODOSELEMENTOSINGENIERO);
            rs=miStatement.executeQuery();
            rs2=miStatement2.executeQuery();
            while(rs.next() && rs2.next()){
                i = crearIngeniero(rs,rs2);
                ingenieros.add(i);
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
        return ingenieros;
    }


    /**
     * Cabecera:public Ingeniero obtenerObjeto(String id)
     * Proposito:Se trata de un método que obtiene un objeto de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de una función que devuelve un objeto asociado a su nombre
     * @author: jjmza
     * @param id
     * @return ingeniero
     * @throws DAOException
     */
    @Override
    public Ingeniero obtenerObjeto(String id) throws DAOException {
        ResultSet rs=null;
        ResultSet rs2=null;
        Ingeniero ingeniero=null;
        String[] fechaNacimiento;
        try{
            miStatement=miConexion.prepareStatement(OBTENERTRIPULANTE);
            miStatement2=miConexion.prepareStatement(OBTENERINGENIERO);
            miStatement.setString(1,id);
            miStatement2.setString(1,id);
            rs=miStatement.executeQuery();
            rs2=miStatement2.executeQuery();
            while(rs.next() && rs2.next()){
                ingeniero = crearIngeniero(rs,rs2);
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
        return ingeniero;
    }

    /**
     * Cabecera: public Ingeniero crearIngeniero(ResultSet rs,ResultSet rs2 )
     * Proposito:Este método crea el objeto ingeniero con los datos que le pasemos por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondición:Se trata de una función que devuelve un objeto ingeniero asociado a su nombre
     * @param rs,rs2
     * @return new Ingeniero
     * @throws SQLException
     * */
    public Ingeniero crearIngeniero(ResultSet rs,ResultSet rs2 ) throws SQLException {
        String[] fechaNacimiento=rs.getString(4).split("-");
        return new Ingeniero(rs.getString(1), rs.getString(2), rs.getString(3), LocalDate.of(Integer.parseInt(fechaNacimiento[0]), Integer.parseInt(fechaNacimiento[1]), Integer.parseInt(fechaNacimiento[2])),
                rs.getString(5), rs2.getString(2));
    }
}
