package com.example.pronosticosmundial2026;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuAdministradorActivity extends AppCompatActivity {

    private String idUsuario;
    private TextView lblNombreAdministrador;
    private TextView lblTipoUsuario;

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

    public void irAdministrarPartidos(View v) {
        // TODO: habilitar cuando se cree AdministrarPartidosActivity (Punto 6)
        Toast.makeText(this, "Administrar partidos: pendiente", Toast.LENGTH_SHORT).show();
    }

    public void irActualizarPuntajes(View v) {
        // TODO: habilitar cuando se cree ActualizarPuntajesActivity (Punto 7)
        Toast.makeText(this, "Actualizar puntajes: pendiente", Toast.LENGTH_SHORT).show();
    }

    public void salir(View v) {
        finishAffinity();
    }
}