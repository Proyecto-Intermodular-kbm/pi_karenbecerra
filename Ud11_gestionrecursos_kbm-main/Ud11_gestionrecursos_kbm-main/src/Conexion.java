import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexion a la base de datos MySQL.
 * Solo tiene un metodo que devuelve la conexion lista para usar.
 */
public class Conexion {

    // Datos de conexion - cambia estos valores segun tu MySQL
    private static final String URL      = "jdbc:mysql://localhost:3306/sistema_reservas";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "12345";

    /**
     * Crea y devuelve una conexion a la base de datos.
     * @return objeto Connection listo para usar
     * @throws SQLException si no puede conectarse
     */
    public static Connection getConexion() throws SQLException {
        // DriverManager intenta conectarse con los datos de arriba
        Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        return con;
    }
}

