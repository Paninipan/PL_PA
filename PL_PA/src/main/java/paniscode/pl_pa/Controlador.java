/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;



/**
 *
 * @author javie
 */
public class Controlador {
    private boolean enPausa = false;

    public synchronized void pausar() {
        enPausa = true;
    }

    public synchronized void reanudar() {
        enPausa = false;
        notifyAll(); // Despierta a todos los hilos que estén esperando
    }

    public synchronized void esperarSiPausado() {
        if(enPausa){
            while (enPausa) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        else{}
    }
    
    


}
