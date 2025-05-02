/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author alvaro
 */
class Exterior {

    private Random random = new Random();
    
    private InterfazP1 interfazP1;
    
    private Tunel tunel1;
    private Tunel tunel2;
    private Tunel tunel3;
    private Tunel tunel4;

    private ArrayList<Humanos> zona1_Humanos;
    private ArrayList<Humanos> zona2_Humanos;
    private ArrayList<Humanos> zona3_Humanos;
    private ArrayList<Humanos> zona4_Humanos;
    
    private ArrayList<Zombies> zona1_Zombies;
    private ArrayList<Zombies> zona2_Zombies;
    private ArrayList<Zombies> zona3_Zombies;
    private ArrayList<Zombies> zona4_Zombies;


    private Semaphore SM_zona1 = new Semaphore(1);
    private Semaphore SM_zona2 = new Semaphore(1);
    private Semaphore SM_zona3 = new Semaphore(1);
    private Semaphore SM_zona4 = new Semaphore(1);
    
    private final ReentrantLock lockZona1 = new ReentrantLock();
    private final ReentrantLock lockZona2 = new ReentrantLock();
    private final ReentrantLock lockZona3 = new ReentrantLock();
    private final ReentrantLock lockZona4 = new ReentrantLock();

    public Exterior(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4, InterfazP1 interfazP1) {
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

    }

    void entrar_zona(int tunel, Humanos humano) {
        ReentrantLock lock = getLockZona(tunel);
        lock.lock();
        try {
            getZonaHumanos(tunel).add(humano);
            interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(tunel), tunel);
        } finally {
            lock.unlock();
        }
}


    public void aguantar(Humanos humano) { //esperar a que el humano termine de recolectar o sea atacado
        //System.out.println("El humano "+humano.getIdH()+" esta en la zona peligrosa");
        int tiempo_max = random.nextInt(3, 6) * 1000;
        long tiempo_inicio = System.currentTimeMillis();
        boolean fin_recoleccion = false;
        while (!humano.isAtacado() && !humano.isMuerto() && !fin_recoleccion) {
            if (System.currentTimeMillis() - tiempo_inicio >= tiempo_max) {
                fin_recoleccion = true;
            }

        }
    }

    public void atacar(int zona, Zombies zombie) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Humanos elegido = null;
        boolean va_atacar = false;
        long tiempo = 1000 * random.nextLong(2, 4);
        ReentrantLock lock = switch (zona) {
            case 1 -> lockZona1;
            case 2 -> lockZona2;
            case 3 -> lockZona3;
            case 4 -> lockZona4;
            default -> throw new IllegalArgumentException("Zona inválida: " + zona);
        };
        
        List<List<Zombies>> zonasZ = Arrays.asList(null, zona1_Zombies, zona2_Zombies, zona3_Zombies, zona4_Zombies);
        List<Zombies> zombiesZona = zonasZ.get(zona);
        zombiesZona.add(zombie); // El zombie “entra” en la zona
        interfazP1.mod_text_zona_exterior_zombies(zombiesZona, zona);

        List<List<Humanos>> zonas = Arrays.asList(null, zona1_Humanos, zona2_Humanos, zona3_Humanos, zona4_Humanos); // índice 1-4
        List<Humanos> humanosZona = zonas.get(zona);
        
        
        lock.lock();
        try {
            // Esperar hasta encontrar un humano o que se agote el tiempo
            while (humanosZona.isEmpty() && System.currentTimeMillis() - startTime < tiempo) {
                Thread.sleep(100);
            }

            
            if (!humanosZona.isEmpty()) { //hay algun humano para atacar
                elegido = humanosZona.get(random.nextInt(humanosZona.size()));
                va_atacar = true; //elegido a un humano

                // Eliminarlo de la lista general para evitar duplicaciones
                switch (zona) {
                    case 1 ->zona1_Humanos.remove(elegido);
                    case 2 ->zona2_Humanos.remove(elegido);
                    case 3 ->zona3_Humanos.remove(elegido);
                    case 4 ->zona4_Humanos.remove(elegido);
                }
                interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(zona), zona);
            }
            

        } finally {
            lock.unlock(); // Liberar el semaforo pase lo que pase
            if (va_atacar){ //hay para atacar
                ataque(elegido, zombie); // Ejecutar ataque
                Thread.sleep(1000L * random.nextInt(2, 4)); // Tiempo entre ataques
            }
            // Eliminar zombie de la zona tras el ataque
            zombiesZona.remove(zombie);
            interfazP1.mod_text_zona_exterior_zombies(zombiesZona, zona);

        }
    }

    public void ataque(Humanos elegido, Zombies zombie) throws InterruptedException {
        sleep(100 * random.nextInt(5, 16));
        int probabilidad_exito = random.nextInt(1, 4);
        if (probabilidad_exito == 1) {
            elegido.Muerto();
            System.out.println("El zombie " + zombie.getIdZ() + " ha matado al humano " + elegido.getIdH());
            Zombies asesinado = new Zombies("Z" + elegido.getIdH().substring(1), zombie.getExterior());
            asesinado.start();
        } else {
            System.out.println("El zombie " + zombie.getIdZ() + " ha atacado al humano " + elegido.getIdH());
            elegido.Atacado();
        }
    }

    public void ir_tunel(int tunel, String IdH) throws InterruptedException, BrokenBarrierException {
        switch (tunel) {
            case 1 ->
                tunel1.entrar_refugio(IdH);
            case 2 ->
                tunel2.entrar_refugio(IdH);
            case 3 ->
                tunel3.entrar_refugio(IdH);
            case 4 ->
                tunel4.entrar_refugio(IdH);
        }
    }

    public void sacar_zona(int tunel, Humanos humano) {
        ReentrantLock lock = getLockZona(tunel);
        lock.lock();
        try {
            getZonaHumanos(tunel).remove(humano);
            interfazP1.mod_text_zona_exterior_humanos(getZonaHumanos(tunel), tunel);
        } finally {
            lock.unlock();
        }
}

    private ArrayList<Humanos> getZonaHumanos(int zona) {
        return switch (zona) {
            case 1 -> zona1_Humanos;
            case 2 -> zona2_Humanos;
            case 3 -> zona3_Humanos;
            case 4 -> zona4_Humanos;
            default -> throw new IllegalArgumentException("Zona inválida: " + zona);
        };
    }

private ReentrantLock getLockZona(int zona) {
    return switch (zona) {
        case 1 -> lockZona1;
        case 2 -> lockZona2;
        case 3 -> lockZona3;
        case 4 -> lockZona4;
        default -> throw new IllegalArgumentException("Zona inválida: " + zona);
    };
}


}

