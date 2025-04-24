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
     */
    public Zombies(String IdZ, Exterior exterior) {
        this.IdZ = IdZ;
        this.exterior = exterior;
        this.contadorMuertes = 0;
        this.atacarEnZona = false;
    }

    @Override
    public void run() {
        try {
            while(true){
                int zona = random.nextInt(1,4);
                MX.print("El zombie " + IdZ + " se dirige hacia " + zona);
                
                exterior.atacar(zona, this);
            }
         
        } catch (InterruptedException ex) {
            Logger.getLogger(Zombies.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Exterior getExterior() {
        return exterior;
    }
}
