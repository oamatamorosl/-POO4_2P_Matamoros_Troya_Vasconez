package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

/**
 * Representa un partido del torneo, incluyendo sus datos y su estado actual
 * ({@code ABIERTO}, {@code CERRADO} o {@code FINALIZADO}). Implementa
 * {@link Serializable} para poder almacenarse cuando sea necesario.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class Partido implements Serializable {

    /** Identificador único del partido. */
    private int idPartido;

    /** Fase del torneo a la que pertenece el partido. */
    private String fase;

    /** Fecha en la que se disputa el partido. */
    private String fecha;

    /** Hora en la que se disputa el partido. */
    private String hora;

    /** Estadio donde se juega el partido. */
    private String estadio;

    /** Nombre de la primera selección. */
    private String seleccion1;

    /** Nombre de la segunda selección. */
    private String seleccion2;

    /** Estado actual del partido: ABIERTO, CERRADO o FINALIZADO. */
    private String estado;

    /**
     * Crea un nuevo partido con todos sus datos.
     *
     * @param idPartido Identificador único del partido.
     * @param fase Fase del torneo a la que pertenece.
     * @param fecha Fecha del partido.
     * @param hora Hora del partido.
     * @param estadio Estadio donde se disputa.
     * @param seleccion1 Nombre de la primera selección.
     * @param seleccion2 Nombre de la segunda selección.
     * @param estado Estado inicial del partido.
     */
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

    /**
     * Devuelve el identificador del partido.
     *
     * @return Identificador único del partido.
     */
    public int getIdPartido() {
        return idPartido;
    }

    /**
     * Devuelve la fase del torneo.
     *
     * @return Fase a la que pertenece el partido.
     */
    public String getFase() {
        return fase;
    }

    /**
     * Devuelve la fecha del partido.
     *
     * @return Fecha del partido.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Devuelve la hora del partido.
     *
     * @return Hora del partido.
     */
    public String getHora() {
        return hora;
    }

    /**
     * Devuelve el estadio del partido.
     *
     * @return Estadio donde se disputa.
     */
    public String getEstadio() {
        return estadio;
    }

    /**
     * Devuelve el nombre de la primera selección.
     *
     * @return Nombre de la selección 1.
     */
    public String getSeleccion1() {
        return seleccion1;
    }

    /**
     * Devuelve el nombre de la segunda selección.
     *
     * @return Nombre de la selección 2.
     */
    public String getSeleccion2() {
        return seleccion2;
    }

    /**
     * Devuelve el estado actual del partido.
     *
     * @return Estado del partido (ABIERTO, CERRADO o FINALIZADO).
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del partido.
     *
     * @param estado Nuevo estado del partido.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve la representación del partido en el formato de línea usado en
     * el archivo partidos.txt, con los campos separados por punto y coma.
     *
     * @return Cadena con los datos del partido separados por ";".
     */
    @Override
    public String toString() {
        return idPartido + ";" + fase + ";" + fecha + ";" + hora + ";" + estadio + ";"
                + seleccion1 + ";" + seleccion2 + ";" + estado;
    }
}