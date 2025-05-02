/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author javie
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Cliente{
    public static void main(String[] args){
        try{
            boolean reservado = false;
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            InterfazRemota obj = (InterfazRemota) Naming.lookup("//localhost/Objeto");
            //InterfazCliente interfaz = new InterfazCliente(obj);
            //interfaz.setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}