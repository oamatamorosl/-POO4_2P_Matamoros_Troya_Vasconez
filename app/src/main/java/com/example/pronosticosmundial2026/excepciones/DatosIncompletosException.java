package com.example.pronosticosmundial2026.excepciones;

public class DatosIncompletosException extends RuntimeException {
    public DatosIncompletosException(String message) {
        super(message);
    }
}
