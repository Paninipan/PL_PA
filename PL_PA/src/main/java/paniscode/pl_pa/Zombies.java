/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

/**
 *
 * @author zona
 */
public class Zombies extends Thread {
    private final Controlador controlador;
    private Random random = new Random();
    private String IdZ;
    private Exterior exterior;

    private int contadorMuertes;
    private boolean atacarEnZona;
    private boolean activo;
    
    

    /**
     *
     * @param IdZ
     * @param exterior
     * @param controlador
     */
    public Zombies(String IdZ, Exterior exterior, Controlador controlador) {
        this.IdZ = IdZ;
        this.exterior = exterior;
        this.contadorMuertes = 0;
        this.atacarEnZona = false;
        this.controlador = controlador;
    }

    public void setContadorMuertes(int contadorMuertes) {
        this.contadorMuertes = contadorMuertes;
    }

    public void setAtacarEnZona(boolean atacarEnZona) {
        this.atacarEnZona = atacarEnZona;
    }

    @Override
    public void run() {
        try {
            while(true){
                controlador.esperarSiPausado();
                int zona = random.nextInt(1,5);
                System.out.println("El zombie " + IdZ + " se dirige hacia " + zona);
                exterior.atacar(zona, this);
            }
         
        } catch (InterruptedException ex) {
            Logger.getLogger(Zombies.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIdZ() {
        return IdZ;
    }

    public int getContadorMuertes() {
        return contadorMuertes;
    }

    public Exterior getExterior() {
        return exterior;
    }
}
