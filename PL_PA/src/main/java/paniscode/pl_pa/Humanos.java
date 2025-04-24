/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author alvaro
 */
public class Humanos extends Thread{
    
    private String Id;
    private Refugio refugio;
    private int comida;

    public Humanos(String Id, Refugio refugio, int comida) {
        this.Id = Id;
        this.refugio = refugio;
        this.comida = comida;
    }
    
    
    public int getComida() {
        return comida;
    }

   
    public void setComida(int comida) {
        this.comida = comida;
    }

    
    public Refugio getRefugio() {
        return refugio;
    }

    
    public void setRefugio(Refugio refugio) {
        this.refugio = refugio;
    }

    
    public String getId() {
        return Id;
    }

    /**
     * Set the value of Id
     *
     * @param Id new value of Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

}
