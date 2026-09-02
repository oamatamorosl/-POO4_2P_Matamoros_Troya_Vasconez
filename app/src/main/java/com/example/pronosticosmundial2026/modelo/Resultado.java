package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

/**
 * Representa el resultado oficial de un partido finalizado, con los goles
 * de cada selección. Implementa {@link Serializable} para poder almacenarse
 * cuando sea necesario.
 *
 * @author Sebastian_Vasconez
 * @version 1.0
 */
public class Resultado implements Serializable {

    /** Identificador único del resultado. */
    private int idResultado;

    /** Identificador del partido al que corresponde el resultado. */
    private int idPartido;

    /** Goles oficiales de la primera selección. */
    private int golesSeleccion1;

    /** Goles oficiales de la segunda selección. */
    private int golesSeleccion2;

    /**
     * Crea un nuevo resultado oficial de un partido.
     *
     * @param idResultado Identificador único del resultado.
     * @param idPartido Identificador del partido correspondiente.
     * @param golesSeleccion1 Goles oficiales de la selección 1.
     * @param golesSeleccion2 Goles oficiales de la selección 2.
     */
    public Resultado(int idResultado, int idPartido, int golesSeleccion1, int golesSeleccion2) {
        this.idResultado = idResultado;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
    }

    /**
     * Devuelve el identificador del resultado.
     *
     * @return Identificador único del resultado.
     */
    public int getIdResultado() {
        return idResultado;
    }

    /**
     * Devuelve el identificador del partido.
     *
     * @return Identificador del partido correspondiente.
     */
    public int getIdPartido() {
        return idPartido;
    }

    /**
     * Devuelve los goles oficiales de la primera selección.
     *
     * @return Goles de la selección 1.
     */
    public int getGolesSeleccion1() {
        return golesSeleccion1;
    }

    /**
     * Devuelve los goles oficiales de la segunda selección.
     *
     * @return Goles de la selección 2.
     */
    public int getGolesSeleccion2() {
        return golesSeleccion2;
    }

    /**
     * Devuelve la representación del resultado en el formato de línea usado
     * en el archivo resultados.txt, con los campos separados por punto y coma.
     *
     * @return Cadena con los datos del resultado separados por ";".
     */
    @Override
    public String toString() {
        return idResultado + ";" + idPartido + ";" + golesSeleccion1 + ";" + golesSeleccion2;
    }
}