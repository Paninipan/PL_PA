/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paniscode.pl_pa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger instance;
    private static final String FILE_NAME = "apocalipsis.txt"; // Se guarda en este archivo, no sé cómo poner que se guarde en 'Resources'
    private static final Object lock = new Object();

    private BufferedWriter writer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH"); // El formato que piden

    private Logger() {
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); // Para darle funcionalidad al writer cuando se usa
        } catch (IOException e) {
        }
    }

    public static Logger getInstance() { // No se si puede ser estática, lo mismo con las variables de arriba. Sirve para reconocer qué clase estamos instanciando
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String fullMessage = "[" + timestamp + "] " + message;

        synchronized (lock) {
            try {
                writer.write(fullMessage);
                writer.newLine();
                writer.flush(); // Le pregunté a chatgpt y dice que esto es relevante para que lo muestre en tiempo real (no estoy seguro)
            } catch (IOException e) {
            }
        }
    }
    
    public void close() { // Simplemente deja de ejecutar el logger y se da por finalizada X instancia
        synchronized (lock) {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
            }
        }
    }
}
