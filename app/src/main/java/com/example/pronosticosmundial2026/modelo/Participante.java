package com.example.pronosticosmundial2026.modelo;

/**
 * Representa a un participante de la aplicación, es decir, un usuario que
 * registra pronósticos y acumula puntaje. Implementa {@link Comparable} para
 * permitir el ordenamiento automático en la tabla de posiciones.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class Participante extends Usuario implements Comparable<Participante> {

    /** Puntaje total acumulado por el participante. */
    private int puntajeAcumulado;

    /**
     * Crea un nuevo participante con sus datos y su puntaje acumulado.
     *
     * @param idUsuario Identificador único del participante.
     * @param nombreUsuario Nombre de usuario para el inicio de sesión.
     * @param contrasena Contraseña del participante.
     * @param nombreCompleto Nombre completo del participante.
     * @param puntajeAcumulado Puntaje inicial acumulado del participante.
     */
    public Participante(String idUsuario, String nombreUsuario, String contrasena,
                        String nombreCompleto, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    /**
     * Devuelve el puntaje acumulado del participante.
     *
     * @return Puntaje total acumulado.
     */
    public int getPuntajeAcumulado() { return puntajeAcumulado; }

    /**
     * Establece el puntaje acumulado del participante.
     *
     * @param puntajeAcumulado Nuevo puntaje acumulado.
     */
    public void setPuntajeAcumulado(int puntajeAcumulado) { this.puntajeAcumulado = puntajeAcumulado; }

    /**
     * Devuelve el tipo de usuario.
     *
     * @return La cadena "PARTICIPANTE".
     */
    @Override
    public String getTipoUsuario() { return "PARTICIPANTE"; }

    /**
     * Compara este participante con otro para el ordenamiento de la tabla de
     * posiciones. Ordena de mayor a menor puntaje; si dos participantes tienen
     * el mismo puntaje, se ordenan alfabéticamente por su nombre de usuario.
     *
     * @param otro Participante con el que se compara.
     * @return Valor negativo, cero o positivo según este participante deba ir
     *         antes, en igual posición o después que el otro.
     */
    @Override
    public int compareTo(Participante otro) {
        // Se invierte la comparación (otro vs this) para lograr orden descendente por puntaje
        int cmp = Integer.compare(otro.puntajeAcumulado, this.puntajeAcumulado);
        if (cmp == 0) {
            cmp = this.nombreUsuario.compareToIgnoreCase(otro.nombreUsuario);
        }
        return cmp;
    }
}