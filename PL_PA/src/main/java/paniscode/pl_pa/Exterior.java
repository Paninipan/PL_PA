package paniscode.pl_pa;

import static java.lang.Thread.sleep;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que gestiona las zonas exteriores del sistema, donde humanos y zombies interactúan.
 * Implementamos la lógica de movimiento entre zonas, ataques y recolección de recursos.
 */
class Exterior {
    // Componentes de concurrencia y control
    private final Random random = new Random();
    private final InterfazP1 interfazP1;
    private final Controlador controlador;
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    // Túneles de acceso
    private final Tunel tunel1;
    private final Tunel tunel2;
    private final Tunel tunel3;
    private final Tunel tunel4;

    // Estructuras para gestionar humanos y zombies por zona
    private final ArrayList<Humanos> zona1_Humanos;
    private final ArrayList<Humanos> zona2_Humanos;
    private final ArrayList<Humanos> zona3_Humanos;
    private final ArrayList<Humanos> zona4_Humanos;
    
    private final ArrayList<Zombies> zona1_Zombies;
    private final ArrayList<Zombies> zona2_Zombies;
    private final ArrayList<Zombies> zona3_Zombies;
    private final ArrayList<Zombies> zona4_Zombies;
    
    // Locks para sincronización por zona
    private final ReentrantLock lockZona1 = new ReentrantLock();
    private final ReentrantLock lockZona2 = new ReentrantLock();
    private final ReentrantLock lockZona3 = new ReentrantLock();
    private final ReentrantLock lockZona4 = new ReentrantLock();

    /**
     * Constructor que inicializa las estructuras de datos y componentes necesarios.
     * @param tunel1 Tunel de la zona 1
     * @param tunel2 Tunel de la zona 2
     * @param tunel3 Tunel de la zona 3
     * @param tunel4 Tunel de la zona 4
     * @param interfazP1 Referencia a la interfaz gráfica
     * @param controlador Controlador principal del sistema
     */
    public Exterior(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4, InterfazP1 interfazP1, Controlador controlador) {
        this.tunel1 = tunel1;
        this.tunel2 = tunel2;
        this.tunel3 = tunel3;
        this.tunel4 = tunel4;
        this.zona1_Humanos = new ArrayList<>();
        this.zona2_Humanos = new ArrayList<>();
        this.zona3_Humanos = new ArrayList<>();
        this.zona4_Humanos = new ArrayList<>();
        this.zona1_Zombies = new ArrayList<>();
        this.zona2_Zombies = new ArrayList<>();
        this.zona3_Zombies = new ArrayList<>();
        this.zona4_Zombies = new ArrayList<>();
        this.interfazP1 = interfazP1;
        this.controlador = controlador;
    }

