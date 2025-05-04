package paniscode.pl_pa;

/**
 * Interfaz remota que define los métodos disponibles para el control distribuido de la simulación.
 * Esta interfaz permite la comunicación entre el cliente remoto y el servidor mediante RMI.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/** 
 * Definiciones de todos los métodos usados entre el Cliente y el Servidor mediante un 'interface'.
 */
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

    String ranking() throws RemoteException;
}