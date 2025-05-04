package paniscode.pl_pa;

/**
 * Implementación del objeto remoto para el control distribuido de la simulación.
 * Esta clase permite la monitorización y control remoto del sistema mediante RMI.
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemota {
    // Referencias a los componentes principales del sistema
    private final Refugio refugio;
    private final Tunel tunel1, tunel2, tunel3, tunel4;
    private final Exterior exterior;
    private final Controlador controlador;

    /**
     * Constructor que inicializa el objeto remoto con referencias a los componentes clave.
     * @param controlador Controlador principal de la simulación
     * @param refugio Referencia al refugio principal
     * @param t1 Tunel 1
     * @param t2 Tunel 2
     * @param t3 Tunel 3
     * @param t4 Tunel 4
     * @param exterior Zona exterior de la simulación
     * @throws RemoteException
     */
    public ObjetoRemoto(Controlador controlador, Refugio refugio, Tunel t1, Tunel t2, Tunel t3, Tunel t4, Exterior exterior) throws RemoteException {
        this.controlador = controlador;
        this.refugio = refugio;
        this.tunel1 = t1;
        this.tunel2 = t2;
        this.tunel3 = t3;
        this.tunel4 = t4;
        this.exterior = exterior;
    }

    /**
     * Implementación remota para pausar la simulación.
     * @throws RemoteException
     */
    @Override
    public void pausarSimulacion() throws RemoteException {
        controlador.pausar();
    }

    /**
     * Implementación remota para reanudar la simulación.
     * @throws RemoteException
     */
    @Override
    public void reanudarSimulacion() throws RemoteException {
        controlador.reanudar();
    }

    // Métodos de consulta del estado de la simulación

    /**
     * Obtiene el número de humanos en el refugio.
     * @return Cantidad de humanos en el refugio
     */
    @Override
    public int getHumanosRefugio() {
        return refugio.getHRefugio();
    }

    /**
     * Obtiene el número de humanos los túneles.
     * @return Cantidad de humanos en cada túnel.
     */
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

    /**
     * Obtiene el número de humanos en las zonas exteriores.
     * @return Cantidad de humanos en cada zona exterior.
     */
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

    /**
     * Obtiene el número de zombies en las zonas exteriores.
     * @return Cantidad de zombies en cada zona exterior.
     */
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
    
    @Override
    public String ranking() {
        return exterior.rankingZombies();
    }
}