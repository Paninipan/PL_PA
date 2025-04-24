/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunel {

    private ArrayList<String> Hilos_ir_refugio;
    private ArrayList<String> Hilos_salir_refugio;
    private ArrayList<String> grupo_entrar_refugio;
    private ArrayList<String> grupo_salir_refugio;

    private Lock paso_tunel = new ReentrantLock();
    private Condition grupo_entrada_lleno = paso_tunel.newCondition();
    private Condition grupo_salida_lleno = paso_tunel.newCondition();
    private Condition salir_tunel = paso_tunel.newCondition();

    private CyclicBarrier salir_refugio = new CyclicBarrier(3);

    public Tunel() {
        this.Hilos_ir_refugio = new ArrayList<>();
        this.Hilos_salir_refugio = new ArrayList<>();
        this.grupo_entrar_refugio = new ArrayList<>();
        this.grupo_salir_refugio = new ArrayList<>();
    }

    void salir_refugio(String IdH) throws InterruptedException, BrokenBarrierException {

        try {
            paso_tunel.lock();

            this.Hilos_salir_refugio.add(IdH);
            int cantidad_grupo_salida = this.grupo_salir_refugio.size();

            if (cantidad_grupo_salida == 3) {
                MX.print("Humano " + IdH + " espera o atro grupo");
                grupo_salida_lleno.await();
            } else {
            }
            this.grupo_salir_refugio.add(IdH);
            MX.print("Humano " + IdH + " entra al grupo");
            salir_refugio.await();

            do {
                if (this.grupo_entrar_refugio.size() == 3) {
                    MX.print("Humano " + IdH + " espera a que entren ");
                    salir_tunel.await();
                } else {
                    MX.print("Humano " + IdH + " sale");
                    salir();
                }
            } while (!grupo_salir_refugio.isEmpty());

        } finally {
            paso_tunel.unlock();
        }

    }

    public synchronized void salir() {
        grupo_salir_refugio.removeFirst();
    }

    void entrar_refugio(String IdH) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
