package com.example.pronosticosmundial2026;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pronosticosmundial2026.modelo.Partido;
import com.example.pronosticosmundial2026.modelo.Pronostico;
import com.example.pronosticosmundial2026.modelo.Resultado;
import com.example.pronosticosmundial2026.utilidades.GestorArchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisPronosticosActivity extends AppCompatActivity {

    private LinearLayout pronosticosLayout;
    private String idUsuario;

    private static final int AZUL = 0xFF1B2A4A;
    private static final int GRIS = 0xFF6B7280;
    private static final int VERDE = 0xFF2E7D32;
    private static final int NARANJA = 0xFFEF6C00;

    private final String[] FASES = {
            "FASE_DE_GRUPOS", "DIECISEISAVOS_DE_FINAL", "OCTAVOS_DE_FINAL",
            "CUARTOS_DE_FINAL", "SEMIFINALES", "TERCER_LUGAR", "FINAL"
    };

    private final String[] FASES_TEXTO = {
            "Fase de grupos", "Dieciseisavos de final", "Octavos de final",
            "Cuartos de final", "Semifinales", "Tercer lugar", "Final"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pronosticos);

        idUsuario = getIntent().getStringExtra("idUsuario");
        pronosticosLayout = findViewById(R.id.pronosticosLayout);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setAllCaps(false);
        btnVolver.setBackgroundColor(AZUL);
        btnVolver.setOnClickListener(v -> finish());

        try {
            cargarPronosticos();
        } catch (Exception e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
        }
    }

    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable fondoRedondeado(int color, int radio, int colorBorde) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radio));
        if (colorBorde != 0) {
            d.setStroke(dp(1), colorBorde);
        }
        return d;
    }

    /**
     * Carga los pronósticos del usuario de todas las fases.
     */
    private void cargarPronosticos() throws IOException {
        pronosticosLayout.removeAllViews();
        Map<Integer, Partido> partidos = cargarPartidos();
        Map<Integer, Resultado> resultados = cargarResultados();
        boolean existePronostico = false;

        for (int i = 0; i < FASES.length; i++) {
            String fase = FASES[i];
            ArrayList<Pronostico> lista = cargarPronosticosFase(fase);

            if (!lista.isEmpty()) {
                existePronostico = true;
                mostrarPronosticos(lista, fase, FASES_TEXTO[i], partidos, resultados);
            }
        }

        if (!existePronostico) {
            mostrarMensajeSinPronosticos();
        }
    }

    /**
     * Lee los pronósticos serializados de una fase.
     */
    private ArrayList<Pronostico> cargarPronosticosFase(String fase) {
        String nombreArchivo = "pronostico_" + idUsuario + "_" + fase + ".dat";
        try {
            ArrayList<Pronostico> lista = (ArrayList<Pronostico>) GestorArchivos.leerObjeto(this, nombreArchivo);
            return lista == null ? new ArrayList<>() : lista;
        } catch (Exception e) {
            // El archivo puede no existir todavía.
            return new ArrayList<>();
        }
    }

    /**
     * Carga todos los partidos desde partidos.txt.
     * Se almacenan en un Map usando el id del partido.
     */
    private Map<Integer, Partido> cargarPartidos() throws IOException {
        Map<Integer, Partido> mapa = new HashMap<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "partidos.txt")) {
            String linea;
            // Saltar encabezado
            reader.readLine();
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] d = linea.split(";");
                if (d.length >= 8) {
                    Partido partido = new Partido(Integer.parseInt(d[0]), d[1], d[2], d[3], d[4], d[5], d[6], d[7]);
                    mapa.put(partido.getIdPartido(), partido);
                }
            }
        }
        return mapa;
    }

    /**
     * Carga los resultados oficiales desde resultados.txt.
     */
    private Map<Integer, Resultado> cargarResultados() throws IOException {
        Map<Integer, Resultado> mapa = new HashMap<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "resultados.txt")) {
            String linea;
            // Saltar encabezado
            reader.readLine();
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] d = linea.split(";");
                if (d.length >= 4) {
                    Resultado resultado = new Resultado(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]), Integer.parseInt(d[3]));
                    mapa.put(resultado.getIdPartido(), resultado);
                }
            }
        }
        return mapa;
    }

    /**
     * Muestra los pronósticos correspondientes
     * a una determinada fase.
     */
    private void mostrarPronosticos(List<Pronostico> lista, String fase, String faseTexto, Map<Integer, Partido> partidos, Map<Integer, Resultado> resultados) {
        // Título de la fase
        TextView tvFase = new TextView(this);
        tvFase.setText(faseTexto);
        tvFase.setTextSize(18f);
        tvFase.setTypeface(null, Typeface.BOLD);
        tvFase.setTextColor(AZUL);
        tvFase.setPadding(dp(4), dp(18), dp(4), dp(8));

        pronosticosLayout.addView(tvFase);

        for (Pronostico pronostico : lista) {
            Partido partido = partidos.get(pronostico.getIdPartido());
            if (partido == null) {
                continue;
            }
            Resultado resultado = resultados.get(pronostico.getIdPartido());
            crearTarjetaPronostico(pronostico, partido, resultado);
        }
    }

    /**
     * Crea visualmente una tarjeta para cada pronóstico.
     */
    private void crearTarjetaPronostico(Pronostico pronostico, Partido partido, Resultado resultado) {
        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setPadding(dp(16), dp(14), dp(16), dp(14));
        tarjeta.setBackground(fondoRedondeado(Color.WHITE, 12, 0xFFE3E7EE));
        tarjeta.setElevation(dp(2));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(4), 0, dp(10));
        tarjeta.setLayoutParams(lp);

        // Fecha y estado
        LinearLayout filaSuperior = new LinearLayout(this);
        filaSuperior.setOrientation(LinearLayout.HORIZONTAL);
        filaSuperior.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvFecha = new TextView(this);
        tvFecha.setText(partido.getFecha() + "  ·  " + partido.getHora());
        tvFecha.setTextSize(12f);
        tvFecha.setTextColor(GRIS);
        tvFecha.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvEstado = new TextView(this);
        tvEstado.setText(partido.getEstado());
        tvEstado.setTextSize(10f);
        tvEstado.setTypeface(null, Typeface.BOLD);
        tvEstado.setPadding(dp(10), dp(4), dp(10), dp(4));

        if (partido.getEstado().equals("FINALIZADO")) {
            tvEstado.setTextColor(0xFF546E7A);
            tvEstado.setBackground(fondoRedondeado(0xFFECEFF1, 20, 0));
        } else {
            tvEstado.setTextColor(NARANJA);
            tvEstado.setBackground(fondoRedondeado(0xFFFFF3E0, 20, 0));
        }

        filaSuperior.addView(tvFecha);
        filaSuperior.addView(tvEstado);
        tarjeta.addView(filaSuperior);

        // Estadio
        TextView tvEstadio = new TextView(this);
        tvEstadio.setText(partido.getEstadio());
        tvEstadio.setTextSize(11f);
        tvEstadio.setTextColor(GRIS);
        tvEstadio.setPadding(0, dp(4), 0, dp(10));
        tarjeta.addView(tvEstadio);

        // Equipos
        TextView tvPartido = new TextView(this);
        tvPartido.setText(partido.getSeleccion1() + "  vs  " + partido.getSeleccion2());
        tvPartido.setTextSize(16f);
        tvPartido.setTypeface(null, Typeface.BOLD);
        tvPartido.setTextColor(AZUL);
        tvPartido.setGravity(Gravity.CENTER);
        tvPartido.setPadding(0, dp(4), 0, dp(12));
        tarjeta.addView(tvPartido);

        // Título del pronóstico
        TextView tvTituloPronostico = new TextView(this);
        tvTituloPronostico.setText("Tu pronóstico");
        tvTituloPronostico.setTextSize(12f);
        tvTituloPronostico.setTextColor(GRIS);
        tarjeta.addView(tvTituloPronostico);

        // Marcador pronosticado
        TextView tvPronostico = new TextView(this);
        tvPronostico.setText(partido.getSeleccion1() + "  " + pronostico.getGolesSeleccion1() + "  -  " + pronostico.getGolesSeleccion2() + "  " + partido.getSeleccion2());
        tvPronostico.setTextSize(16f);
        tvPronostico.setTypeface(null, Typeface.BOLD);
        tvPronostico.setTextColor(AZUL);
        tvPronostico.setGravity(Gravity.CENTER);
        tvPronostico.setPadding(0, dp(6), 0, dp(12));
        tarjeta.addView(tvPronostico);

        // Resultado oficial
        TextView tvResultadoTitulo = new TextView(this);
        tvResultadoTitulo.setText("Resultado oficial");
        tvResultadoTitulo.setTextSize(12f);
        tvResultadoTitulo.setTextColor(GRIS);
        tarjeta.addView(tvResultadoTitulo);

        TextView tvResultado = new TextView(this);
        if (resultado != null) {
            tvResultado.setText(partido.getSeleccion1() + "  " + resultado.getGolesSeleccion1() + "  -  " + resultado.getGolesSeleccion2() + "  " + partido.getSeleccion2());
            tvResultado.setTextColor(VERDE);
        } else {
            tvResultado.setText("Resultado pendiente");
            tvResultado.setTextColor(NARANJA);
        }
        tvResultado.setTextSize(14f);
        tvResultado.setTypeface(null, Typeface.BOLD);
        tvResultado.setGravity(Gravity.CENTER);
        tvResultado.setPadding(0, dp(6), 0, dp(12));
        tarjeta.addView(tvResultado);

        // Puntos
        TextView tvPuntos = new TextView(this);
        if (resultado == null) {
            tvPuntos.setText("Puntaje pendiente");
            tvPuntos.setTextColor(NARANJA);
        } else {
            tvPuntos.setText("Puntos obtenidos: " + pronostico.getPuntosObtenidos());
            tvPuntos.setTextColor(VERDE);
        }
        tvPuntos.setTextSize(13f);
        tvPuntos.setTypeface(null, Typeface.BOLD);
        tvPuntos.setGravity(Gravity.CENTER);
        tvPuntos.setPadding(dp(8), dp(8), dp(8), dp(8));
        tvPuntos.setBackground(fondoRedondeado(0xFFF5F7FA, 8, 0));
        tarjeta.addView(tvPuntos);

        pronosticosLayout.addView(tarjeta);
    }

    /**
     * Mensaje cuando el participante todavía
     * no tiene pronósticos registrados.
     */
    private void mostrarMensajeSinPronosticos() {
        TextView tvMensaje = new TextView(this);
        tvMensaje.setText("No tienes pronósticos registrados.");
        tvMensaje.setTextSize(15f);
        tvMensaje.setTextColor(GRIS);
        tvMensaje.setGravity(Gravity.CENTER);
        tvMensaje.setPadding(dp(20), dp(40), dp(20), dp(40));
        pronosticosLayout.addView(tvMensaje);
    }

    public void volver(View view) {
        finish();
    }
}