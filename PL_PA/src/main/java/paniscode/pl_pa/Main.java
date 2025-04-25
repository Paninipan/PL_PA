package paniscode.pl_pa;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int NUM_HUMANOS = 10; // Número de ejemplo de humanos iniciales

    public static void main(String[] args) {
        Tunel tunel1 = new Tunel();
        Tunel tunel2 = new Tunel();
        Tunel tunel3 = new Tunel();
        Tunel tunel4 = new Tunel();

        Refugio refugio = new Refugio(tunel1, tunel2, tunel3, tunel4);
        Exterior exterior = new Exterior(tunel1, tunel2, tunel3, tunel4);

        Zombies pacienteCero = new Zombies("Z0000", exterior); // Aquí he puesto el primer zombie
        pacienteCero.start();

        Thread generadorHumanos = new Thread(() -> { // He puesto la creación de humanos como un propio hilo (puede que no sea necesario)
            Random random = new Random();
            for (int i = 0; i < NUM_HUMANOS; i++) {
                String id = String.format("H%04d", i); // Le doy un cierto valor a su id de manera ordenada
                Humanos humano = new Humanos(id, refugio, exterior); // Lo mismo que con los zombies
                humano.start();
                try {
                    long espera = 500 + random.nextInt(1500); // El tiempo de espera 
                    TimeUnit.MILLISECONDS.sleep(espera);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        generadorHumanos.start();
    }
}
