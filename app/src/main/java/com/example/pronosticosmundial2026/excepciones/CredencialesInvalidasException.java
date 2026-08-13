package com.example.pronosticosmundial2026.excepciones;

public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}