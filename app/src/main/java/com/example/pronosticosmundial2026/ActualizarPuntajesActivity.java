package com.example.pronosticosmundial2026;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class ActualizarPuntajesActivity extends AppCompatActivity {

    // ---- Paleta ----
    private static final int AZUL = 0xFF1B2A4A;
    private static final int GRIS = 0xFF6B7280;
    private static final int VERDE = 0xFF2E7D32;
    private static final int VERDE_CLARO_FONDO = 0xFFE8F5E9;
    private static final int VERDE_MEDIO = 0xFF43A047;
    private static final int VERDE_TENUE = 0xFF66BB6A;
    private static final int AZUL_INFO_FONDO = 0xFFE8F0FE;
    private static final int AZUL_INFO_TEXTO = 0xFF1A56DB;
    private static final int GRIS_FONDO = 0xFFF3F4F6;
    private static final int BLANCO = 0xFFFFFFFF;

    private final String[] FASES = {
            "FASE_DE_GRUPOS", "DIECISEISAVOS_DE_FINAL", "OCTAVOS_DE_FINAL",
            "CUARTOS_DE_FINAL", "SEMIFINALES", "TERCER_LUGAR", "FINAL"
    };

    private TextView lblEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout pantalla = new LinearLayout(this);
        pantalla.setOrientation(LinearLayout.VERTICAL);
        pantalla.setBackgroundColor(GRIS_FONDO);



        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout contenido = new LinearLayout(this);
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setPadding(dp(16), dp(16), dp(16), dp(16));

        contenido.addView(crearBannerInfo());
        contenido.addView(crearTarjetaPrincipal());
        contenido.addView(espacio(14));
        contenido.addView(crearBannerReinicio());

        scroll.addView(contenido);
        pantalla.addView(scroll);

        pantalla.addView(crearBarraInferior());

        setContentView(pantalla);
    }

    // ============================================================
    //  CONSTRUCCIÓN DE LA UI
    // ============================================================



    private LinearLayout crearBannerInfo() {
        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        banner.setBackground(fondoRedondeado(AZUL_INFO_FONDO, 10, 0));
        banner.setPadding(dp(14), dp(12), dp(14), dp(12));

        TextView icono = new TextView(this);
        icono.setText("\u2139\uFE0F");
        icono.setTextSize(16f);
        icono.setPadding(0, 0, dp(10), 0);
        banner.addView(icono);

        TextView texto = new TextView(this);
        texto.setText("Esta opción calcula los puntos de todos los participantes " +
                "tomando en cuenta los resultados oficiales de los partidos finalizados.");
        texto.setTextSize(12.5f);
        texto.setTextColor(AZUL_INFO_TEXTO);
        texto.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        banner.addView(texto);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(14));
        banner.setLayoutParams(lp);

        return banner;
    }

    private LinearLayout crearTarjetaPrincipal() {
        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setBackground(fondoRedondeado(BLANCO, 16, 0xFFE3E7EE));
        tarjeta.setPadding(dp(20), dp(24), dp(20), dp(22));

        LinearLayout circulo = new LinearLayout(this);
        circulo.setGravity(Gravity.CENTER);
        circulo.setBackground(fondoRedondeado(VERDE_CLARO_FONDO, 40, 0));
        LinearLayout.LayoutParams lpCirculo = new LinearLayout.LayoutParams(dp(72), dp(72));
        lpCirculo.gravity = Gravity.CENTER_HORIZONTAL;
        lpCirculo.bottomMargin = dp(14);
        circulo.setLayoutParams(lpCirculo);
        TextView estrella = new TextView(this);
        estrella.setText("\u2B50");
        estrella.setTextSize(30f);
        circulo.addView(estrella);
        tarjeta.addView(circulo);

        TextView titulo = new TextView(this);
        titulo.setText("Actualizar puntajes");
        titulo.setTextSize(19f);
        titulo.setTypeface(null, Typeface.BOLD);
        titulo.setTextColor(AZUL);
        titulo.setGravity(Gravity.CENTER);
        tarjeta.addView(titulo);

        TextView descripcion = new TextView(this);
        descripcion.setText("Al seleccionar el botón, el sistema recorrerá todos los pronósticos " +
                "y actualizará el puntaje acumulado de cada participante según los resultados oficiales.");
        descripcion.setTextSize(12.5f);
        descripcion.setTextColor(GRIS);
        descripcion.setGravity(Gravity.CENTER);
        descripcion.setPadding(dp(4), dp(8), dp(4), dp(18));
        tarjeta.addView(descripcion);

        TextView subtitulo = new TextView(this);
        subtitulo.setText("Reglas de puntuación por partido");
        subtitulo.setTextSize(13.5f);
        subtitulo.setTypeface(null, Typeface.BOLD);
        subtitulo.setTextColor(AZUL);
        subtitulo.setPadding(0, 0, 0, dp(10));
        tarjeta.addView(subtitulo);

        tarjeta.addView(filaRegla("3 puntos", "Acierta el marcador exacto.", VERDE));
        tarjeta.addView(filaRegla("2 puntos", "Acierta el ganador y la diferencia de goles.", VERDE_MEDIO));
        tarjeta.addView(filaRegla("2 puntos", "Acierta un empate (sin importar el marcador exacto).", VERDE_MEDIO));
        tarjeta.addView(filaRegla("1 punto", "Acierta únicamente el ganador del partido.", VERDE_TENUE));
        tarjeta.addView(filaRegla("0 puntos", "Cualquier otro caso.", GRIS));

        LinearLayout boton = new LinearLayout(this);
        boton.setOrientation(LinearLayout.HORIZONTAL);
        boton.setGravity(Gravity.CENTER);
        boton.setBackground(fondoRedondeado(VERDE, 28, 0));
        boton.setClickable(true);
        boton.setFocusable(true);
        LinearLayout.LayoutParams lpBoton = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50));
        lpBoton.setMargins(0, dp(20), 0, 0);
        boton.setLayoutParams(lpBoton);

        TextView icBoton = new TextView(this);
        icBoton.setText("\u21BB");
        icBoton.setTextSize(17f);
        icBoton.setTextColor(BLANCO);
        icBoton.setPadding(0, 0, dp(8), 0);
        boton.addView(icBoton);

        TextView txtBoton = new TextView(this);
        txtBoton.setText("Actualizar puntajes");
        txtBoton.setTextSize(15f);
        txtBoton.setTypeface(null, Typeface.BOLD);
        txtBoton.setTextColor(BLANCO);
        boton.addView(txtBoton);

        boton.setOnClickListener(this::actualizarPuntajes);
        tarjeta.addView(boton);

        lblEstado = new TextView(this);
        lblEstado.setTextSize(12f);
        lblEstado.setTextColor(VERDE);
        lblEstado.setGravity(Gravity.CENTER);
        lblEstado.setPadding(0, dp(10), 0, 0);
        tarjeta.addView(lblEstado);

        LinearLayout.LayoutParams lpTarjeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tarjeta.setLayoutParams(lpTarjeta);

        return tarjeta;
    }

    private LinearLayout filaRegla(String puntos, String descripcion, int colorPill) {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lpFila = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpFila.setMargins(0, 0, 0, dp(8));
        fila.setLayoutParams(lpFila);

        TextView pill = new TextView(this);
        pill.setText(puntos);
        pill.setTextSize(11.5f);
        pill.setTypeface(null, Typeface.BOLD);
        pill.setTextColor(BLANCO);
        pill.setGravity(Gravity.CENTER);
        pill.setBackground(fondoRedondeado(colorPill, 14, 0));
        pill.setPadding(dp(10), dp(5), dp(10), dp(5));
        LinearLayout.LayoutParams lpPill = new LinearLayout.LayoutParams(dp(74), LinearLayout.LayoutParams.WRAP_CONTENT);
        lpPill.setMargins(0, 0, dp(10), 0);
        pill.setLayoutParams(lpPill);
        fila.addView(pill);

        TextView texto = new TextView(this);
        texto.setText(descripcion);
        texto.setTextSize(12.5f);
        texto.setTextColor(0xFF37474F);
        texto.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        fila.addView(texto);

        return fila;
    }

    private LinearLayout crearBannerReinicio() {
        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        banner.setBackground(fondoRedondeado(0xFFECEFF1, 10, 0));
        banner.setPadding(dp(14), dp(12), dp(14), dp(12));

        TextView icono = new TextView(this);
        icono.setText("\uD83D\uDC65");
        icono.setTextSize(16f);
        icono.setPadding(0, 0, dp(10), 0);
        banner.addView(icono);

        TextView texto = new TextView(this);
        texto.setText("Al ejecutar esta opción, los puntajes actuales de todos los participantes " +
                "se reiniciarán a cero y se recalcularán según los resultados oficiales registrados.");
        texto.setTextSize(12f);
        texto.setTextColor(0xFF546E7A);
        texto.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        banner.addView(texto);

        return banner;
    }

    private LinearLayout crearBarraInferior() {
        LinearLayout barra = new LinearLayout(this);
        barra.setOrientation(LinearLayout.HORIZONTAL);
        barra.setGravity(Gravity.CENTER_VERTICAL);
        barra.setBackgroundColor(AZUL);
        barra.setPadding(dp(18), dp(16), dp(18), dp(16));
        barra.setClickable(true);
        barra.setFocusable(true);

        TextView flecha = new TextView(this);
        flecha.setText("\u2190");
        flecha.setTextSize(16f);
        flecha.setTextColor(BLANCO);
        flecha.setPadding(0, 0, dp(10), 0);
        barra.addView(flecha);

        TextView texto = new TextView(this);
        texto.setText("Volver al menú principal");
        texto.setTextSize(14f);
        texto.setTextColor(BLANCO);
        barra.addView(texto);

        barra.setOnClickListener(this::volver);
        return barra;
    }

    private View espacio(int alturaDp) {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(alturaDp)));
        return v;
    }

    // ============================================================
    //  UTILIDADES DE ESTILO
    // ============================================================

    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable fondoRedondeado(int color, int radio, int colorBorde) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radio));
        if (colorBorde != 0) d.setStroke(dp(1), colorBorde);
        return d;
    }

    // ============================================================
    //  ACCIONES
    // ============================================================

    public void actualizarPuntajes(View v) {
        try {
            int total = recalcularPuntajes();
            lblEstado.setText("Puntajes actualizados correctamente (" + total + " participantes).");
            Toast.makeText(this, "Puntajes actualizados correctamente.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
        }
    }

    public void volver(View v) {
        finish();
    }

    // ============================================================
    //  LÓGICA DE NEGOCIO (sin cambios respecto a la versión anterior)
    // ============================================================

    private int recalcularPuntajes() throws IOException {
        Map<Integer, Partido> partidosPorId = cargarPartidos();
        Map<Integer, Resultado> resultadosPorId = cargarResultados();

        Map<String, Integer> puntajePorParticipante = new HashMap<>();
        for (String idUsuario : cargarIdsParticipantes()) {
            puntajePorParticipante.put(idUsuario, 0);
        }

        for (String idUsuario : puntajePorParticipante.keySet()) {
            for (String fase : FASES) {
                String nombreArchivo = "pronostico_" + idUsuario + "_" + fase + ".dat";

                ArrayList<Pronostico> pronosticos = leerPronosticos(nombreArchivo);
                if (pronosticos == null || pronosticos.isEmpty()) continue;

                boolean modificado = false;
                int sumaFase = 0;

                for (Pronostico pr : pronosticos) {
                    Partido partido = partidosPorId.get(pr.getIdPartido());
                    int puntos = 0;

                    if (partido != null && "FINALIZADO".equals(partido.getEstado())) {
                        Resultado resultado = resultadosPorId.get(pr.getIdPartido());
                        if (resultado != null) {
                            puntos = calcularPuntos(
                                    pr.getGolesSeleccion1(), pr.getGolesSeleccion2(),
                                    resultado.getGolesSeleccion1(), resultado.getGolesSeleccion2());
                        }
                    }

                    if (pr.getPuntosObtenidos() != puntos) {
                        pr.setPuntosObtenidos(puntos);
                        modificado = true;
                    }
                    sumaFase += puntos;
                }

                if (modificado) {
                    GestorArchivos.guardarObjeto(this, nombreArchivo, pronosticos);
                }

                puntajePorParticipante.put(idUsuario,
                        puntajePorParticipante.get(idUsuario) + sumaFase);
            }
        }

        guardarParticipantes(puntajePorParticipante);
        return puntajePorParticipante.size();
    }

    private int calcularPuntos(int predGoles1, int predGoles2, int realGoles1, int realGoles2) {
        if (predGoles1 == realGoles1 && predGoles2 == realGoles2) {
            return 3;
        }

        boolean predEmpate = predGoles1 == predGoles2;
        boolean realEmpate = realGoles1 == realGoles2;

        if (predEmpate && realEmpate) {
            return 2;
        }

        if (predEmpate != realEmpate) {
            return 0;
        }

        boolean ganaSeleccion1Pred = predGoles1 > predGoles2;
        boolean ganaSeleccion1Real = realGoles1 > realGoles2;

        if (ganaSeleccion1Pred != ganaSeleccion1Real) {
            return 0;
        }

        int diferenciaPred = Math.abs(predGoles1 - predGoles2);
        int diferenciaReal = Math.abs(realGoles1 - realGoles2);

        if (diferenciaPred == diferenciaReal) {
            return 2;
        }

        return 1;
    }

    private Map<Integer, Partido> cargarPartidos() throws IOException {
        Map<Integer, Partido> mapa = new HashMap<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "partidos.txt")) {
            reader.readLine();
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");
                Partido p = new Partido(Integer.parseInt(d[0]), d[1], d[2], d[3],
                        d[4], d[5], d[6], d[7]);
                mapa.put(p.getIdPartido(), p);
            }
        }
        return mapa;
    }

    private Map<Integer, Resultado> cargarResultados() throws IOException {
        Map<Integer, Resultado> mapa = new HashMap<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "resultados.txt")) {
            reader.readLine();
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");
                Resultado r = new Resultado(Integer.parseInt(d[0]), Integer.parseInt(d[1]),
                        Integer.parseInt(d[2]), Integer.parseInt(d[3]));
                mapa.put(r.getIdPartido(), r);
            }
        }
        return mapa;
    }

    private List<String> cargarIdsParticipantes() throws IOException {
        List<String> ids = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "participantes.txt")) {
            reader.readLine();
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");
                ids.add(d[0]);
            }
        }
        return ids;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Pronostico> leerPronosticos(String nombreArchivo) {
        try {
            Object obj = GestorArchivos.leerObjeto(this, nombreArchivo);
            return (ArrayList<Pronostico>) obj;
        } catch (Exception e) {
            return null;
        }
    }

    private void guardarParticipantes(Map<String, Integer> puntajePorParticipante) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("idUsuario;puntajeAcumulado");
        for (Map.Entry<String, Integer> entry : puntajePorParticipante.entrySet()) {
            lineas.add(entry.getKey() + ";" + entry.getValue());
        }
        GestorArchivos.escribirInterno(this, "participantes.txt", lineas);
    }
}