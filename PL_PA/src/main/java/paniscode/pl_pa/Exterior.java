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

/**
 *
 * @author alvaro
 */
class Exterior {
    
    private Random random = new Random();
    private Tunel tunel1;
    
    private Tunel tunel2;
    
    private Tunel tunel3;
    
    private Tunel tunel4;
    
    private ArrayList<Humanos> zona1_Humanos;
    private ArrayList<Humanos> zona2_Humanos;
    private ArrayList<Humanos> zona3_Humanos;
    private ArrayList<Humanos> zona4_Humanos;

    public Exterior(Tunel tunel1, Tunel tunel2, Tunel tunel3, Tunel tunel4) {
        this.tunel1 = tunel1;
        this.tunel2 = tunel2;
        this.tunel3 = tunel3;
        this.tunel4 = tunel4;
        this.zona1_Humanos = new ArrayList<>();
        this.zona2_Humanos = new ArrayList<>();
        this.zona3_Humanos = new ArrayList<>();
        this.zona4_Humanos = new ArrayList<>();
    }
    
    
    
    
    void entrar_zona(int tunel, Humanos humano) { //meter al humano en la lista correspondiente tras cruzar el tunel
        switch(tunel){
            case 1 -> zona1_Humanos.add(humano);
            case 2 -> zona2_Humanos.add(humano);
            case 3 -> zona3_Humanos.add(humano);
            case 4 -> zona4_Humanos.add(humano);
        }
        MX.print("Humano"+ humano.getIdH()+" en zona "+tunel);
    }


    public void aguantar(Humanos humano) { //esperar a que el humano termine de recolectar o sea atacado
        int tiempo_max = random.nextInt(3,6)*1000;
        long tiempo_inicio = System.currentTimeMillis();
        boolean fin_recoleccion = false;
        while(!humano.isAtacado() && !humano.isMuerto() && !fin_recoleccion){
            if(System.currentTimeMillis()-tiempo_inicio >= tiempo_max){fin_recoleccion = true;}
                   
        }
    }

        
    public void atacar(int zona, Zombies zombie) throws InterruptedException{
        long startTime = System.currentTimeMillis();
        Humanos elegido = null;
        long tiempo = 1000*random.nextLong(2,4);
        List<List<Humanos>> zonas = Arrays.asList(null, zona1_Humanos, zona2_Humanos, zona3_Humanos, zona4_Humanos); // índice 1-4
        List<Humanos> humanosZona = zonas.get(zona);

        while (humanosZona.isEmpty()) {
            if (System.currentTimeMillis() - startTime >= tiempo) break;
        }

        if (!humanosZona.isEmpty()) {
            elegido = humanosZona.get(random.nextInt(humanosZona.size()));
            ataque(elegido, zombie);
        }
                sleep(1000*random.nextInt(2,4));
            }
    
    public void ataque(Humanos elegido, Zombies zombie) throws InterruptedException{
        sleep(100*random.nextInt(5,16));
        int probabilidad_exito = random.nextInt(1,4);
        if(probabilidad_exito==1){
            elegido.Muerto();
            Zombies asesinado = new Zombies("Z"+elegido.getIdH().substring(1),zombie.getExterior());
            asesinado.start();
        }
        elegido.Atacado();}

    void ir_tunel(int tunel, String IdH) throws InterruptedException, BrokenBarrierException {
                switch(tunel){
            case 1 -> tunel1.entrar_refugio(IdH);
            case 2 -> tunel2.entrar_refugio(IdH);
            case 3 -> tunel3.entrar_refugio(IdH);
            case 4 -> tunel4.entrar_refugio(IdH);
        }
    }
}
