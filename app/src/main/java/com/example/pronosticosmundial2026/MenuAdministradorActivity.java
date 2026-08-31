package com.example.pronosticosmundial2026;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Menú principal del administrador. Muestra el nombre del administrador
 * autenticado y ofrece las opciones de administrar partidos, actualizar
 * puntajes y salir de la aplicación.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class MenuAdministradorActivity extends AppCompatActivity {

    /** Identificador del administrador autenticado. */
    private String idUsuario;

    /** Etiqueta que muestra el nombre del administrador. */
    private TextView lblNombreAdministrador;

    /** Etiqueta que muestra el tipo de usuario. */
    private TextView lblTipoUsuario;

    /**
     * Inicializa el menú del administrador y muestra su nombre a partir de los
     * datos recibidos desde la pantalla de inicio de sesión.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_administrador);

        lblNombreAdministrador = findViewById(R.id.lblNombreAdministrador);
        lblTipoUsuario = findViewById(R.id.lblTipoUsuario);

        idUsuario = getIntent().getStringExtra("idUsuario");
        String nombre = getIntent().getStringExtra("nombreCompleto");
        lblNombreAdministrador.setText("Bienvenido, "+"\n" + nombre);
        lblTipoUsuario.setText("Administrador");
    }

    /**
     * Abre la pantalla de administración de partidos.
     *
     * @param v Vista que originó el evento.
     */
    public void irAdministrarPartidos(View v) {
        Intent intent = new Intent(this, AdministrarPartidosActivity.class);
        startActivity(intent);
    }

    /**
     * Abre la pantalla de actualización de puntajes.
     *
     * @param v Vista que originó el evento.
     */
    public void irActualizarPuntajes(View v) {
        Intent intent = new Intent(this, ActualizarPuntajesActivity.class);
        startActivity(intent);
    }

    /**
     * Cierra por completo la aplicación.
     *
     * @param v Vista que originó el evento.
     */
    public void salir(View v) {
        finishAffinity();
    }
}