/**
 * Clase principal. Es el punto de entrada de la aplicacion.
 * Solo llama al menu de RecursoApp para arrancar el programa.
 *
 * Para ejecutar necesitas el driver JDBC de MySQL en el classpath:
 *   javac -cp mysql-connector-j-8.x.x.jar *.java
 *   java  -cp .:mysql-connector-j-8.x.x.jar Main
 *   (en Windows usa ; en vez de : )
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicacion de gestion de recursos...");
        RecursoApp.mostrarMenu();
    }
}
