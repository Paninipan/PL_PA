package paniscode.pl_pa;

/**
 * Clase Controlador que gestiona la pausa y reanudación de la simulación.
 * Implementa un mecanismo de sincronización para coordinar los hilos de ejecución
 * cuando la simulación se pausa o reanuda.
 */
public class Controlador {
    private boolean enPausa = false; // Estado actual de pausa

    /**
     * Pausa la simulación.
     * Bloquea todos los hilos que llamen a esperarSiPausado() hasta que se reanude.
     */
    public synchronized void pausar() {
        enPausa = true;
    }

    /**
     * Reanuda la simulación.
     * Despierta todos los hilos que estaban esperando debido a una pausa.
     */
    public synchronized void reanudar() {
        enPausa = false;
        notifyAll(); // Notifica a todos los hilos en espera
    }

    /**
     * Método que los hilos deben llamar periódicamente para respetar el estado de pausa.
     * Si la simulación está pausada, el hilo actual se bloquea hasta que se reanude.
     * Maneja correctamente las interrupciones preservando el estado de interrupción.
     */
    public synchronized void esperarSiPausado() {
        if(enPausa) {
            while (enPausa) {
                try {
                    wait(); // Espera hasta ser notificado
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                }
            }
        }
    }
}