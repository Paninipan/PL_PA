/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author alvaro
 */

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Refugio{
    private Random random = new Random();
    private Tunel tunel1;
    
    private Tunel tunel2;
    
    private Tunel tunel3;
    
    private Tunel tunel4;
    
    private Lock reservas_comida = new ReentrantLock();
    private Condition reservas_vacias = reservas_comida.newCondition();
    
    private int cantidad_comida;

    
    
    

    public Refugio(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4, int reservas_comida) {
        this.tunel1 = tunel1;
        this.tunel2 = tunel2;
        this.tunel3 = tunel3;
        this.tunel4 = tunel4;
        this.cantidad_comida = 0;
    }

    public void ir_zona_común(String IdH) throws InterruptedException {
        sleep(1000 * random.nextInt(1,2));
    }

    public void ir_tunel(int tunel) {
        switch(tunel){
            case 1 -> tunel1.salir();
            case 2 -> tunel2.salir();
            case 3 -> tunel3.salir();
            case 4 -> tunel4.salir();
        }
    }

    public void depositar_comida(int comida) throws InterruptedException {
        try{
            reservas_comida.lock();
            if(comida>0){
                cantidad_comida+=comida;
                reservas_vacias.signal();}
            else{}      
        }
        finally{
            reservas_comida.unlock();
        }
    }

    public void ir_zona_descanso(String IdH) throws InterruptedException {
        sleep(1000 * random.nextInt(2,4));
    }

    public void ir_comedor(String IdH) throws InterruptedException {
        try{
            reservas_comida.lock();
            if(cantidad_comida==0){
                reservas_vacias.await();            
            }      
            else{}
            sleep(1000 * random.nextInt(3,5));
            MX.print("El humano "+IdH+" va a comer");
            cantidad_comida--;
        }
        finally{
            reservas_comida.unlock();
        }
    
        
    }

    public void ir_recuperarse(String IdH) throws InterruptedException {
        sleep(1000 * random.nextInt(3,5));

    }
 
}

