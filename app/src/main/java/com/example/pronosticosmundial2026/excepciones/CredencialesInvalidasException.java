package com.example.pronosticosmundial2026.excepciones;

/**
 * Excepción verificada que se lanza cuando las credenciales ingresadas en el
 * inicio de sesión no son válidas (usuario o contraseña incorrectos).
 *
 * @author Oscar_Matamoros
 * @version 1.0
 */
public class CredencialesInvalidasException extends Exception {

    /**
     * Crea la excepción con un mensaje descriptivo del error.
     *
     * @param message Mensaje que explica por qué las credenciales son inválidas.
     */
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}