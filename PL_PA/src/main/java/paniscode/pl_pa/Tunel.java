/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunel {

    private InterfazP1 interfazP1;

    private Semaphore crear_grupo_salir_refugio = new Semaphore(3); //tres humanos para salir
    private Semaphore crear_grupo_entrar_refugio = new Semaphore(3); //tres humanos para entrar

    private CyclicBarrier espera_salir_refugio = new CyclicBarrier(3);
    private CyclicBarrier espera_entrar_refugio = new CyclicBarrier(3);

    private Semaphore pasar_por_tunel = new Semaphore(1); //1 a la vez

    private final Object tunelControl = new Object(); //"monitor para esperar si hay gente intentando entrar" 
    private int grupo_entrando = 0; //gente intentando entrar
    private int gente_entrado = 0; //gente intentando entrar

    public Tunel(InterfazP1 interfazP1) {
        this.interfazP1 = interfazP1;
    }
    
    

    public void salir_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        crear_grupo_salir_refugio.acquire(); //si hay 3 se bloquea
        //System.out.println("Humano " + IdH + " entra al grupo para poder salir");

        espera_salir_refugio.await(); // esperan a formar un grupo de 3
        //la barrera se reinicia esperando a los tres siguientes hilos para salir del refugio

        synchronized (tunelControl) { //accedemos al monitor ordenadamente
            while (grupo_entrando == 3) { //si hay grupo par entrar esperamos
                //System.out.println("Humano " + IdH + " espera a que el grupo de entrada cruce");
                tunelControl.wait(); //esperamos
            }
        }

        pasar_por_tunel.acquire(); //vamos a pasar de 1 en 1
        //System.out.println("Humano " + IdH + " está saliendo del refugio");
        Thread.sleep(1000); // simula paso por el túnel
        pasar_por_tunel.release(); //cuando sale pasa el siguiente

        crear_grupo_salir_refugio.release(); //que pase a formarse el siguiente grupo

    }

    public void entrar_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        crear_grupo_entrar_refugio.acquire(); //tres humanos por grupo
        //System.out.println("Humano " + IdH + " entra al grupo para entrar");

        espera_entrar_refugio.await(); // esperan a formar grupo de entrada
        //la barrera se reinicia esperando a los tres siguientes hilos para entrar al refugio
        
        synchronized (tunelControl) {
            grupo_entrando ++; // marca grupo como entrando
        }

        pasar_por_tunel.acquire();
        gente_entrado++; //cuanta gente a pasado
        //System.out.println("Humano " + IdH + " está pasando al refugio");
        Thread.sleep(1000); // simula paso por el túnel
        pasar_por_tunel.release();

        synchronized (tunelControl) {
            if (gente_entrado == 3) { //han pasado el grupo al completo
                grupo_entrando = 0; //el grupo a pasado completo
                gente_entrado = 0; //reseteo del contador
                //System.out.println("El grupo para entrar ya ha pasado");
                tunelControl.notifyAll(); // libera a los que estaban esperando salir
            }
        }

        crear_grupo_entrar_refugio.release(); //creacion siguiente grupo para entrar al refugio
    }
}