    /**
     * Método para que un humano entre en una zona específica.
     * Implementamos sincronización con ReentrantLock para garantizar exclusión mutua.
     * @param tunel Zona de destino
     * @param humano Humano que entra en la zona
     */
    void entrar_zona(int tunel, Humanos humano) {
        controlador.esperarSiPausado();
        ReentrantLock lock = getLockZona(tunel);
        lock.lock();
        try {
            getZonaHumanos(tunel).add(humano);
            controlador.esperarSiPausado();
            interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(tunel), tunel);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Simula el tiempo que un humano permanece en zona peligrosa.
     * Implementamos una espera activa con comprobación de estado (atacado/muerto).
     * @param humano Humano que espera en la zona
     */
    public void aguantar(Humanos humano) {
        int tiempo_max = random.nextInt(3, 6) * 1000;
        long tiempo_inicio = System.currentTimeMillis();
        boolean fin_recoleccion = false;
        controlador.esperarSiPausado();
        while (!humano.isAtacado() && !humano.isMuerto() && !fin_recoleccion) {
            controlador.esperarSiPausado();
            if (System.currentTimeMillis() - tiempo_inicio >= tiempo_max) {
                fin_recoleccion = true;
            }
        }
    }

    /**
     * Lógica de ataque de zombies a humanos en una zona específica.
     * Implementamos selección aleatoria de víctimas con tiempo limitado de búsqueda.
     * @param zona Zona donde ocurre el ataque
     * @param zombie Zombie que realiza el ataque
     * @throws InterruptedException
     */
    public void atacar(int zona, Zombies zombie) throws InterruptedException {
        controlador.esperarSiPausado();
        long startTime = System.currentTimeMillis();
        Humanos elegido = null;
        boolean va_atacar = false;
        long tiempo = 1000 * random.nextLong(2, 4);
        controlador.esperarSiPausado();
        
        // Selección del lock correspondiente a la zona
        ReentrantLock lock = switch (zona) {
            case 1 -> lockZona1;
            case 2 -> lockZona2;
            case 3 -> lockZona3;
            case 4 -> lockZona4;
            default -> throw new IllegalArgumentException("Zona inválida: " + zona);
        };
        
        // Añadir zombie a la zona correspondiente
        List<List<Zombies>> zonasZ = Arrays.asList(null, zona1_Zombies, zona2_Zombies, zona3_Zombies, zona4_Zombies);
        List<Zombies> zombiesZona = zonasZ.get(zona);
        controlador.esperarSiPausado();
        zombiesZona.add(zombie);
        controlador.esperarSiPausado();
        interfazP1.mod_text_zona_exterior_zombies(zombiesZona, zona);
        
        // Obtener humanos en la zona
        List<List<Humanos>> zonas = Arrays.asList(null, zona1_Humanos, zona2_Humanos, zona3_Humanos, zona4_Humanos);
        List<Humanos> humanosZona = zonas.get(zona);
        
        lock.lock();
        try {
            // Esperar hasta encontrar humano o agotar tiempo
            while (humanosZona.isEmpty() && System.currentTimeMillis() - startTime < tiempo) {
                controlador.esperarSiPausado();
                Thread.sleep(100);
            }

            if (!humanosZona.isEmpty()) {
                elegido = humanosZona.get(random.nextInt(humanosZona.size()));
                va_atacar = true;
                
                // Eliminar humano de la zona
                switch (zona) {
                    case 1 -> zona1_Humanos.remove(elegido);
                    case 2 -> zona2_Humanos.remove(elegido);
                    case 3 -> zona3_Humanos.remove(elegido);
                    case 4 -> zona4_Humanos.remove(elegido);
                }
                controlador.esperarSiPausado();
                interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(zona), zona);
            }
        } finally {
            if (va_atacar) {
                controlador.esperarSiPausado();
                ataque(elegido, zombie);
                controlador.esperarSiPausado();
                Thread.sleep(1000L * random.nextInt(2, 4));
            }
            controlador.esperarSiPausado();
            zombiesZona.remove(zombie);
            controlador.esperarSiPausado();
            interfazP1.mod_text_zona_exterior_zombies(zombiesZona, zona);
            lock.unlock();
        }
    }

    /**
     * Simula el resultado de un ataque zombie-humano.
     * Implementamos probabilidad de éxito (33%) y conversión a zombie si es exitoso.
     * @param elegido Humano atacado
     * @param zombie Zombie atacante
     * @throws InterruptedException
     */
    public void ataque(Humanos elegido, Zombies zombie) throws InterruptedException {
        controlador.esperarSiPausado();
        sleep(100 * random.nextInt(5, 16));
        int probabilidad_exito = random.nextInt(1, 4);
        if (probabilidad_exito == 1) {
            controlador.esperarSiPausado();
            zombie.incrementarContadorMuertes();
            controlador.esperarSiPausado();
            elegido.Muerto();
            System.out.println(LocalDateTime.now().format(formato) + "  El zombie " + zombie.getIdZ() + " ha matado al humano " + elegido.getIdH());
            // Crear nuevo zombie a partir del humano muerto
            controlador.esperarSiPausado();
            Zombies asesinado = new Zombies("Z" + elegido.getIdH().substring(1), zombie.getExterior(), controlador);
            asesinado.start();
        } else {
            System.out.println(LocalDateTime.now().format(formato) + "  El zombie " + zombie.getIdZ() + " ha atacado al humano " + elegido.getIdH());
            elegido.Atacado();
        }
    }

