package paniscode.pl_pa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa el comportamiento de los zombies en la simulación.
 * Gestiona el movimiento aleatorio entre zonas y los ataques a humanos,
 * coordinándose con el exterior y mostrando información de seguimiento.
 */
public class Zombies extends Thread {
    private final Controlador controlador;
    private final Random random = new Random();
    private final String IdZ;
    private final Exterior exterior;
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private int contadorMuertes;  // Registro de humanos eliminados por este zombie


    /**
     * Constructor del zombie.
     * @param IdZ Identificador único del zombie
     * @param exterior Referencia al área exterior donde actúa
     * @param controlador Controlador principal para gestión de pausas
     */
    public Zombies(String IdZ, Exterior exterior, Controlador controlador) {
        this.IdZ = IdZ;
        this.exterior = exterior;
        this.contadorMuertes = 0;
        this.controlador = controlador;
    }

    // Métodos setters para modificación de estado
    public void setContadorMuertes(int contadorMuertes) {
        this.contadorMuertes = contadorMuertes;
    }

    public synchronized void incrementarContadorMuertes() {
        this.contadorMuertes++;
    }
    
    /**
     * Método principal de ejecución del hilo zombie.
     * Implementa el comportamiento cíclico de:
     * 1. Movimiento aleatorio entre zonas
     * 2. Ataque coordinado con el exterior
     * 3. Respuesta a controles de pausa
     */
    @Override
    public void run() {
        try {
            while(true){
                controlador.esperarSiPausado();
                int zona = random.nextInt(1,5);
                System.out.println(LocalDateTime.now().format(formato) + 
                                  "  El zombie " + IdZ + " se dirige hacia " + zona);
                exterior.atacar(zona, this);    // Intento de ataque coordinado
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Zombies.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Métodos getters para acceso a estado
    public String getIdZ() {
        return IdZ;
    }

    public int getContadorMuertes() {
        return contadorMuertes;
    }

    public Exterior getExterior() {
        return exterior;
    }
}