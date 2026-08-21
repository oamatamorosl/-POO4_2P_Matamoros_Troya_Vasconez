package com.example.pronosticosmundial2026.modelo;


import java.io.Serializable;

public class Pronostico implements Serializable {
    private int idPronostico;
    private String idUsuario;
    private int idPartido;
    private int golesSeleccion1;
    private int golesSeleccion2;
    private int puntosObtenidos;

    public Pronostico(int idPronostico, String idUsuario, int idPartido,
                      int golesSeleccion1, int golesSeleccion2) {

        this.idPronostico = idPronostico;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
        this.puntosObtenidos = 0;
    }

    public int getIdPronostico() {
        return idPronostico;
    }
    public String getIdUsuario() {
        return idUsuario;
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
    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setGolesSeleccion1(int golesSeleccion1) {
        this.golesSeleccion1 = golesSeleccion1;
    }
    public void setGolesSeleccion2(int golesSeleccion2) {
        this.golesSeleccion2 = golesSeleccion2;
    }
    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }
}