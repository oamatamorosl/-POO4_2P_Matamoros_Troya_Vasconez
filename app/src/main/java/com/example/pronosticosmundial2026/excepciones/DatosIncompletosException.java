package com.example.pronosticosmundial2026.excepciones;

/**
 * Excepción que se lanza cuando faltan datos o son inválidos al registrar
 * un pronóstico o un resultado (por ejemplo, campos vacíos o goles negativos).
 *
 * @author Equipo POO
 * @version 1.0
 */
public class DatosIncompletosException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo del error.
     *
     * @param message Mensaje que indica qué datos faltan o son inválidos.
     */
    public DatosIncompletosException(String message) {
        super(message);
    }
}