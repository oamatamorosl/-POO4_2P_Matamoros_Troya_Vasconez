package com.example.pronosticosmundial2026;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pronosticosmundial2026.excepciones.DatosIncompletosException;
import com.example.pronosticosmundial2026.excepciones.PronosticoFueraDeTiempoException;
import com.example.pronosticosmundial2026.modelo.Partido;
import com.example.pronosticosmundial2026.modelo.Pronostico;
import com.example.pronosticosmundial2026.utilidades.GestorArchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PronosticosActivity extends AppCompatActivity {

    private Spinner spFase;
    private LinearLayout partidosLayout;
    private String idUsuario;

    private final String[] FASES = {
            "FASE_DE_GRUPOS", "DIECISEISAVOS_DE_FINAL", "OCTAVOS_DE_FINAL",
            "CUARTOS_DE_FINAL", "SEMIFINALES", "TERCER_LUGAR", "FINAL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronosticos);

        idUsuario = getIntent().getStringExtra("idUsuario");
        spFase = findViewById(R.id.spFase);
        partidosLayout = findViewById(R.id.partidosLayout);

        spFase.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, FASES));


        spFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mostrarPartidos(cargarPartidos(FASES[position]));
                } catch (IOException e) {
                    Toast.makeText(PronosticosActivity.this,
                            "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }


    private List<Partido> cargarPartidos(String fase) throws IOException {
        List<Partido> lista = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "partidos.txt")) {
            String linea = reader.readLine();
            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";");
                if (d[1].equals(fase)) {
                    lista.add(new Partido(Integer.parseInt(d[0]), d[1], d[2], d[3],
                            d[4], d[5], d[6], d[7]));
                }
            }
        }
        return lista;
    }



    private void mostrarPartidos(List<Partido> partidos) {
        partidosLayout.removeAllViews();

        for (Partido p : partidos) {
            LinearLayout tarjeta = new LinearLayout(this);
            tarjeta.setOrientation(LinearLayout.VERTICAL);
            tarjeta.setPadding(24, 24, 24, 24);
            tarjeta.setBackgroundColor(0xFFFFFFFF);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 24);
            tarjeta.setLayoutParams(lp);

            TextView tvInfo = new TextView(this);
            tvInfo.setText(p.getFecha() + "   " + p.getHora() + "   " + p.getEstadio());
            tvInfo.setTextSize(12f);
            tvInfo.setTextColor(0xFF666666);

            TextView tvEquipos = new TextView(this);
            tvEquipos.setText(p.getSeleccion1() + "  vs  " + p.getSeleccion2());
            tvEquipos.setTextSize(18f);
            tvEquipos.setTextColor(0xFF1B2A4A);
            tvEquipos.setPadding(0, 12, 0, 12);

            TextView tvEstado = new TextView(this);
            tvEstado.setText(p.getEstado());
            tvEstado.setTextSize(12f);


            LinearLayout filaGoles = new LinearLayout(this);
            filaGoles.setOrientation(LinearLayout.HORIZONTAL);

            EditText etGoles1 = new EditText(this);
            etGoles1.setInputType(InputType.TYPE_CLASS_NUMBER);
            etGoles1.setWidth(150);

            TextView tvGuion = new TextView(this);
            tvGuion.setText("  -  ");
            tvGuion.setTextSize(18f);

            EditText etGoles2 = new EditText(this);
            etGoles2.setInputType(InputType.TYPE_CLASS_NUMBER);
            etGoles2.setWidth(150);

            filaGoles.addView(etGoles1);
            filaGoles.addView(tvGuion);
            filaGoles.addView(etGoles2);


            try {
                ArrayList<Pronostico> guardados = (ArrayList<Pronostico>)
                        GestorArchivos.leerObjeto(this, "pronostico_" + idUsuario + "_" + p.getFase() + ".dat");
                if (guardados != null) {
                    for (Pronostico pr : guardados) {
                        if (pr.getIdPartido() == p.getIdPartido()) {
                            etGoles1.setText(String.valueOf(pr.getGolesSeleccion1()));
                            etGoles2.setText(String.valueOf(pr.getGolesSeleccion2()));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // sin pronósticos previos
            }



            Button btnGuardar = new Button(this);
            btnGuardar.setText("Guardar pronóstico");

            boolean abierto = p.getEstado().equals("ABIERTO");
            etGoles1.setEnabled(abierto);
            etGoles2.setEnabled(abierto);
            btnGuardar.setEnabled(abierto);

            btnGuardar.setOnClickListener(v -> {
                try {
                    guardarPronostico(p, etGoles1.getText().toString(), etGoles2.getText().toString());
                    Toast.makeText(this, "Pronóstico guardado", Toast.LENGTH_SHORT).show();
                } catch (DatosIncompletosException | PronosticoFueraDeTiempoException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
                }
            });



            tarjeta.addView(tvInfo);
            tarjeta.addView(tvEquipos);
            tarjeta.addView(tvEstado);
            tarjeta.addView(filaGoles);
            tarjeta.addView(btnGuardar);
            partidosLayout.addView(tarjeta);
        }
    }


    private void guardarPronostico(Partido p, String g1, String g2)
            throws DatosIncompletosException, PronosticoFueraDeTiempoException,
            IOException, ClassNotFoundException {

        if (!p.getEstado().equals("ABIERTO")) {
            throw new PronosticoFueraDeTiempoException(
                    "Los pronósticos de este partido están cerrados.");
        }

        if (g1.trim().isEmpty() || g2.trim().isEmpty()) {
            throw new DatosIncompletosException(
                    "Ingresa los goles de ambas selecciones.");
        }

        int goles1 = Integer.parseInt(g1.trim());
        int goles2 = Integer.parseInt(g2.trim());

        if (goles1 < 0 || goles2 < 0) {
            throw new DatosIncompletosException("Los goles deben ser números mayores o iguales a cero.");
        }

        String nombreArchivo = "pronostico_" + idUsuario + "_" + p.getFase() + ".dat";

        ArrayList<Pronostico> lista = (ArrayList<Pronostico>)
                GestorArchivos.leerObjeto(this, nombreArchivo);
        if (lista == null) lista = new ArrayList<>();

        boolean reemplazado = false;
        for (Pronostico pr : lista) {
            if (pr.getIdPartido() == p.getIdPartido()) {
                pr.setGolesSeleccion1(goles1);
                pr.setGolesSeleccion2(goles2);
                reemplazado = true;
                break;
            }
        }

        if (!reemplazado) {
            lista.add(new Pronostico(lista.size() + 1, idUsuario, p.getIdPartido(), goles1, goles2));
        }

        GestorArchivos.guardarObjeto(this, nombreArchivo, lista);
    }




}