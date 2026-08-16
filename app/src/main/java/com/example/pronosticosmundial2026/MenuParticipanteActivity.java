package com.example.pronosticosmundial2026;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuParticipanteActivity extends AppCompatActivity {

    private String idUsuario;
    private TextView lblNombreParticipante;
    private TextView lblTipoUsuario;

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

    public void irTablaPosiciones(View v) {
        Intent intent = new Intent(this, TablaPosicionesActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("nombreCompleto", getIntent().getStringExtra("nombreCompleto"));
        startActivity(intent);
    }

    public void irPronosticos(View v) {
        // TODO: habilitar cuando se cree PronosticosActivity (Punto 4)
        Toast.makeText(this, "Pronósticos: pendiente (Punto 4)", Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(this, PronosticosActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
        */
    }

    public void irMisPronosticos(View v) {
        // TODO: habilitar cuando se cree MisPronosticosActivity (Punto 5)
        Toast.makeText(this, "Mis pronósticos: pendiente (Punto 5)", Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(this, MisPronosticosActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
        */
    }

    public void salir(View v) {
        finishAffinity();
    }
}