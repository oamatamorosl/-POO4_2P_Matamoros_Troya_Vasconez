package com.example.pronosticosmundial2026;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class TablaPosicionesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_posiciones);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        try {
            List<Participante> lista = cargarParticipantes();
            mostrarTabla(lista);
        } catch (IOException e) {
            Toast.makeText(this, "Error al leer los archivos", Toast.LENGTH_SHORT).show();
        }

    }

    private List<Participante> cargarParticipantes() throws IOException {
        List<Participante> lista = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeAssets(this, "usuarios.txt")) {
            String linea = reader.readLine();
            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";");
                if (d[4].equals("PARTICIPANTE")) {
                    lista.add(new Participante(d[0], d[1], d[2], d[3], 0));
                }
            }
        }

        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "participantes.txt")) {
            String linea = reader.readLine();
            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";");
                for (Participante p : lista) {
                    if (p.getIdUsuario().equals(d[0])) {
                        p.setPuntajeAcumulado(Integer.parseInt(d[1]));
                    }
                }
            }
        }

        Collections.sort(lista);
        return lista;
    }

    private void mostrarTabla(List<Participante> lista) {
        TableLayout tabla = findViewById(R.id.tablaLayout);

        //Diseño de tabla
        TableRow header = new TableRow(this);
        header.setBackgroundColor(0xFF1B2A4A);

        String[] titulos = {"Pos.", "Participante", "Puntos"};
        float[] pesos = {1f, 3f, 1f};

        for (int i = 0; i < titulos.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(titulos[i]);
            tv.setTextColor(0xFFFFFFFF);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            tv.setPadding(16, 16, 16, 16);
            tv.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, pesos[i]));
            header.addView(tv);
        }
        tabla.addView(header);

        for (int i = 0; i < lista.size(); i++) {
            Participante p = lista.get(i);
            TableRow fila = new TableRow(this);

            TextView tvPos = new TextView(this);
            tvPos.setText(String.valueOf(i + 1));

            tvPos.setPadding(20, 20, 20, 20);
            tvPos.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tvPos.setTextSize(16f);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(p.getNombreCompleto());

            tvNombre.setPadding(20, 20, 20, 20);
            tvNombre.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
            tvNombre.setTextSize(16f);

            TextView tvPuntaje = new TextView(this);
            tvPuntaje.setText(String.valueOf(p.getPuntajeAcumulado()));

            tvPuntaje.setPadding(20, 20, 20, 20);
            tvPuntaje.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tvPuntaje.setTextSize(16f);

            fila.addView(tvPos);
            fila.addView(tvNombre);
            fila.addView(tvPuntaje);
            tabla.addView(fila);
        }
    }


}
