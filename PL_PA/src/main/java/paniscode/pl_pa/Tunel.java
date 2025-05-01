/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Tunel {

    private int tunelId;
    private InterfazP1 interfazP1;

    private Semaphore crear_grupo_salir_refugio = new Semaphore(3); //tres humanos para salir
    private Semaphore crear_grupo_entrar_refugio = new Semaphore(3); //tres humanos para entrar

    private CyclicBarrier espera_salir_refugio = new CyclicBarrier(3);
    private CyclicBarrier espera_entrar_refugio = new CyclicBarrier(3);

    private Semaphore pasar_por_tunel = new Semaphore(1); //1 a la vez

    private final Object tunelControl = new Object(); //"monitor para esperar si hay gente intentando entrar" 
    private int grupo_entrando = 0; //gente intentando entrar
    private int gente_entrado = 0; //gente intentando entrar
    
    private List<String> humanos_grupo_salida = new ArrayList<>();
    private List<String> humanos_grupo_entrada = new ArrayList<>();



    public Tunel(int tunelId,InterfazP1 interfazP1) {
        this.tunelId = tunelId;
        this.interfazP1 = interfazP1;
    }
    
    

    public void salir_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        crear_grupo_salir_refugio.acquire(); //si hay 3 se bloquea
        //System.out.println("Humano " + IdH + " entra al grupo para poder salir");
        
        
        //modificar el texto del grupo de salida para que esten los 3 y se van añadiendo
        humanos_grupo_salida.add(IdH);
        this.interfazP1.mod_text_tuneles_salida(humanos_grupo_salida, this.tunelId);
   
 
        espera_salir_refugio.await(); // esperan a formar un grupo de 3
        //la barrera se reinicia esperando a los tres siguientes hilos para salir del refugio
        sleep(500);
  
        pasar_por_tunel.acquire(); //vamos a pasar de 1 en 1
        
        synchronized (tunelControl) { //accedemos al monitor ordenadamente
            while (grupo_entrando == 3) { //si hay grupo par entrar esperamos
                //System.out.println("Humano " + IdH + " espera a que el grupo de entrada cruce");
                pasar_por_tunel.release();
                tunelControl.wait(); //esperamos
                pasar_por_tunel.acquire();
            }
        }
  
        //modificamos la lista de humanos en el grupo 
        humanos_grupo_salida.remove(IdH);

        //modificamos el texto grupo salida tunel x
        this.interfazP1.mod_text_tuneles_salida(humanos_grupo_salida, this.tunelId);
        
        //modificamos el texto del paso tunel x
        this.interfazP1.mod_text_paso_tunel(IdH, this.tunelId);
        
        //System.out.println("Humano " + IdH + " está saliendo del refugio");
        Thread.sleep(1000); // simula paso por el túnel
        
        //vaciamos el texto del paso tunel x
        this.interfazP1.mod_text_paso_tunel(null, this.tunelId);
        
        pasar_por_tunel.release(); //cuando sale pasa el siguiente

        crear_grupo_salir_refugio.release(); //que pase a formarse el siguiente grupo
       
    }

    public void entrar_refugio(String IdH) throws InterruptedException, BrokenBarrierException {
        crear_grupo_entrar_refugio.acquire(); //tres humanos por grupo
        //System.out.println("Humano " + IdH + " entra al grupo para entrar");
        
        // modificamos la lista con los humanos del grupo de entrada
        humanos_grupo_entrada.add(IdH);
        this.interfazP1.mod_text_tuneles_entrada(humanos_grupo_entrada,this.tunelId);

        espera_entrar_refugio.await(); // esperan a formar grupo de entrada
        //la barrera se reinicia esperando a los tres siguientes hilos para entrar al refugio
        sleep(500);
 
        //modiicamos el texto grupo entrada
        
        synchronized (tunelControl) {
            grupo_entrando ++; // marca grupo como entrando
        }

        pasar_por_tunel.acquire(); //pasa un humano a la vez
        
        //modificamo la lista y el texto del tunel x
        humanos_grupo_entrada.remove(IdH);
        
        this.interfazP1.mod_text_tuneles_entrada(humanos_grupo_entrada,this.tunelId);
        //cada vez que sale un humano se resetea el texto del grupo, si la lista esta vacia, se vacia el texto del jtext
        
        
        //modificamos el paso del tunel x
        this.interfazP1.mod_text_paso_tunel(IdH, this.tunelId);
        
        
        
        gente_entrado++; //cuanta gente a pasado
        //System.out.println("Humano " + IdH + " está pasando al refugio");
        Thread.sleep(1000); // simula paso por el túnel
        
        
        //vaciamos el texto del paso del tunel x
        this.interfazP1.mod_text_paso_tunel(null, this.tunelId);


        
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
