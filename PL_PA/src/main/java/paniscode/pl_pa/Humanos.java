package paniscode.pl_pa;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
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
    private boolean muerto;
    private boolean atacado;

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

    public String getIdH() {
        return IdH;
    }
    
    public void Atacado(){this.atacado = true;}
    
    public void Muerto(){this.muerto = true;}
    public void setComida(int comida) {
        this.comida = comida;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public boolean isAtacado() {
        return atacado;
    }

    @Override
    public void run() {
        try {
            
            while(!this.muerto){
                refugio.ir_zona_común(IdH);
                //System.out.println("El humano "+this.IdH+" va a zona común");
                

                int tunel = random.nextInt(1,5); //1-4
                //System.out.println("El humano "+this.IdH+" va por el tunel " + tunel);
                refugio.ir_tunel(tunel,this.IdH);
                

                exterior.entrar_zona(tunel, this);
                
                System.out.println("El humano "+this.IdH+" va a la zona " + tunel);
                this.setComida(2);
                exterior.aguantar(this);
                
                //da igual si esta vivo, herido o muerto le sacamos de la lista
                exterior.sacar_zona(tunel, this); //sacamos al humano de la lista en la zona
                if(!this.muerto){
                    if(this.atacado){
                        System.out.println("El humano "+this.IdH+" ha sido atacado pero no asesinado");
                        this.setComida(0); 
                    }
                    else{}
                    exterior.ir_tunel(tunel,this.IdH);
                    //System.out.println("El humano "+this.IdH+" regresa por el tunel" + tunel);
                    
                    
                    refugio.depositar_comida(this.comida);
                    //System.out.println("El humano "+this.IdH+" a dejado " + this.comida+" piezas de comida");
     
                    //System.out.println("El humano "+this.IdH+" va a descansar");
                    refugio.ir_zona_descanso(IdH);
                      
                    //System.out.println("El humano "+this.IdH+" va a comer");
                    refugio.ir_comedor(IdH);
                        
                    if(this.atacado){
                        //System.out.println("El humano "+this.IdH+" va a recuperarse de las heridas anteriores");
                        refugio.ir_recuperarse(IdH);
                        }
                    else{}
                }
                else{}
            }
            System.out.println("El humano "+this.IdH+" ha muerto");
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Humanos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}