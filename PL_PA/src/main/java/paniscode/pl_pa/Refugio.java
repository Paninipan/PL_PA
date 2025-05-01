package paniscode.pl_pa;

/**
 *
 * @author alvaro
 */

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Refugio{
    private Random random = new Random();
    
    private InterfazP1 interfazP1;

    private Tunel tunel1;
    private Tunel tunel2;
    private Tunel tunel3;
    private Tunel tunel4;
    
    private Lock reservas_comida = new ReentrantLock();
    private Condition reservas_vacias = reservas_comida.newCondition();
    
    private int cantidad_comida;
    
    private List<String> humanos_zona_comun = new ArrayList<>();
    private List<String> humanos_zona_descanso = new ArrayList<>();
    private List<String> humanos_comedor = new ArrayList<>();

    public Refugio(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4,InterfazP1 interfazP1) {
        this.tunel1 = tunel1;
        this.tunel2 = tunel2;
        this.tunel3 = tunel3;
        this.tunel4 = tunel4;
        this.cantidad_comida = 0;
        this.interfazP1 = interfazP1;
    }

    public void ir_zona_común(String IdH) throws InterruptedException {
        this.humanos_zona_comun.add(IdH);
        this.interfazP1.mod_text_zonaComun(humanos_zona_comun);
        sleep(1000 * random.nextInt(1,3));
        this.humanos_zona_comun.remove(IdH);
        this.interfazP1.mod_text_zonaComun(humanos_zona_comun);
    }

    public void ir_tunel(int tunel, String IdH) throws InterruptedException, BrokenBarrierException {
        switch(tunel){
            case 1 -> tunel1.salir_refugio(IdH);
            case 2 -> tunel2.salir_refugio(IdH);
            case 3 -> tunel3.salir_refugio(IdH);
            case 4 -> tunel4.salir_refugio(IdH);
        }
    }

    public void depositar_comida(int comida) throws InterruptedException {
        try{
            reservas_comida.lock();
            if(comida>0){
                cantidad_comida+=comida;
                sleep(250); //retardo en de lo que tardan en descargar
                this.interfazP1.mod_text_comida(this.cantidad_comida);
                reservas_vacias.signal();}
            else{}      
        }
        finally{
            reservas_comida.unlock();
        }
    }

    public void ir_zona_descanso(String IdH) throws InterruptedException {
        this.humanos_zona_descanso.add(IdH);
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
        sleep(1000 * random.nextInt(2,5));
        this.humanos_zona_descanso.remove(IdH);
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
       
    }

    public void ir_comedor(String IdH) throws InterruptedException {
        try{
            this.humanos_comedor.add(IdH);
            this.interfazP1.mod_text_comedor(humanos_comedor);
            
            reservas_comida.lock();
            if(cantidad_comida==0){
                reservas_vacias.await();            
            }      
            else{}
            
            cantidad_comida--;
            this.interfazP1.mod_text_comida(cantidad_comida);
            
            //System.out.println("El humano "+IdH+" va a comer");
            
        }
        finally{
            reservas_comida.unlock();
            sleep(1000 * random.nextInt(3,6));
            this.humanos_comedor.remove(IdH);
            this.interfazP1.mod_text_comedor(humanos_comedor);
        }
    
        
    }

    public void ir_recuperarse(String IdH) throws InterruptedException {
        this.humanos_zona_descanso.add(IdH);
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
        sleep(1000 * random.nextInt(3,6));
        //System.out.println("El humano "+IdH+" se ha recuperado del ataque");
        this.humanos_zona_descanso.remove(IdH);
        this.interfazP1.mod_text_descanso(humanos_zona_descanso);
    }
 
}