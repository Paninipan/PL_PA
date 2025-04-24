/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

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
    
    
    void ir_zona(int tunel, String IdH) {
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
    
}
