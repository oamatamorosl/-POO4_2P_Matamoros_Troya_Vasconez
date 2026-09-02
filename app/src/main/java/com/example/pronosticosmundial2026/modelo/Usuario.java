package com.example.pronosticosmundial2026.modelo;

import java.io.Serializable;

/**
 * Representa a un usuario genérico de la aplicación de pronósticos.
 * Es la clase base abstracta de la que heredan {@link Participante} y
 * {@link Administrador}. Implementa {@link Serializable} para permitir
 * que los objetos que la referencian puedan almacenarse mediante serialización.
 *
 * @author Oscar_Matamoros
 * @version 1.0
 */
public abstract class Usuario implements Serializable {

    /** Identificador único del usuario. */
    protected String idUsuario;

    /** Nombre de usuario utilizado para iniciar sesión. */
    protected String nombreUsuario;

    /** Contraseña del usuario. */
    protected String contrasena;

    /** Nombre completo del usuario. */
    protected String nombreCompleto;

    /**
     * Crea un nuevo usuario con sus datos básicos.
     *
     * @param idUsuario Identificador único del usuario.
     * @param nombreUsuario Nombre de usuario para el inicio de sesión.
     * @param contrasena Contraseña del usuario.
     * @param nombreCompleto Nombre completo del usuario.
     */
    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Devuelve el identificador del usuario.
     *
     * @return Identificador único del usuario.
     */
    public String getIdUsuario() { return idUsuario; }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return Nombre de usuario para el inicio de sesión.
     */
    public String getNombreUsuario() { return nombreUsuario; }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return Contraseña del usuario.
     */
    public String getContrasena() { return contrasena; }

    /**
     * Devuelve el nombre completo del usuario.
     *
     * @return Nombre completo del usuario.
     */
    public String getNombreCompleto() { return nombreCompleto; }

    /**
     * Devuelve el tipo de usuario. Cada subclase concreta define su propio tipo.
     *
     * @return Cadena que identifica el tipo de usuario (por ejemplo, "PARTICIPANTE" o "ADMINISTRADOR").
     */
    public abstract String getTipoUsuario();
}