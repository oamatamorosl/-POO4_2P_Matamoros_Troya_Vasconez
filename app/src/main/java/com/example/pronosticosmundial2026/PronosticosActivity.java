package com.example.pronosticosmundial2026;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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

/**
 * Pantalla donde el participante registra sus pronósticos para los partidos de
 * una fase seleccionada. Permite ingresar los goles de cada selección mientras
 * el partido esté ABIERTO y almacena los pronósticos mediante serialización.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class PronosticosActivity extends AppCompatActivity {

    /** Selector de la fase del torneo. */
    private Spinner spFase;

    /** Contenedor donde se agregan dinámicamente las tarjetas de los partidos. */
    private LinearLayout partidosLayout;

    /** Identificador del participante autenticado. */
    private String idUsuario;

    /** Color azul usado en la interfaz. */
    private static final int AZUL = 0xFF1B2A4A;

    /** Color gris usado en la interfaz. */
    private static final int GRIS = 0xFF6B7280;

    /** Color verde usado en la interfaz. */
    private static final int VERDE = 0xFF2E7D32;

    /** Nombres internos de las fases, tal como aparecen en partidos.txt. */
    private final String[] FASES = {
            "FASE_DE_GRUPOS", "DIECISEISAVOS_DE_FINAL", "OCTAVOS_DE_FINAL",
            "CUARTOS_DE_FINAL", "SEMIFINALES", "TERCER_LUGAR", "FINAL"
    };

    /** Nombres visibles de las fases, mostrados al usuario en el Spinner. */
    private final String[] FASES_TEXTO = {
            "Fase de grupos", "Dieciseisavos de final", "Octavos de final",
            "Cuartos de final", "Semifinales", "Tercer lugar", "Final"
    };

    /**
     * Inicializa la pantalla, configura el Spinner de fases y su listener para
     * mostrar los partidos correspondientes, y enlaza el botón de volver.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronosticos);

        idUsuario = getIntent().getStringExtra("idUsuario");
        spFase = findViewById(R.id.spFase);
        partidosLayout = findViewById(R.id.partidosLayout);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.item_spinner, FASES_TEXTO);
        adaptador.setDropDownViewResource(R.layout.item_spinner);
        spFase.setAdapter(adaptador);

        spFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mostrarPartidos(cargarPartidos(FASES[position]), FASES[position]);
                } catch (IOException e) {
                    Toast.makeText(PronosticosActivity.this,
                            "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setAllCaps(false);
        btnVolver.setBackgroundColor(0xFF1B2A4A);
        btnVolver.setOnClickListener(v -> finish());
    }

    /**
     * Convierte una medida en dp a píxeles según la densidad de la pantalla.
     *
     * @param valor Medida en dp.
     * @return Medida equivalente en píxeles.
     */
    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }

    /**
     * Crea un fondo con esquinas redondeadas y borde opcional.
     *
     * @param color Color de relleno.
     * @param radio Radio de las esquinas en dp.
     * @param colorBorde Color del borde, o 0 para no dibujar borde.
     * @return El fondo ({@link GradientDrawable}) configurado.
     */
    private GradientDrawable fondoRedondeado(int color, int radio, int colorBorde) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radio));
        if (colorBorde != 0) d.setStroke(dp(1), colorBorde);
        return d;
    }

    /**
     * Carga los partidos que pertenecen a una fase determinada.
     *
     * @param fase Nombre interno de la fase.
     * @return Lista de partidos de esa fase.
     * @throws IOException Si ocurre un error al leer el archivo de partidos.
     */
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

    /**
     * Carga los pronósticos ya guardados por el participante para una fase.
     *
     * @param fase Nombre interno de la fase.
     * @return Lista de pronósticos guardados, o una lista vacía si no existen.
     */
    private ArrayList<Pronostico> cargarPronosticos(String fase) {
        try {
            ArrayList<Pronostico> lista = (ArrayList<Pronostico>)
                    GestorArchivos.leerObjeto(this, "pronostico_" + idUsuario + "_" + fase + ".dat");
            return lista == null ? new ArrayList<>() : lista;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Construye y muestra dinámicamente una tarjeta por cada partido de la fase,
     * habilitando los campos de goles solo si el partido está ABIERTO y
     * precargando el pronóstico previamente guardado si existe.
     *
     * @param partidos Lista de partidos a mostrar.
     * @param fase Nombre interno de la fase.
     */
    private void mostrarPartidos(List<Partido> partidos, String fase) {
        partidosLayout.removeAllViews();
        ArrayList<Pronostico> guardados = cargarPronosticos(fase);

        for (Partido p : partidos) {
            boolean abierto = p.getEstado().equals("ABIERTO");

            LinearLayout tarjeta = new LinearLayout(this);
            tarjeta.setOrientation(LinearLayout.VERTICAL);
            tarjeta.setPadding(dp(16), dp(14), dp(16), dp(14));
            tarjeta.setBackground(fondoRedondeado(Color.WHITE, 12, 0xFFE3E7EE));
            tarjeta.setElevation(dp(2));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, dp(6), 0, dp(10));
            tarjeta.setLayoutParams(lp);

            // Fila superior: datos del partido + estado
            LinearLayout filaTop = new LinearLayout(this);
            filaTop.setOrientation(LinearLayout.HORIZONTAL);
            filaTop.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvInfo = new TextView(this);
            tvInfo.setText(p.getFecha() + "  ·  " + p.getHora());
            tvInfo.setTextSize(12f);
            tvInfo.setTextColor(GRIS);
            tvInfo.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvEstado = new TextView(this);
            tvEstado.setText(p.getEstado());
            tvEstado.setTextSize(10f);
            tvEstado.setTypeface(null, Typeface.BOLD);
            tvEstado.setPadding(dp(10), dp(4), dp(10), dp(4));

            if (abierto) {
                tvEstado.setTextColor(VERDE);
                tvEstado.setBackground(fondoRedondeado(0xFFE8F5E9, 20, 0));
            } else if (p.getEstado().equals("CERRADO")) {
                tvEstado.setTextColor(0xFFEF6C00);
                tvEstado.setBackground(fondoRedondeado(0xFFFFF3E0, 20, 0));
            } else {
                tvEstado.setTextColor(0xFF546E7A);
                tvEstado.setBackground(fondoRedondeado(0xFFECEFF1, 20, 0));
            }

            filaTop.addView(tvInfo);
            filaTop.addView(tvEstado);

            TextView tvEstadio = new TextView(this);
            tvEstadio.setText(p.getEstadio());
            tvEstadio.setTextSize(11f);
            tvEstadio.setTextColor(GRIS);
            tvEstadio.setPadding(0, dp(2), 0, dp(10));

            // Fila de equipos y marcador
            LinearLayout filaEquipos = new LinearLayout(this);
            filaEquipos.setOrientation(LinearLayout.HORIZONTAL);
            filaEquipos.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvEq1 = new TextView(this);
            tvEq1.setText(p.getSeleccion1());
            tvEq1.setTextSize(15f);
            tvEq1.setTypeface(null, Typeface.BOLD);
            tvEq1.setTextColor(AZUL);
            tvEq1.setGravity(Gravity.CENTER);
            tvEq1.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            EditText etGoles1 = new EditText(this);
            etGoles1.setInputType(InputType.TYPE_CLASS_NUMBER);
            etGoles1.setGravity(Gravity.CENTER);
            etGoles1.setTextSize(18f);
            etGoles1.setTextColor(AZUL);
            etGoles1.setBackground(fondoRedondeado(0xFFF7F8FA, 8, 0xFFD8DDE5));
            etGoles1.setLayoutParams(new LinearLayout.LayoutParams(dp(52), dp(48)));

            TextView tvGuion = new TextView(this);
            tvGuion.setText("–");
            tvGuion.setTextSize(18f);
            tvGuion.setTextColor(GRIS);
            tvGuion.setPadding(dp(8), 0, dp(8), 0);

            EditText etGoles2 = new EditText(this);
            etGoles2.setInputType(InputType.TYPE_CLASS_NUMBER);
            etGoles2.setGravity(Gravity.CENTER);
            etGoles2.setTextSize(18f);
            etGoles2.setTextColor(AZUL);
            etGoles2.setBackground(fondoRedondeado(0xFFF7F8FA, 8, 0xFFD8DDE5));
            etGoles2.setLayoutParams(new LinearLayout.LayoutParams(dp(52), dp(48)));

            TextView tvEq2 = new TextView(this);
            tvEq2.setText(p.getSeleccion2());
            tvEq2.setTextSize(15f);
            tvEq2.setTypeface(null, Typeface.BOLD);
            tvEq2.setTextColor(AZUL);
            tvEq2.setGravity(Gravity.CENTER);
            tvEq2.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            filaEquipos.addView(tvEq1);
            filaEquipos.addView(etGoles1);
            filaEquipos.addView(tvGuion);
            filaEquipos.addView(etGoles2);
            filaEquipos.addView(tvEq2);

            // Precargar pronóstico guardado
            for (Pronostico pr : guardados) {
                if (pr.getIdPartido() == p.getIdPartido()) {
                    etGoles1.setText(String.valueOf(pr.getGolesSeleccion1()));
                    etGoles2.setText(String.valueOf(pr.getGolesSeleccion2()));
                    break;
                }
            }

            etGoles1.setEnabled(abierto);
            etGoles2.setEnabled(abierto);

            tarjeta.addView(filaTop);
            tarjeta.addView(tvEstadio);
            tarjeta.addView(filaEquipos);

            if (abierto) {
                Button btnGuardar = new Button(this);
                btnGuardar.setText("Guardar pronóstico");
                btnGuardar.setTextSize(13f);
                btnGuardar.setAllCaps(false);
                btnGuardar.setTextColor(Color.WHITE);
                btnGuardar.setBackground(fondoRedondeado(0xFF2E7D32, 8, 0));

                LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dp(42));
                lpBtn.setMargins(0, dp(12), 0, 0);
                btnGuardar.setLayoutParams(lpBtn);

                btnGuardar.setOnClickListener(v -> {
                    try {
                        guardarPronostico(p, etGoles1.getText().toString(),
                                etGoles2.getText().toString());
                        Toast.makeText(this, "Pronóstico guardado", Toast.LENGTH_SHORT).show();
                    } catch (DatosIncompletosException | PronosticoFueraDeTiempoException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                tarjeta.addView(btnGuardar);
            } else {
                TextView tvAviso = new TextView(this);
                tvAviso.setText(p.getEstado().equals("CERRADO")
                        ? "Los pronósticos de este partido están cerrados"
                        : "Partido finalizado");
                tvAviso.setTextSize(11f);
                tvAviso.setTextColor(0xFF8D6E63);
                tvAviso.setGravity(Gravity.CENTER);
                tvAviso.setPadding(dp(8), dp(8), dp(8), dp(8));
                tvAviso.setBackground(fondoRedondeado(0xFFFFF8E1, 8, 0));

                LinearLayout.LayoutParams lpAviso = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lpAviso.setMargins(0, dp(12), 0, 0);
                tvAviso.setLayoutParams(lpAviso);

                tarjeta.addView(tvAviso);
            }

            partidosLayout.addView(tarjeta);
        }
    }

    /**
     * Valida y guarda el pronóstico de un participante para un partido. Verifica
     * que el partido esté ABIERTO, que los goles no estén vacíos y que no sean
     * negativos. Si ya existía un pronóstico para ese partido, lo reemplaza en
     * lugar de crear uno nuevo, y guarda la lista mediante serialización.
     *
     * @param p Partido pronosticado.
     * @param g1 Goles ingresados para la selección 1, como texto.
     * @param g2 Goles ingresados para la selección 2, como texto.
     * @throws DatosIncompletosException Si algún dato falta o es inválido.
     * @throws PronosticoFueraDeTiempoException Si el partido no está ABIERTO.
     * @throws IOException Si ocurre un error al leer o escribir el archivo.
     * @throws ClassNotFoundException Si falla la deserialización de los pronósticos.
     */
    private void guardarPronostico(Partido p, String g1, String g2)
            throws DatosIncompletosException, PronosticoFueraDeTiempoException,
            IOException, ClassNotFoundException {

        if (!p.getEstado().equals("ABIERTO")) {
            throw new PronosticoFueraDeTiempoException(
                    "Los pronósticos de este partido están cerrados.");
        }

        if (g1.trim().isEmpty() || g2.trim().isEmpty()) {
            throw new DatosIncompletosException("Ingresa los goles de ambas selecciones.");
        }

        int goles1 = Integer.parseInt(g1.trim());
        int goles2 = Integer.parseInt(g2.trim());

        if (goles1 < 0 || goles2 < 0) {
            throw new DatosIncompletosException("Los goles deben ser mayores o iguales a cero.");
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