package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    protected String idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombreCompleto;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getNombreCompleto() { return nombreCompleto; }
}