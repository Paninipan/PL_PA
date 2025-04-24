package paniscode.pl_pa;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvaro
 */
public class Humanos extends Thread{
    private Random random = new Random();
    private String IdH;
    private Refugio refugio;
    
    private Exterior exterior;

    private int comida;
    private final boolean muerto;
    private final boolean atacado;

    /**
     *
     * @param IdH
     * @param refugio
     * @param exterior
     */
    public Humanos(String IdH, Refugio refugio, Exterior exterior) {
        this.IdH = IdH;
        this.refugio = refugio;
        this.exterior = exterior;
        this.comida = 0;
        this.muerto = false;
        this.atacado = false;
    }
    

    public void setComida(int comida) {
        this.comida = comida;
    }

    @Override
    public void run() {
        try {
            
            while(!this.muerto){
                refugio.ir_zona_común(IdH);
                MX.print("El humano "+this.IdH+" va a zona común");
                

                int tunel;
                refugio.ir_tunel(tunel = random.nextInt(1,4));
                MX.print("El humano "+this.IdH+" va por el tunel" + tunel);

                exterior.ir_zona(tunel);
                this.setComida(2);
                exterior.aguantar(this.IdH);

                if(!this.muerto){
                    exterior.ir_tunel(tunel);
                    MX.print("El humano "+this.IdH+" regresa por el tunel" + tunel);

                    if(this.atacado){
                        this.setComida(0); 
                    }
                    else{}
                    refugio.depositar_comida(this.comida);
                    MX.print("El humano "+this.IdH+" a dejado " + this.comida+" piezas de comida");

                    MX.print("El humano "+this.IdH+" va a descansar");
                    refugio.ir_zona_descanso(IdH);

                    MX.print("El humano "+this.IdH+" va a comer");
                    refugio.ir_comedor(IdH);

                    if(this.atacado){
                        MX.print("El humano "+this.IdH+" va a recuperarse de las heridas anteriores");
                        refugio.ir_recuperarse(IdH);
                        }
                    else{}
                }
                else{}
            }          
        } catch (InterruptedException ex) {
            Logger.getLogger(Humanos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}