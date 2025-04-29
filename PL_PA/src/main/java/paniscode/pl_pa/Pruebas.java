/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paniscode.pl_pa;

import static java.lang.Thread.sleep;

/**
 *
 * @author alvaro
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Tunel tunel1 = new Tunel();
        Tunel tunel2 = new Tunel();
        Tunel tunel3 = new Tunel();
        Tunel tunel4 = new Tunel();

        Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4);
        Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4);
        
        for(int i = 0; i<50;i++){
            Humanos humano = new Humanos("H"+i,refugio,exterior);
            humano.start();
            sleep(1000);
                    
        }
    }
    
}
