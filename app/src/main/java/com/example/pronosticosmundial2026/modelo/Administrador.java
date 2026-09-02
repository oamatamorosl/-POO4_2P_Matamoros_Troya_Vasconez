package com.example.pronosticosmundial2026.modelo;

/**
 * Representa a un administrador de la aplicación, es decir, un usuario
 * autorizado para gestionar partidos, registrar resultados y actualizar
 * los puntajes de los participantes.
 *
 * @author Oscar_Matamoros
 * @version 1.0
 */
public class Administrador extends Usuario {

    /** Cargo del administrador (por ejemplo, "Administrador General"). */
    private String cargo;

    /**
     * Crea un nuevo administrador con sus datos y su cargo.
     *
     * @param idUsuario Identificador único del administrador.
     * @param nombreUsuario Nombre de usuario para el inicio de sesión.
     * @param contrasena Contraseña del administrador.
     * @param nombreCompleto Nombre completo del administrador.
     * @param cargo Cargo que desempeña el administrador.
     */
    public Administrador(String idUsuario, String nombreUsuario, String contrasena,
                         String nombreCompleto, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto);
        this.cargo = cargo;
    }

    /**
     * Devuelve el cargo del administrador.
     *
     * @return Cargo del administrador.
     */
    public String getCargo() { return cargo; }

    /**
     * Establece el cargo del administrador.
     *
     * @param cargo Nuevo cargo del administrador.
     */
    public void setCargo(String cargo) { this.cargo = cargo; }

    /**
     * Devuelve el tipo de usuario.
     *
     * @return La cadena "ADMINISTRADOR".
     */
    @Override
    public String getTipoUsuario() { return "ADMINISTRADOR"; }
}