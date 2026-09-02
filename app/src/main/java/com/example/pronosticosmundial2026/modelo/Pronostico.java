package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

/**
 * Representa el pronóstico que un participante registra para un partido,
 * incluyendo los goles pronosticados para cada selección y los puntos
 * obtenidos. Implementa {@link Serializable} porque los pronósticos se
 * almacenan mediante serialización de objetos.
 *
 * @author Sebastian_Vasconez
 * @version 1.0
 */
public class Pronostico implements Serializable {

    /** Identificador único del pronóstico. */
    private int idPronostico;

    /** Identificador del usuario que realizó el pronóstico. */
    private String idUsuario;

    /** Identificador del partido al que corresponde el pronóstico. */
    private int idPartido;

    /** Goles pronosticados para la primera selección. */
    private int golesSeleccion1;

    /** Goles pronosticados para la segunda selección. */
    private int golesSeleccion2;

    /** Puntos obtenidos por este pronóstico tras la actualización de puntajes. */
    private int puntosObtenidos;

    /**
     * Crea un nuevo pronóstico con los goles pronosticados. Los puntos
     * obtenidos se inicializan en cero.
     *
     * @param idPronostico Identificador único del pronóstico.
     * @param idUsuario Identificador del participante que lo realiza.
     * @param idPartido Identificador del partido pronosticado.
     * @param golesSeleccion1 Goles pronosticados para la selección 1.
     * @param golesSeleccion2 Goles pronosticados para la selección 2.
     */
    public Pronostico(int idPronostico, String idUsuario, int idPartido,
                      int golesSeleccion1, int golesSeleccion2) {
        this.idPronostico = idPronostico;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
        this.puntosObtenidos = 0;
    }

    /**
     * Devuelve el identificador del pronóstico.
     *
     * @return Identificador único del pronóstico.
     */
    public int getIdPronostico() {
        return idPronostico;
    }

    /**
     * Devuelve el identificador del usuario que realizó el pronóstico.
     *
     * @return Identificador del participante.
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Devuelve el identificador del partido pronosticado.
     *
     * @return Identificador del partido.
     */
    public int getIdPartido() {
        return idPartido;
    }

    /**
     * Devuelve los goles pronosticados para la primera selección.
     *
     * @return Goles pronosticados para la selección 1.
     */
    public int getGolesSeleccion1() {
        return golesSeleccion1;
    }

    /**
     * Devuelve los goles pronosticados para la segunda selección.
     *
     * @return Goles pronosticados para la selección 2.
     */
    public int getGolesSeleccion2() {
        return golesSeleccion2;
    }

    /**
     * Devuelve los puntos obtenidos por el pronóstico.
     *
     * @return Puntos obtenidos.
     */
    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    /**
     * Establece los goles pronosticados para la primera selección.
     *
     * @param golesSeleccion1 Nuevos goles pronosticados para la selección 1.
     */
    public void setGolesSeleccion1(int golesSeleccion1) {
        this.golesSeleccion1 = golesSeleccion1;
    }

    /**
     * Establece los goles pronosticados para la segunda selección.
     *
     * @param golesSeleccion2 Nuevos goles pronosticados para la selección 2.
     */
    public void setGolesSeleccion2(int golesSeleccion2) {
        this.golesSeleccion2 = golesSeleccion2;
    }

    /**
     * Establece los puntos obtenidos por el pronóstico.
     *
     * @param puntosObtenidos Puntos obtenidos tras comparar con el resultado oficial.
     */
    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }
}