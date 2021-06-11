package DAO;

import excepciones.DAOException;
import tripulantes.Cientifico;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DAOCientifico extends DAOTripulante{
    //CONSULTAS
    public static final String INSERTARCIENTIFICO="INSERT INTO Cientificos(IdTripulante,NombreUniversidad,AnhiosExperiencia) VALUES (?,?,?)";
    public static final String MODIFICARCIENTIFICO="UPDATE Cientificos SET NombreUniversidad=?,AnhiosExperiencia=? WHERE IdTripulante=?";
    public static final String OBTENERTODOSELEMENTOSCIENTIFICOS="SELECT*FROM Cientificos";
    public static final String OBTENERCIENTIFICO="SELECT*FROM Cientificos WHERE IdTripulante=?";
    //Todos los métodos de esta clase sobrecargan la de su clase padre DAOTripulante,excepto los métodos de obtenr objetos y obtener todos lso objetos pues son métodos
    //que devuelven instacias de Tripulante y al ser una clase abstracta no podía hacer eso,

    /**
     * Cabecera:public boolean  insertarObjeto(tripulantes.Cientifico objeto)
     * Proposito:Método que nos permite insertar un objeto cientifíco en nuestra base de datos
     * Precondición:El objeto científico no debe ser nulo
     * Postcondiciónn:Se trata de un procedimiento que inserta un elemento en la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
   public void insertarObjeto(Cientifico objeto) throws DAOException {
        try {
            super.insertarObjeto(objeto);
            miStatement= preparedStatementCientificos(objeto,INSERTARCIENTIFICO,true);
            miStatement.executeUpdate();
        } catch (SQLException e){
            throw new DAOException();
        }
    }
    /**
     * Cabecera: boolean modificarObjeto(String id)
     * Propsito:Método que nos permite modificar un elemento de nuestra base de datos
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de un procedimiento que modifica el estado de la base de datos
     * @author: jjmza
     * @param objeto
     * @throws DAOException
     */
    public void modificarObjeto(Cientifico objeto) throws DAOException {
        try{
            super.modificarObjeto(objeto);
            miStatement= preparedStatementCientificos(objeto,MODIFICARCIENTIFICO,false);
            miStatement.setString(3,objeto.getId());
            miStatement.executeUpdate();
        }catch(SQLException e){
            throw  new DAOException();
        }
    }


    /**
     * Cabecera:  public PreparedStatement preparedStatementCientificos(PreparedStatement miStatement, tripulantes.Cientifico objeto, String consulta, boolean idNecesario)
     * Proposito:Se trata de un método que nos devulve una consulta preparada llena con los elementos del objeto que le hemos pasado por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondiciónn:Se trata de una función que devulve un objeto PreparedStatement
     * @author: jjmza
     * @param objeto,consulta,idNecesario--booleano que sera true si necesitamos introducir a la consulta el id o false si no
     * @throws SQLException
     */
    public PreparedStatement preparedStatementCientificos(Cientifico objeto, String consulta, boolean idNecesario) throws SQLException {
        miStatement = miConexion.prepareStatement(consulta);
        int valorPreparado1=1,valorPreparado2=2;//si necesitamos el id este va a ocupar la primera posición de la consulta preparada, si no es el caso el primer lugar lo ocuparía el nombre de la universidad
        if (idNecesario) {
            miStatement.setString(1, objeto.getId());
            valorPreparado1++;
            valorPreparado2++;
        }
        miStatement.setString(valorPreparado1, objeto.getNombreUniversidad());
        miStatement.setString(valorPreparado2, String.valueOf(objeto.getAnhiosDeExperiencia()));
        return miStatement;
    }


    /**
     * Cabecera: List obtenerTodosLosObjetos()
     * Precondición:Ninguna
     * Postcondiciónn:Se trata de una función que devuelve un objeto tipo List asociado a su nombre
     * @author: jjmza
     * @return List Cientificos
     * @throws DAOException
     */
    @Override
    public List obtenerTodosLosObjetos() throws DAOException {
        ResultSet rs=null;
        ResultSet rs2=null;
        Cientifico c=null;
        String[] fechaNacimiento;
        List cientificos= new LinkedList<Cientifico>();
        try{
            miStatement=miConexion.prepareStatement(OBTENERTODOSELEMENTOSTRIPULANTES);
            miStatement2=miConexion.prepareStatement(OBTENERTODOSELEMENTOSCIENTIFICOS);
            rs=miStatement.executeQuery();
            rs2=miStatement2.executeQuery();
            while(rs.next() && rs2.next()){
                c = crearCientifico(rs,rs2);
                cientificos.add(c);
            }
        }catch(SQLException e){
            throw new DAOException();
        }
        return cientificos;
    }


    /**
     * Cabecera:tripulantes.Cientifico obtenerObjeto(String id)
     * Precondición:La cadena id no debe ser nula
     * Postcondiciónn:Se trata de una función que devuelve un objeto asociado a su nombre
     * @author: jjmza
     * @param id
     * @return cientifico
     * @throws DAOException
     */
    @Override
    public Cientifico obtenerObjeto(String id) throws DAOException {
        ResultSet rs=null;
        ResultSet rs2=null;
        Cientifico cientifico=null;
        String[] fechaNacimiento;
        try{
            miStatement=miConexion.prepareStatement(OBTENERTRIPULANTE);
            miStatement2=miConexion.prepareStatement(OBTENERCIENTIFICO);
            miStatement.setString(1,id);
            miStatement2.setString(1,id);
            rs=miStatement.executeQuery();
            rs2=miStatement2.executeQuery();
            while(rs.next() && rs2.next()){
                cientifico = crearCientifico(rs,rs2);
            }
        }catch(SQLException e){
            throw new DAOException();
        }
        return cientifico;
    }

    /**
     * Cabecera:public tripulantes.Cientifico crearCientifico(PreparedStatement miStatement,ResultSet rs,ResultSet rs2, PreparedStatement miStatement2)
     * Proposito:Este método crea el objeto cientifíco con los datos que le pasemos por parámetros
     * Precondición:Ninguno de los elementos pasados por parámetros debe ser nulo
     * Postcondición:Se trata de una función que devuelve un objeto cientifico asociado a su nombre
     * @param rs,rs2
     * @return new tripulantes.Cientifico
     * @throws SQLException
     * */
    public Cientifico crearCientifico(ResultSet rs,ResultSet rs2 ) throws SQLException {
        String[] fechaNacimiento=rs.getString(4).split("-");
        return new Cientifico(rs.getString(1), rs.getString(2), rs.getString(3), LocalDate.of(Integer.parseInt(fechaNacimiento[0]), Integer.parseInt(fechaNacimiento[1]), Integer.parseInt(fechaNacimiento[2])),
                    rs.getString(5), rs2.getString(2), Integer.parseInt(rs2.getString(3)));
    }
}

