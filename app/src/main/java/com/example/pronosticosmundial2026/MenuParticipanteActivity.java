package com.example.pronosticosmundial2026;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Menú principal del participante. Muestra el nombre del participante
 * autenticado y ofrece las opciones de tabla de posiciones, pronósticos,
 * mis pronósticos y salir de la aplicación.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class MenuParticipanteActivity extends AppCompatActivity {

    /** Identificador del participante autenticado. */
    private String idUsuario;

    /** Etiqueta que muestra el nombre del participante. */
    private TextView lblNombreParticipante;

    /** Etiqueta que muestra el tipo de usuario. */
    private TextView lblTipoUsuario;

    /**
     * Inicializa el menú del participante y muestra su nombre a partir de los
     * datos recibidos desde la pantalla de inicio de sesión.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_participante);

        lblNombreParticipante = findViewById(R.id.lblNombreParticipante);
        lblTipoUsuario = findViewById(R.id.lblTipoUsuario);

        idUsuario = getIntent().getStringExtra("idUsuario");
        String nombre = getIntent().getStringExtra("nombreCompleto");
        lblNombreParticipante.setText("Bienvenido, "+"\n" + nombre);
        lblTipoUsuario.setText("Participante");
    }

    /**
     * Abre la pantalla de la tabla de posiciones.
     *
     * @param v Vista que originó el evento.
     */
    public void irTablaPosiciones(View v) {
        Intent intent = new Intent(this, TablaPosicionesActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("nombreCompleto", getIntent().getStringExtra("nombreCompleto"));
        startActivity(intent);
    }

    /**
     * Abre la pantalla de registro de pronósticos.
     *
     * @param v Vista que originó el evento.
     */
    public void irPronosticos(View v) {
        Intent intent = new Intent(this, PronosticosActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }

    /**
     * Abre la pantalla que muestra los pronósticos registrados por el participante.
     *
     * @param v Vista que originó el evento.
     */
    public void irMisPronosticos(View v) {
        Intent intent = new Intent(this, MisPronosticosActivity.class);
        intent.putExtra("idUsuario", idUsuario);
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