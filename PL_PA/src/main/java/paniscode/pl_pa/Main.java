package paniscode.pl_pa;

import java.util.Random;

public class Main {

    private static final int NUM_HUMANOS = 15; // Número de ejemplo de humanos iniciales

    public static void main(String[] args) {
        Random random = new Random();
        
        InterfazP1 interfazP1 = new InterfazP1();
        interfazP1.setVisible(true);
        
        Tunel tunel1 = new Tunel(1,interfazP1);
        Tunel tunel2 = new Tunel(2,interfazP1);
        Tunel tunel3 = new Tunel(3,interfazP1);
        Tunel tunel4 = new Tunel(4,interfazP1);

        Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4,interfazP1);
        Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4,interfazP1);

        Zombies pacienteCero = new Zombies("Z0000", exterior); // Aquí he puesto el primer zombie
        pacienteCero.start();

        for (int i = 0; i < NUM_HUMANOS; i++) {
            String id = String.format("H%04d", i+1);
            Humanos humano = new Humanos(id, refugio, exterior);
            humano.start();
            try {
                Thread.sleep(500 + random.nextInt(1500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
