import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase principal de la aplicacion.
 * Contiene el menu y todos los metodos CRUD para la tabla RECURSO.
 *
 * Operaciones disponibles:
 *   - Insertar un recurso nuevo
 *   - Listar todos los recursos
 *   - Buscar recurso por nombre
 *   - Modificar un recurso
 *   - Eliminar un recurso
 */
public class RecursoApp {

    // Scanner compartido para leer lo que escribe el usuario
    static Scanner teclado = new Scanner(System.in);

    /**
     * Muestra el menu principal y gestiona la opcion elegida.
     * El bucle while se repite hasta que el usuario elige 0 (salir).
     */
    public static void mostrarMenu() {

        int opcion = -1; // valor inicial para que entre en el bucle

        while (opcion != 0) {

            // Imprimimos el menu
            System.out.println("\n===== GESTION DE RECURSOS =====");
            System.out.println("1. Insertar recurso");
            System.out.println("2. Listar todos los recursos");
            System.out.println("3. Buscar recurso por nombre");
            System.out.println("4. Modificar recurso");
            System.out.println("5. Eliminar recurso");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            opcion = teclado.nextInt();
            teclado.nextLine(); // limpia el salto de linea que queda tras nextInt()

            // Segun la opcion elegida llamamos a un metodo u otro
            switch (opcion) {
                case 1:
                    insertarRecurso();
                    break;
                case 2:
                    listarTodos();
                    break;
                case 3:
                    buscarPorNombre();
                    break;
                case 4:
                    modificarRecurso();
                    break;
                case 5:
                    eliminarRecurso();
                    break;
                case 0:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion no valida, prueba de nuevo.");
            }
        }
    }

    // ---------------------------------------------------------------
    // CREATE - Insertar un recurso nuevo
    // ---------------------------------------------------------------

    /**
     * Pide los datos al usuario e inserta una fila nueva en la tabla RECURSO.
     */
    public static void insertarRecurso() {

        System.out.println("\n--- INSERTAR RECURSO ---");

        // Pedimos los datos por teclado
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();

        System.out.print("Descripcion: ");
        String descripcion = teclado.nextLine();

        System.out.print("Ubicacion: ");
        String ubicacion = teclado.nextLine();

        System.out.print("Capacidad: ");
        int capacidad = teclado.nextInt();
        teclado.nextLine();

        // Preparamos la sentencia SQL con ? para evitar errores
        String sql = "INSERT INTO recurso (nombre, descripcion, ubicacion, capacidad) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps  = con.prepareStatement(sql);

            // Sustituimos cada ? por el valor correspondiente
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, ubicacion);
            ps.setInt(4, capacidad);

            // executeUpdate() ejecuta INSERT, UPDATE y DELETE
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso insertado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());

        } finally {
            // Cerramos siempre, aunque haya habido error
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // READ - Listar todos los recursos
    // ---------------------------------------------------------------

    /**
     * Consulta todos los registros de la tabla RECURSO y los imprime.
     */
    public static void listarTodos() {

        System.out.println("\n--- LISTA DE RECURSOS ---");

        String sql = "SELECT * FROM recurso";

        Connection con = null;
        PreparedStatement ps  = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps  = con.prepareStatement(sql);

            // executeQuery() se usa para SELECT, devuelve un ResultSet
            rs = ps.executeQuery();

            // Recorremos fila a fila el resultado
            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                int    id          = rs.getInt("id");
                String nombre      = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String ubicacion   = rs.getString("ubicacion");
                int    capacidad   = rs.getInt("capacidad");

                System.out.println("ID: " + id
                        + " | Nombre: "      + nombre
                        + " | Desc: "        + descripcion
                        + " | Ubicacion: "   + ubicacion
                        + " | Capacidad: "   + capacidad);
            }

            if (!hayResultados) {
                System.out.println("No hay recursos en la base de datos.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());

        } finally {
            try {
                if (rs  != null) rs.close();
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // READ - Buscar por nombre (segunda consulta requerida)
    // ---------------------------------------------------------------

    /**
     * Busca recursos cuyo nombre contenga el texto introducido.
     * Usa LIKE para busqueda parcial (por ejemplo "Sala" encuentra "Sala A").
     */
    public static void buscarPorNombre() {

        System.out.println("\n--- BUSCAR POR NOMBRE ---");
        System.out.print("Introduce texto a buscar: ");
        String texto = teclado.nextLine();

        // El % significa "cualquier cosa antes o despues"
        String sql = "SELECT * FROM recurso WHERE nombre LIKE ?";

        Connection con = null;
        PreparedStatement ps  = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps  = con.prepareStatement(sql);
            ps.setString(1, "%" + texto + "%"); // ponemos el % aqui, no en el SQL

            rs = ps.executeQuery();

            boolean hayResultados = false;

            while (rs.next()) {
                hayResultados = true;
                System.out.println("ID: "        + rs.getInt("id")
                        + " | Nombre: "              + rs.getString("nombre")
                        + " | Ubicacion: "           + rs.getString("ubicacion")
                        + " | Capacidad: "           + rs.getInt("capacidad"));
            }

            if (!hayResultados) {
                System.out.println("No se encontraron recursos con ese nombre.");
            }

        } catch (SQLException e) {
            System.out.println("Error en la busqueda: " + e.getMessage());

        } finally {
            try {
                if (rs  != null) rs.close();
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // UPDATE - Modificar un recurso
    // ---------------------------------------------------------------

    /**
     * Pide el ID del recurso a modificar y los nuevos datos,
     * luego ejecuta un UPDATE en la base de datos.
     */
    public static void modificarRecurso() {

        System.out.println("\n--- MODIFICAR RECURSO ---");
        System.out.print("ID del recurso a modificar: ");
        int id = teclado.nextInt();
        teclado.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = teclado.nextLine();

        System.out.print("Nueva descripcion: ");
        String descripcion = teclado.nextLine();

        System.out.print("Nueva ubicacion: ");
        String ubicacion = teclado.nextLine();

        System.out.print("Nueva capacidad: ");
        int capacidad = teclado.nextInt();
        teclado.nextLine();

        String sql = "UPDATE recurso SET nombre = ?, descripcion = ?, ubicacion = ?, capacidad = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps  = con.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, ubicacion);
            ps.setInt(4, capacidad);
            ps.setInt(5, id); // el WHERE id = ?

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso modificado correctamente.");
            } else {
                System.out.println("No se encontro un recurso con ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar: " + e.getMessage());

        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // DELETE - Eliminar un recurso
    // ---------------------------------------------------------------

    /**
     * Pide el ID del recurso y lo elimina de la base de datos.
     */
    public static void eliminarRecurso() {

        System.out.println("\n--- ELIMINAR RECURSO ---");
        System.out.print("ID del recurso a eliminar: ");
        int id = teclado.nextInt();
        teclado.nextLine();

        String sql = "DELETE FROM recurso WHERE id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso eliminado correctamente.");
            } else {
                System.out.println("No se encontro un recurso con ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());

        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}