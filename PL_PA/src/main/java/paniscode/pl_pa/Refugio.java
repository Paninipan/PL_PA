package paniscode.pl_pa;

/**
 * Clase que gestiona el refugio en la simulación.
 * Implementamos las zonas comunes, gestión de recursos (comida) y
 * sincronización de humanos entre diferentes áreas del refugio.
 */
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Refugio {
    // Componentes para generación de valores aleatorios y control
    private final Random random = new Random();
    private final InterfazP1 interfazP1;
    private final Controlador controlador;
    
    // Túneles de conexión con el exterior
    private final Tunel tunel1;
    private final Tunel tunel2;
    private final Tunel tunel3;
    private final Tunel tunel4;
    
    // Mecanismos de sincronización para la comida
    private final Lock reservas_comida = new ReentrantLock();
    private final Condition reservas_vacias = reservas_comida.newCondition();
    private int cantidad_comida;
    
    // Estructuras para gestionar humanos en diferentes zonas
    private final List<String> humanos_zona_comun = new ArrayList<>();
    private final List<String> humanos_zona_descanso = new ArrayList<>();
    private final List<String> humanos_comedor = new ArrayList<>();

    /**
     * Constructor que inicializa el refugio con sus componentes principales.
     * @param tunel1 Tunel 1 de acceso
     * @param tunel2 Tunel 2 de acceso
     * @param tunel3 Tunel 3 de acceso
     * @param tunel4 Tunel 4 de acceso
     * @param interfazP1 Referencia a la interfaz gráfica
     * @param controlador Controlador principal de la simulación
     */
    public Refugio(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4, InterfazP1 interfazP1, Controlador controlador) {
        this.tunel1 = tunel1;
        this.tunel2 = tunel2;
        this.tunel3 = tunel3;
        this.tunel4 = tunel4;
        this.cantidad_comida = 0;
        this.interfazP1 = interfazP1;
        this.controlador = controlador;
    }

    /**
     * Método para que un humano entre en la zona común.
     * Implementamos una estancia temporal con actualización de la interfaz.
     * @param IdH Identificador del humano
     * @throws InterruptedException
     */
    public void ir_zona_común(String IdH) throws InterruptedException {
        controlador.esperarSiPausado();
        this.humanos_zona_comun.add(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_zonaComun(humanos_zona_comun);
        controlador.esperarSiPausado();
        sleep(1000 * random.nextInt(1,3)); // Espera aleatoria en la zona
        controlador.esperarSiPausado();
        this.humanos_zona_comun.remove(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_zonaComun(humanos_zona_comun);
    }

    /**
     * Método para dirigirse a un túnel específico para salir al exterior.
     * @param tunel Número de túnel (1-4)
     * @param IdH Identificador del humano
     * @throws InterruptedException
     * @throws BrokenBarrierException
     */
    public void ir_tunel(int tunel, String IdH) throws InterruptedException, BrokenBarrierException {
        controlador.esperarSiPausado();
        switch(tunel) {
            case 1 -> tunel1.salir_refugio(IdH);
            case 2 -> tunel2.salir_refugio(IdH);
            case 3 -> tunel3.salir_refugio(IdH);
            case 4 -> tunel4.salir_refugio(IdH);
        }
    }

    /**
     * Método para depositar comida en el refugio.
     * Implementamos sincronización con Lock para evitar condiciones de carrera.
     * @param comida Cantidad de comida a depositar
     * @throws InterruptedException
     */
    public void depositar_comida(int comida) throws InterruptedException {
        controlador.esperarSiPausado();
        try {
            reservas_comida.lock();
            if(comida > 0) {
                controlador.esperarSiPausado();
                cantidad_comida += comida;
                controlador.esperarSiPausado();
                sleep(250); // Simulación del tiempo de descarga
                controlador.esperarSiPausado();
                this.interfazP1.mod_text_comida(this.cantidad_comida);
                reservas_vacias.signal(); // Notificar a posibles hilos esperando
            }
        } finally {
            reservas_comida.unlock();
        }
    }

    /**
     * Método para que un humano descanse en la zona designada.
     * @param IdH Identificador del humano
     * @throws InterruptedException
     */
    public void ir_zona_descanso(String IdH) throws InterruptedException {
        controlador.esperarSiPausado();
        this.humanos_zona_descanso.add(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
        controlador.esperarSiPausado();
        sleep(1000 * random.nextInt(2,5)); // Tiempo aleatorio de descanso
        controlador.esperarSiPausado();
        this.humanos_zona_descanso.remove(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
    }

    /**
     * Método para que un humano coma en el comedor.
     * Implementamos sincronización con await/signal para gestionar la comida disponible.
     * @param IdH Identificador del humano
     * @throws InterruptedException
     */
    public void ir_comedor(String IdH) throws InterruptedException {
        controlador.esperarSiPausado();
        try {
            controlador.esperarSiPausado();
            this.humanos_comedor.add(IdH);
            controlador.esperarSiPausado();
            this.interfazP1.mod_text_comedor(humanos_comedor);
            
            reservas_comida.lock();
            if(cantidad_comida == 0) {
                reservas_vacias.await(); // Esperar si no hay comida            
            }
            controlador.esperarSiPausado();
            cantidad_comida--;
            controlador.esperarSiPausado();
            this.interfazP1.mod_text_comida(cantidad_comida);
        } finally {
            reservas_comida.unlock();
            controlador.esperarSiPausado();
            sleep(1000 * random.nextInt(3,6)); // Tiempo aleatorio comiendo
            controlador.esperarSiPausado();
            this.humanos_comedor.remove(IdH);
            controlador.esperarSiPausado();
            this.interfazP1.mod_text_comedor(humanos_comedor);
        }
    }

    /**
     * Método para que un humano se recupere de un ataque.
     * @param IdH Identificador del humano
     * @throws InterruptedException
     */
    public void ir_recuperarse(String IdH) throws InterruptedException {
        controlador.esperarSiPausado();
        this.humanos_zona_descanso.add(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
        controlador.esperarSiPausado();
        sleep(1000 * random.nextInt(3,6)); // Tiempo de recuperación
        controlador.esperarSiPausado();
        this.humanos_zona_descanso.remove(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
    }
    
    /**
     * Método para obtener el número total de humanos en el refugio.
     * @return Suma de humanos en todas las zonas del refugio
     */
    public int getHRefugio() { 
        return humanos_zona_comun.size() + humanos_zona_descanso.size() + humanos_comedor.size(); 
    }
}