    /**
     * Método para que un humano se dirija a un túnel específico.
     * @param tunel Tunel de destino
     * @param IdH Identificador del humano
     * @throws InterruptedException
     * @throws BrokenBarrierException
     */
    public void ir_tunel(int tunel, String IdH) throws InterruptedException, BrokenBarrierException {
        controlador.esperarSiPausado();
        switch (tunel) {
            case 1 -> tunel1.entrar_refugio(IdH);
            case 2 -> tunel2.entrar_refugio(IdH);
            case 3 -> tunel3.entrar_refugio(IdH);
            case 4 -> tunel4.entrar_refugio(IdH);
        }
    }

    /**
     * Método para sacar un humano de una zona específica.
     * Implementamos sincronización con ReentrantLock para garantizar consistencia.
     * @param tunel Zona de origen
     * @param humano Humano a sacar
     */
    public void sacar_zona(int tunel, Humanos humano) {
        controlador.esperarSiPausado();
        ReentrantLock lock = getLockZona(tunel);
        lock.lock();
        try {
            controlador.esperarSiPausado();
            getZonaHumanos(tunel).remove(humano);
            controlador.esperarSiPausado();
            interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(tunel), tunel);
        } finally {
            lock.unlock();
        }
    }

    // Métodos auxiliares para obtener estructuras por zona
    
    private ArrayList<Humanos> getZonaHumanos(int zona) {
        controlador.esperarSiPausado();
        return switch (zona) {
            case 1 -> zona1_Humanos;
            case 2 -> zona2_Humanos;
            case 3 -> zona3_Humanos;
            case 4 -> zona4_Humanos;
            default -> throw new IllegalArgumentException("Zona inválida: " + zona);
        };
    }

    private ReentrantLock getLockZona(int zona) {
        controlador.esperarSiPausado();
        return switch (zona) {
            case 1 -> lockZona1;
            case 2 -> lockZona2;
            case 3 -> lockZona3;
            case 4 -> lockZona4;
            default -> throw new IllegalArgumentException("Zona inválida: " + zona);
        };
    }

    // Métodos de consulta para la interfaz
    
    public int getHZona1() { return zona1_Humanos.size(); }
    public int getHZona2() { return zona2_Humanos.size(); }
    public int getHZona3() { return zona3_Humanos.size(); }
    public int getHZona4() { return zona4_Humanos.size(); }

    public int getZZona1() { return zona1_Zombies.size(); }
    public int getZZona2() { return zona2_Zombies.size(); }
    public int getZZona3() { return zona3_Zombies.size(); }
    public int getZZona4() { return zona4_Zombies.size(); }
    
    // Método que hace un listado de los 3 Zombies con más muertes, cada vez que se llame.
    public String rankingZombies() {
        List<Zombies> todosLosZombies = new ArrayList<>();
        todosLosZombies.addAll(zona1_Zombies);
        todosLosZombies.addAll(zona2_Zombies);
        todosLosZombies.addAll(zona3_Zombies);
        todosLosZombies.addAll(zona4_Zombies);

        List<Zombies> zombiesOrdenados = new ArrayList<>(todosLosZombies);
        zombiesOrdenados.sort((z1, z2) -> Integer.compare(z2.getContadorMuertes(), z1.getContadorMuertes()));

        StringBuilder sb = new StringBuilder();
        int cantidad = Math.min(3, zombiesOrdenados.size());

        for (int i = 0; i < cantidad; i++) {
            Zombies z = zombiesOrdenados.get(i);
            sb.append(z.getIdZ()).append(" - ").append(z.getContadorMuertes()).append(" muertes").append("\n");
        }
        return sb.toString().trim();
    }
}