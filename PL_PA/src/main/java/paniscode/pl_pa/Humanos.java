package paniscode.pl_pa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa a los humanos en la simulación.
 * Implementamos el ciclo de vida completo de un humano, incluyendo:
 * - Movimiento entre zonas del refugio
 * - Recolección de comida en el exterior
 * - Interacción con zombies
 * - Recuperación de heridas
 */
public class Humanos extends Thread {
    // Componentes de control y sincronización
    private final Controlador controlador;
    private final Random random = new Random();
    private final String IdH;
    private final Refugio refugio;
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    // Referencia al exterior para movimientos
    private final Exterior exterior;

    // Estado del humano
    private int comida;
    private boolean muerto;
    private boolean atacado;

    /**
     * Constructor para crear un nuevo humano.
     * @param IdH Identificador único del humano
     * @param refugio Referencia al refugio principal
     * @param exterior Referencia a la zona exterior
     * @param controlador Controlador principal de la simulación
     */
    public Humanos(String IdH, Refugio refugio, Exterior exterior, Controlador controlador) {
        this.IdH = IdH;
        this.refugio = refugio;
        this.exterior = exterior;
        this.comida = 0;
        this.muerto = false;
        this.atacado = false;
        this.controlador = controlador;
    }

    // Métodos de acceso básicos
    public String getIdH() { return IdH; }
    public void Atacado() { this.atacado = true; }
    public void Muerto() { this.muerto = true; }
    public void setComida(int comida) { this.comida = comida; }
    public boolean isMuerto() { return muerto; }
    public boolean isAtacado() { return atacado; }

    /**
     * Método principal que ejecuta el ciclo de vida del humano.
     * Implementamos la lógica completa de movimiento y acciones del humano.
     */
    @Override
    public void run() {
        try {
            while(!this.muerto) {
                controlador.esperarSiPausado();
                
                // Fase 1: Movimiento inicial a zona común
                refugio.ir_zona_común(IdH);
                System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va a zona común");
                
                // Fase 2: Selección de túnel y movimiento al exterior
                int tunel = random.nextInt(1,5); // Selección aleatoria de túnel 1-4
                System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va por el tunel " + tunel);
                refugio.ir_tunel(tunel, this.IdH);
                
                // Fase 3: Actividades en el exterior
                exterior.entrar_zona(tunel, this);
                System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va a la zona " + tunel);
                this.setComida(2); // Recolecta 2 unidades de comida
                exterior.aguantar(this); // Espera en zona peligrosa
                
                // Fase 4: Regreso al refugio (incluso si fue atacado)
                exterior.sacar_zona(tunel, this);
                
                if(!this.muerto) {
                    // Procesamiento de estado después del exterior
                    if(this.atacado) {
                        System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" ha sido atacado pero no asesinado");
                        this.setComida(0); // Pierde la comida si fue atacado
                    }
                    
                    // Regreso por el túnel
                    exterior.ir_tunel(tunel, this.IdH);
                    System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" regresa por el tunel" + tunel);
                    
                    // Fase 5: Actividades en el refugio
                    refugio.depositar_comida(this.comida);
                    System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" ha dejado " + this.comida+" piezas de comida");
     
                    // Descanso y alimentación
                    System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va a descansar");
                    refugio.ir_zona_descanso(IdH);
                      
                    System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va a comer");
                    refugio.ir_comedor(IdH);
                        
                    // Recuperación si fue atacado
                    if(this.atacado) {
                        System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" va a recuperarse de las heridas anteriores");
                        refugio.ir_recuperarse(IdH);
                    }
                }
            }
            // Fin del ciclo de vida
            System.out.println(LocalDateTime.now().format(formato) + "  El humano "+this.IdH+" ha muerto");
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Humanos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}