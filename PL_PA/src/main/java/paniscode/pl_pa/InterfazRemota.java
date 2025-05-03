/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package paniscode.pl_pa;

/**
 *
 * @author javie
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfazRemota extends Remote {
    void pausarSimulacion() throws RemoteException;
    void reanudarSimulacion() throws RemoteException;
    int getHumanosRefugio() throws RemoteException;
    int getTunel1() throws RemoteException;
    int getTunel2() throws RemoteException;
    int getTunel3() throws RemoteException;
    int getTunel4() throws RemoteException;

    int getZona1Hum() throws RemoteException;
    int getZona2Hum() throws RemoteException;
    int getZona3Hum() throws RemoteException;
    int getZona4Hum() throws RemoteException;

    int getZona1Zom() throws RemoteException;
    int getZona2Zom() throws RemoteException;
    int getZona3Zom() throws RemoteException;
    int getZona4Zom() throws RemoteException;


}