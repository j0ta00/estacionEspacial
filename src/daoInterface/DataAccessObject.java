package daoInterface;

import java.util.List;

public interface DataAccessObject<T>{//He decidido hacer interfaces debido a que la mayoría de operaciones suelen ser comunes además hacer interfaces nos favorece la portabilidad es decir si tuvieramos que cambiar de sql server a mysql u otro tipo de lenguaje
    boolean insertar(T objeto);
    boolean eliminar(T objeto);
    boolean modificar(T objeto);
    List<T> obtenerTodosLosObjetos();
    T obtenerUnObjeto(String Id);
}
