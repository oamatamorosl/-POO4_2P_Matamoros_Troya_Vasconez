package com.example.pronosticosmundial2026;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pronosticosmundial2026.modelo.Participante;
import com.example.pronosticosmundial2026.utilidades.GestorArchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablaPosicionesActivity extends AppCompatActivity {

    private TableLayout tablaLayout;
    private TextView lblNombreParticipante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_posiciones);

        tablaLayout = findViewById(R.id.tablaLayout);
        lblNombreParticipante = findViewById(R.id.lblNombreParticipante);

        String nombre = getIntent().getStringExtra("nombreCompleto");
        lblNombreParticipante.setText(nombre);

        try {
            List<Participante> lista = cargarParticipantes();
            Collections.sort(lista);
            mostrarEncabezado();
            mostrarTabla(lista);
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo cargar la tabla de posiciones.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error inesperado al procesar los datos.", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Participante> cargarParticipantes() throws IOException {
        Map<String, String> nombresUsuarioPorId = new HashMap<>();
        Map<String, String> nombresCompletosPorId = new HashMap<>();
        try (BufferedReader r = GestorArchivos.leerDeAssets(this, "usuarios.txt")) {
            String linea = r.readLine(); // salta cabecera
            while ((linea = r.readLine()) != null) {
                String[] d = linea.split(";");
                nombresUsuarioPorId.put(d[0], d[1]);   // idUsuario -> nombreUsuario
                nombresCompletosPorId.put(d[0], d[3]); // idUsuario -> nombreCompleto
            }
        }

        List<Participante> lista = new ArrayList<>();
        try (BufferedReader r = GestorArchivos.leerDeInterno(this, "participantes.txt")) {
            String linea = r.readLine(); // salta cabecera
            while ((linea = r.readLine()) != null) {
                String[] d = linea.split(";");
                String id = d[0];
                int puntaje = Integer.parseInt(d[1]);
                lista.add(new Participante(
                        id,
                        nombresUsuarioPorId.get(id),   // para el orden
                        "",
                        nombresCompletosPorId.get(id), // para mostrar
                        puntaje
                ));
            }
        }
        return lista;
    }

    private void mostrarEncabezado() {
        TableRow fila = new TableRow(this);
        fila.addView(crearCelda("Pos.", true));
        fila.addView(crearCelda("Participante", true));
        fila.addView(crearCelda("Puntos", true));
        tablaLayout.addView(fila);
    }

    private void mostrarTabla(List<Participante> lista) {
        int posicion = 1;
        for (Participante p : lista) {
            TableRow fila = new TableRow(this);
            fila.addView(crearCelda(String.valueOf(posicion), false));
            fila.addView(crearCelda(p.getNombreCompleto(), false));
            fila.addView(crearCelda(String.valueOf(p.getPuntajeAcumulado()), false));
            tablaLayout.addView(fila);
            posicion++;
        }
    }

    private TextView crearCelda(String texto, boolean esEncabezado) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(24, 16, 24, 16);
        if (esEncabezado) tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    public void volver(View v) {
        finish();
    }
}
