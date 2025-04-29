package paniscode.pl_pa;

import java.util.Random;

public class Main {

    private static final int NUM_HUMANOS = 15; // Número de ejemplo de humanos iniciales

    public static void main(String[] args) {
        Random random = new Random();

        Tunel tunel1 = new Tunel();
        Tunel tunel2 = new Tunel();
        Tunel tunel3 = new Tunel();
        Tunel tunel4 = new Tunel();

        Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4);
        Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4);

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
