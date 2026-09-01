package com.example.pronosticosmundial2026.excepciones;

/**
 * Excepción verificada que se lanza cuando un participante intenta registrar
 * o modificar un pronóstico de un partido cuyo período ya finalizó (es decir,
 * el partido está en estado CERRADO o FINALIZADO).
 *
 * @author Equipo POO
 * @version 1.0
 */
public class PronosticoFueraDeTiempoException extends Exception {

    /**
     * Crea la excepción con un mensaje descriptivo del error.
     *
     * @param message Mensaje que indica que el período para pronosticar ya finalizó.
     */
    public PronosticoFueraDeTiempoException(String message) {
        super(message);
    }
}