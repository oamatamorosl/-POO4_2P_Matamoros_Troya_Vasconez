package com.example.pronosticosmundial2026.modelo;


import java.io.Serializable;

public class Resultado implements Serializable {
    private int idResultado;
    private int idPartido;
    private int golesSeleccion1;
    private int golesSeleccion2;

    public Resultado(int idResultado, int idPartido, int golesSeleccion1, int golesSeleccion2) {
        this.idResultado = idResultado;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
    }

    public int getIdResultado() {
        return idResultado;
    }
    public int getIdPartido() {
        return idPartido;
    }
    public int getGolesSeleccion1() {
        return golesSeleccion1;
    }
    public int getGolesSeleccion2() {
        return golesSeleccion2;

    }

    @Override
    public String toString() {
        return idResultado + ";" + idPartido + ";" + golesSeleccion1 + ";" + golesSeleccion2;
    }
}