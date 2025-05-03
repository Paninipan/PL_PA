package paniscode.pl_pa;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

public class Main {

    private static final int NUM_HUMANOS = 100; // Número de ejemplo de humanos iniciales

    public static void main(String[] args) {
        try{
            Controlador controlador = new Controlador();

            LocateRegistry.createRegistry(1099);
            Random random = new Random();

            InterfazP1 interfazP1 = new InterfazP1();
            interfazP1.setVisible(true);

            Tunel tunel1 = new Tunel(1,interfazP1, controlador);
            Tunel tunel2 = new Tunel(2,interfazP1, controlador);
            Tunel tunel3 = new Tunel(3,interfazP1, controlador);
            Tunel tunel4 = new Tunel(4,interfazP1, controlador);
            
            Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4,interfazP1, controlador);
            Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4,interfazP1, controlador);

            Zombies pacienteCero = new Zombies("Z0000", exterior, controlador); // Aquí he puesto el primer zombie
            pacienteCero.start();
            
            ObjetoRemoto simulacion = new ObjetoRemoto(controlador);
            Naming.rebind("//localhost/Simulacion", simulacion);

            for (int i = 0; i < NUM_HUMANOS; i++) {
                String id = String.format("H%04d", i+1);
                controlador.esperarSiPausado();
                Humanos humano = new Humanos(id, refugio, exterior, controlador);
                humano.start();
                try {
                    Thread.sleep(500 + random.nextInt(1500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error en el servidor RMI: " + e);
            e.printStackTrace();
        }

    }
}
