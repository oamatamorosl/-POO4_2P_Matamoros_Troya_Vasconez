package com.example.pronosticosmundial2026.modelo;

public class Participante extends Usuario implements Comparable<Participante> {
    private int puntajeAcumulado;

    public Participante(String idUsuario, String nombreUsuario, String contrasena,
                        String nombreCompleto, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public int getPuntajeAcumulado() { return puntajeAcumulado; }
    public void setPuntajeAcumulado(int puntajeAcumulado) { this.puntajeAcumulado = puntajeAcumulado; }

    // Ordena de MAYOR a MENOR puntaje; si empatan, alfabético por nombre de usuario
    @Override
    public int compareTo(Participante otro) {
        int cmp = Integer.compare(otro.puntajeAcumulado, this.puntajeAcumulado);
        if (cmp == 0) {
            cmp = this.nombreUsuario.compareToIgnoreCase(otro.nombreUsuario);
        }
        return cmp;
    }
}