
package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

public class Partido implements Serializable {
    private int idPartido;
    private String fase;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private String estado;

    public Partido(int idPartido, String fase, String fecha, String hora, String estadio,
                   String seleccion1, String seleccion2, String estado) {
        this.idPartido = idPartido;
        this.fase = fase;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.estado = estado;
    }

    public int getIdPartido() {
        return idPartido;
    }
    public String getFase() {
        return fase;
    }
    public String getFecha() {
        return fecha;
    }
    public String getHora() {
        return hora;
    }
    public String getEstadio() {
        return estadio;
    }
    public String getSeleccion1() {
        return seleccion1;
    }
    public String getSeleccion2() {
        return seleccion2;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idPartido + ";" + fase + ";" + fecha + ";" + hora + ";" + estadio + ";"
                + seleccion1 + ";" + seleccion2 + ";" + estado;
    }
}