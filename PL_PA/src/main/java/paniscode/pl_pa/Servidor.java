/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author javie
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor{
    public static void main(String[] args){
        try{
            ObjetoRemoto obj = new ObjetoRemoto();
            Registry reg = LocateRegistry.createRegistry(1099);
            //Naming.rebind("//localhost/Objeto", obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}