package paniscode.pl_pa;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Clase que modela el comportamiento de un túnel en el sistema de refugio.
 * Gestiona la entrada/salida de humanos mediante mecanismos de concurrencia avanzados:
 * - Semáforos para control de grupos
 * - Barreras cíclicas para sincronización
 * - Monitores para exclusión mutua
 * - Coordinación con la interfaz gráfica
 */
public class Tunel {

    private final int tunelId;
    private final InterfazP1 interfazP1;
    private final Controlador controlador;

    // Semáforos para formar grupos de 3 humanos
    private final Semaphore crear_grupo_salir_refugio = new Semaphore(3);
    private final Semaphore crear_grupo_entrar_refugio = new Semaphore(3);

    // Barreras para esperar formación de grupos
    private final CyclicBarrier espera_salir_refugio = new CyclicBarrier(3);
    private final CyclicBarrier espera_entrar_refugio = new CyclicBarrier(3);

    // Semáforo para paso individual por el túnel
    private final Semaphore pasar_por_tunel = new Semaphore(1);

    // Monitor para coordinación entrada/salida
    private final Object tunelControl = new Object();
    private int grupo_entrando = 0; // Contador de humanos en proceso de entrada
    private int gente_entrado = 0;   // Contador de humanos que han completado entrada

    // Listas para gestión de grupos en UI
    private final List<String> humanos_grupo_salida = new ArrayList<>();
    private final List<String> humanos_grupo_entrada = new ArrayList<>();

    /**
     * Constructor del túnel.
     * @param tunelId Identificador único del túnel
     * @param interfazP1 Referencia a la interfaz gráfica para actualizaciones
     * @param controlador Controlador principal para gestión de pausas
     */
    public Tunel(int tunelId, InterfazP1 interfazP1, Controlador controlador) {
        this.tunelId = tunelId;
        this.interfazP1 = interfazP1;
        this.controlador = controlador;
    }

    /**
     * Método para que un humano salga del refugio a través del túnel.
     * Implementa:
     * 1. Formación de grupos de 3 humanos
     * 2. Paso secuencial por el túnel
     * 3. Coordinación con humanos que intentan entrar
     * @param IdH Identificador del humano
     */
    public void salir_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        controlador.esperarSiPausado();
        crear_grupo_salir_refugio.acquire(); // Bloquea después de 3 humanos
        
        // Actualización UI
        controlador.esperarSiPausado();
        humanos_grupo_salida.add(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_tuneles_salida(humanos_grupo_salida, this.tunelId);

        espera_salir_refugio.await(); // Sincronización grupo de 3
        sleep(500);
  
        // Sección crítica con monitor
        pasar_por_tunel.acquire();
        synchronized (tunelControl) {
            while (grupo_entrando == 3) { // Espera si hay grupo entrando
                pasar_por_tunel.release();
                tunelControl.wait();
                pasar_por_tunel.acquire();
            }
        }

        // Actualización UI después de pasar
        controlador.esperarSiPausado();
        humanos_grupo_salida.remove(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_tuneles_salida(humanos_grupo_salida, this.tunelId);
        
        // Simulación paso por túnel
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_paso_tunel(IdH, this.tunelId);
        Thread.sleep(1000);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_paso_tunel(null, this.tunelId);
        
        // Liberación recursos
        pasar_por_tunel.release();
        crear_grupo_salir_refugio.release();
    }

    /**
     * Método para que un humano entre al refugio a través del túnel.
     * Implementa:
     * 1. Formación de grupos de entrada
     * 2. Coordinación con salidas
     * 3. Notificación cuando completo el grupo
     * @param IdH Identificador del humano
     */
    public void entrar_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        controlador.esperarSiPausado();
        crear_grupo_entrar_refugio.acquire();
        
        // Gestión grupo entrada
        controlador.esperarSiPausado();
        humanos_grupo_entrada.add(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_tuneles_entrada(humanos_grupo_entrada,this.tunelId);

        espera_entrar_refugio.await(); // Sincronización grupo
        controlador.esperarSiPausado();
        sleep(500);

        // Registro grupo entrante
        synchronized (tunelControl) {
            controlador.esperarSiPausado();
            grupo_entrando++;
            controlador.esperarSiPausado();
        }

        // Paso por túnel
        pasar_por_tunel.acquire();
        controlador.esperarSiPausado();
        humanos_grupo_entrada.remove(IdH);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_tuneles_entrada(humanos_grupo_entrada,this.tunelId);
        
        // Simulación paso
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_paso_tunel(IdH, this.tunelId);
        controlador.esperarSiPausado();
        gente_entrado++;
        Thread.sleep(1000);
        controlador.esperarSiPausado();
        this.interfazP1.mod_text_paso_tunel(null, this.tunelId);

        // Liberación recursos y notificación
        pasar_por_tunel.release();
        synchronized (tunelControl) {
            if (gente_entrado == 3) { // Grupo completo
                grupo_entrando = 0;
                gente_entrado = 0;
                tunelControl.notifyAll(); // Despierta posibles hilos esperando
            }
        }

        crear_grupo_entrar_refugio.release();
    }
    
    /**
     * Método auxiliar para obtener humanos en tránsito.
     * @return Total de humanos en proceso de entrada/salida
     */
    public int getHTunel() { 
        int humanos_pasando = 0;
        if(this.pasar_por_tunel.availablePermits() == 0){
            humanos_pasando +=1;
        }
        return humanos_grupo_salida.size() + humanos_grupo_entrada.size() + humanos_pasando; 
    }
}