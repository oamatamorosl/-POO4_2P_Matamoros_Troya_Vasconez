package com.example.pronosticosmundial2026.modelo;

public class Administrador extends Usuario {
    private String cargo;

    public Administrador(String idUsuario, String nombreUsuario, String contrasena,
                         String nombreCompleto, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto);
        this.cargo = cargo;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    @Override
    public String getTipoUsuario() { return "ADMINISTRADOR"; }
}