/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author javie
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemota {
    private Refugio refugio;
    private Tunel tunel1, tunel2, tunel3, tunel4;
    private Exterior exterior;

  
    private final Controlador controlador;

    public ObjetoRemoto(Controlador controlador) throws RemoteException {
        this.controlador = controlador;
    }

    @Override
    public void pausarSimulacion() throws RemoteException {
        controlador.pausar();
    }

    @Override
    public void reanudarSimulacion() throws RemoteException {
        controlador.reanudar();
    }
   
 
    @Override
    public int getHumanosRefugio() {
        return refugio.getHRefugio();
    }

    @Override
    public int getTunel1() {
        return tunel1.getHTunel();
    }
@Override
    public int getTunel2() {
        return tunel2.getHTunel();
    }
@Override
    public int getTunel3() {
        return tunel3.getHTunel();
    }
@Override
    public int getTunel4() {
        return tunel4.getHTunel();
    }
@Override
    public int getZona1Hum() {
        return exterior.getHZona1();
    }
@Override
    public int getZona2Hum() {
        return exterior.getHZona2();
    }
@Override
    public int getZona3Hum() {
        return exterior.getHZona3();
    }
@Override
    public int getZona4Hum() {
        return exterior.getHZona4();
    }
@Override
    public int getZona1Zom() {
        return exterior.getZZona1();
    }
@Override
    public int getZona2Zom() {
        return exterior.getZZona2();
    }
@Override
    public int getZona3Zom() {
        return exterior.getZZona3();
    }
@Override
    public int getZona4Zom() {
        return exterior.getZZona4();
    }

}
