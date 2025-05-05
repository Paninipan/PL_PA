package paniscode.pl_pa;

import java.io.File;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

/**
 * Clase principal que inicia la simulación del sistema distribuido.
 * Implementamos la configuración inicial, creación de componentes principales
 * y puesta en marcha del servidor RMI para control remoto.
 */
public class Main {

    // Constante que define el número inicial de humanos en la simulación
    private static final int NUM_HUMANOS = 10000;

    /**
     * Método principal que inicializa todos los componentes del sistema.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Configuración del registro de eventos
            File archivo = new File("apocalipsis.txt");
            PrintStream salidaArchivo = new PrintStream(archivo);
            System.setOut(salidaArchivo); // Redirigimos la salida estándar al archivo

            // Inicialización del controlador principal
            Controlador controlador = new Controlador();

            // Configuración del registro RMI
            LocateRegistry.createRegistry(1099);
            Random random = new Random();

            // Inicialización de la interfaz gráfica
            InterfazP1 interfazP1 = new InterfazP1();
            interfazP1.setVisible(true);

            // Creación de los túneles (puntos de acceso entre refugio y exterior)
            Tunel tunel1 = new Tunel(1, interfazP1, controlador);
            Tunel tunel2 = new Tunel(2, interfazP1, controlador);
            Tunel tunel3 = new Tunel(3, interfazP1, controlador);
            Tunel tunel4 = new Tunel(4, interfazP1, controlador);
            
            // Creación de las áreas principales
            Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4, interfazP1, controlador);
            Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4, interfazP1, controlador);

            // Inicio del paciente cero (primer zombie)
            Zombies pacienteCero = new Zombies("Z0000", exterior, controlador);
            pacienteCero.start();
            
            // Configuración del objeto remoto para control distribuido
            ObjetoRemoto objetoRemoto = new ObjetoRemoto(controlador, refugio, tunel1, tunel2, tunel3, tunel4, exterior);
            Naming.rebind("//localhost/Simulacion", objetoRemoto);

            // Creación e inicio de la población inicial de humanos
            for (int i = 0; i < NUM_HUMANOS; i++) {
                String id = String.format("H%04d", i+1); // Generación de ID con formato H0001, H0002, etc.
                controlador.esperarSiPausado();
                Humanos humano = new Humanos(id, refugio, exterior, controlador);
                humano.start();
                
                // Espera aleatoria entre creación de humanos para escalonar su aparición
                try {
                    Thread.sleep(500 + random.nextInt(1500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error en el servidor RMI: " + e);
            e.printStackTrace();
        }
    }
}