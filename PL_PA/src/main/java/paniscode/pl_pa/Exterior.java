/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Random;

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
        this.zona1_Humanos = new ArrayList<Humanos>();
        this.zona2_Humanos = new ArrayList<Humanos>();
        this.zona3_Humanos = new ArrayList<Humanos>();
        this.zona4_Humanos = new ArrayList<Humanos>();
    }
    
    
    
    
    void entrar_zona(int tunel, String IdH) {
        switch(tunel){
            case 1 -> tunel1.entrar_refugio(IdH);
            case 2 -> tunel2.entrar_refugio(IdH);
            case 3 -> tunel3.entrar_refugio(IdH);
            case 4 -> tunel4.entrar_refugio(IdH);
        }  
    }


    void aguantar(String IdH) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void ir_tunel(int tunel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public void atacar(int zona, Zombies zombie) throws InterruptedException{
        long startTime = System.currentTimeMillis();
        Humanos elegido = null;
        long tiempo = 1000*random.nextLong(2,3);
        switch(zona){
            case 1:
                while(zona1_Humanos.isEmpty()){
                    if(System.currentTimeMillis() - startTime >=tiempo){break;}
                }
                elegido = zona1_Humanos.get(random.nextInt(0, zona1_Humanos.size()));
                ataque(elegido, zombie);
                break;
            case 2:
                while(zona2_Humanos.isEmpty()){
                    if(System.currentTimeMillis() - startTime >=tiempo){break;}
                }
                elegido = zona2_Humanos.get(random.nextInt(0, zona2_Humanos.size()));
                ataque(elegido, zombie);
                break;
            case 3:
                while(zona3_Humanos.isEmpty()){
                    if(System.currentTimeMillis() - startTime >=tiempo){break;}
                }
                elegido = zona3_Humanos.get(random.nextInt(0, zona3_Humanos.size()));
                ataque(elegido, zombie);
                break;
            case 4:
                while(zona4_Humanos.isEmpty()){
                    if(System.currentTimeMillis() - startTime >=tiempo){break;}
                }
                elegido = zona4_Humanos.get(random.nextInt(0, zona4_Humanos.size()));
                ataque(elegido, zombie);
                break;      
        }
        sleep(1000*random.nextInt(2,3));
    }
    
    public void ataque(Humanos elegido, Zombies zombie) throws InterruptedException{
        sleep(100*random.nextInt(5,15));
        int probabilidad_exito = random.nextInt(1,3);
        if(probabilidad_exito==1){
            elegido.Muerto();
            Zombies asesinado = new Zombies("Z"+elegido.getIdH().substring(1),zombie.getExterior());
            asesinado.start();
        }
        elegido.Atacado();}
}
