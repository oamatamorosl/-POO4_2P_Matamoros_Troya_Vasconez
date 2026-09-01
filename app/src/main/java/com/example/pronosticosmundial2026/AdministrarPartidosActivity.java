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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pronosticosmundial2026.excepciones.DatosIncompletosException;
import com.example.pronosticosmundial2026.modelo.Partido;
import com.example.pronosticosmundial2026.modelo.Resultado;
import com.example.pronosticosmundial2026.utilidades.GestorArchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de administración de partidos (opción del administrador). Permite
 * consultar los partidos por fase, cerrar los pronósticos y registrar los
 * resultados oficiales, gestionando las transiciones de estado
 * (ABIERTO, CERRADO, FINALIZADO) y su persistencia en archivos. Toda la
 * interfaz se construye de forma dinámica por código.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class AdministrarPartidosActivity extends AppCompatActivity {

    /** Selector de la fase del torneo. */
    private Spinner spFase;

    /** Contenedor donde se agregan dinámicamente las tarjetas de los partidos. */
    private LinearLayout partidosLayout;

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
            "Cuartos de final", "Semifinales", "Partido por el tercer lugar", "Final"
    };

    /**
     * Inicializa la pantalla construyendo dinámicamente el título, el Spinner
     * de fases, el área desplazable de partidos y el botón de volver. Configura
     * además el listener que refresca los partidos al cambiar de fase.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ---- GUI dinámica: construyo toda la pantalla por código ----
        LinearLayout raiz = new LinearLayout(this);
        raiz.setOrientation(LinearLayout.VERTICAL);
        raiz.setBackgroundColor(0xFFF2F4F7);
        raiz.setPadding(dp(16), dp(16), dp(16), dp(16));

        TextView titulo = new TextView(this);
        titulo.setText("Administrar partidos");
        titulo.setTextSize(20f);
        titulo.setTypeface(null, Typeface.BOLD);
        titulo.setTextColor(AZUL);
        titulo.setPadding(0, 0, 0, dp(12));
        raiz.addView(titulo);

        TextView lblFase = new TextView(this);
        lblFase.setText("Fase del torneo");
        lblFase.setTextSize(13f);
        lblFase.setTextColor(GRIS);
        raiz.addView(lblFase);

        spFase = new Spinner(this);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, FASES_TEXTO);
        spFase.setAdapter(adaptador);
        raiz.addView(spFase);

        ScrollView scroll = new ScrollView(this);
        LinearLayout.LayoutParams lpScroll = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        scroll.setLayoutParams(lpScroll);

        partidosLayout = new LinearLayout(this);
        partidosLayout.setOrientation(LinearLayout.VERTICAL);
        partidosLayout.setPadding(0, dp(10), 0, dp(10));
        scroll.addView(partidosLayout);
        raiz.addView(scroll);

        Button btnVolver = new Button(this);
        btnVolver.setText("Volver al menú principal");
        btnVolver.setAllCaps(false);
        btnVolver.setTextColor(Color.WHITE);
        btnVolver.setBackground(fondoRedondeado(AZUL, 8, 0));
        btnVolver.setOnClickListener(v -> finish());
        raiz.addView(btnVolver);

        setContentView(raiz);

        spFase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refrescar(FASES[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
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
     * Vuelve a cargar y mostrar los partidos de la fase indicada.
     *
     * @param fase Nombre interno de la fase a mostrar.
     */
    private void refrescar(String fase) {
        try {
            mostrarPartidos(cargarPartidos(fase));
        } catch (IOException e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga únicamente los partidos que pertenecen a una fase determinada.
     *
     * @param fase Nombre interno de la fase.
     * @return Lista de partidos de esa fase.
     * @throws IOException Si ocurre un error al leer el archivo de partidos.
     */
    private List<Partido> cargarPartidos(String fase) throws IOException {
        List<Partido> lista = new ArrayList<>();
        for (Partido p : cargarTodos()) {
            if (p.getFase().equals(fase)) lista.add(p);
        }
        return lista;
    }

    /**
     * Carga todos los partidos registrados en partidos.txt. Es necesario cargar
     * la lista completa para poder reescribir el archivo íntegro al cambiar el
     * estado de un solo partido.
     *
     * @return Lista con todos los partidos del torneo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private List<Partido> cargarTodos() throws IOException {
        List<Partido> lista = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "partidos.txt")) {
            reader.readLine(); // salta cabecera
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] d = linea.split(";");
                lista.add(new Partido(Integer.parseInt(d[0]), d[1], d[2], d[3],
                        d[4], d[5], d[6], d[7]));
            }
        }
        return lista;
    }

    /**
     * Construye y muestra dinámicamente una tarjeta por cada partido, con los
     * controles correspondientes a su estado.
     *
     * @param partidos Lista de partidos a mostrar.
     */
    private void mostrarPartidos(List<Partido> partidos) {
        partidosLayout.removeAllViews();

        for (Partido p : partidos) {
            LinearLayout tarjeta = new LinearLayout(this);
            tarjeta.setOrientation(LinearLayout.VERTICAL);
            tarjeta.setPadding(dp(16), dp(14), dp(16), dp(14));
            tarjeta.setBackground(fondoRedondeado(Color.WHITE, 12, 0xFFE3E7EE));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, dp(6), 0, dp(10));
            tarjeta.setLayoutParams(lp);

            // Fila superior: id + estado
            LinearLayout filaTop = new LinearLayout(this);
            filaTop.setOrientation(LinearLayout.HORIZONTAL);
            filaTop.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvInfo = new TextView(this);
            tvInfo.setText("Id " + p.getIdPartido() + "  ·  " + p.getFecha() + "  ·  " + p.getHora());
            tvInfo.setTextSize(12f);
            tvInfo.setTextColor(GRIS);
            tvInfo.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvEstado = new TextView(this);
            tvEstado.setText(p.getEstado());
            tvEstado.setTextSize(10f);
            tvEstado.setTypeface(null, Typeface.BOLD);
            tvEstado.setPadding(dp(10), dp(4), dp(10), dp(4));
            pintarEstado(tvEstado, p.getEstado());

            filaTop.addView(tvInfo);
            filaTop.addView(tvEstado);

            TextView tvEstadio = new TextView(this);
            tvEstadio.setText(p.getEstadio());
            tvEstadio.setTextSize(11f);
            tvEstadio.setTextColor(GRIS);
            tvEstadio.setPadding(0, dp(2), 0, dp(8));

            TextView tvEquipos = new TextView(this);
            tvEquipos.setText(p.getSeleccion1() + "   vs   " + p.getSeleccion2());
            tvEquipos.setTextSize(15f);
            tvEquipos.setTypeface(null, Typeface.BOLD);
            tvEquipos.setTextColor(AZUL);
            tvEquipos.setGravity(Gravity.CENTER);
            tvEquipos.setPadding(0, dp(4), 0, dp(4));

            tarjeta.addView(filaTop);
            tarjeta.addView(tvEstadio);
            tarjeta.addView(tvEquipos);

            // Controles según estado
            switch (p.getEstado()) {
                case "ABIERTO":
                    agregarControlesAbierto(tarjeta, p);
                    break;
                case "CERRADO":
                    agregarControlesCerrado(tarjeta, p);
                    break;
                default: // FINALIZADO
                    agregarControlesFinalizado(tarjeta, p);
                    break;
            }

            partidosLayout.addView(tarjeta);
        }
    }

    /**
     * Aplica color y fondo a la etiqueta de estado según el estado del partido.
     *
     * @param tv Etiqueta a la que se aplica el estilo.
     * @param estado Estado del partido (ABIERTO, CERRADO o FINALIZADO).
     */
    private void pintarEstado(TextView tv, String estado) {
        if (estado.equals("ABIERTO")) {
            tv.setTextColor(VERDE);
            tv.setBackground(fondoRedondeado(0xFFE8F5E9, 20, 0));
        } else if (estado.equals("CERRADO")) {
            tv.setTextColor(0xFFEF6C00);
            tv.setBackground(fondoRedondeado(0xFFFFF3E0, 20, 0));
        } else {
            tv.setTextColor(0xFF546E7A);
            tv.setBackground(fondoRedondeado(0xFFECEFF1, 20, 0));
        }
    }

    /**
     * Agrega a la tarjeta los controles de un partido en estado ABIERTO:
     * un aviso y el botón para cerrar los pronósticos.
     *
     * @param tarjeta Tarjeta del partido.
     * @param p Partido correspondiente.
     */
    private void agregarControlesAbierto(LinearLayout tarjeta, Partido p) {
        TextView aviso = new TextView(this);
        aviso.setText("Los participantes pueden registrar o modificar sus pronósticos.");
        aviso.setTextSize(11f);
        aviso.setTextColor(GRIS);
        aviso.setPadding(0, dp(6), 0, dp(6));
        tarjeta.addView(aviso);

        Button btn = new Button(this);
        btn.setText("Cerrar pronósticos");
        btn.setAllCaps(false);
        btn.setTextColor(Color.WHITE);
        btn.setBackground(fondoRedondeado(0xFFEF6C00, 8, 0));
        btn.setLayoutParams(btnParams());
        btn.setOnClickListener(v -> {
            cambiarEstado(p, "CERRADO");
            Toast.makeText(this, "Pronósticos cerrados", Toast.LENGTH_SHORT).show();
            refrescar(p.getFase());
        });
        tarjeta.addView(btn);
    }

    /**
     * Agrega a la tarjeta los controles de un partido en estado CERRADO:
     * un aviso, los campos de goles (inicialmente deshabilitados), el botón
     * para registrar el resultado (que los habilita) y el botón para guardarlo.
     *
     * @param tarjeta Tarjeta del partido.
     * @param p Partido correspondiente.
     */
    private void agregarControlesCerrado(LinearLayout tarjeta, Partido p) {
        TextView aviso = new TextView(this);
        aviso.setText("Los pronósticos están cerrados. Registre el resultado oficial cuando el partido haya finalizado.");
        aviso.setTextSize(11f);
        aviso.setTextColor(0xFFEF6C00);
        aviso.setPadding(0, dp(6), 0, dp(8));
        tarjeta.addView(aviso);

        // Fila de goles (inicialmente deshabilitada)
        LinearLayout filaGoles = new LinearLayout(this);
        filaGoles.setOrientation(LinearLayout.HORIZONTAL);
        filaGoles.setGravity(Gravity.CENTER);

        EditText etG1 = campoGoles();
        TextView guion = new TextView(this);
        guion.setText("–");
        guion.setTextSize(18f);
        guion.setTextColor(GRIS);
        guion.setPadding(dp(10), 0, dp(10), 0);
        EditText etG2 = campoGoles();

        etG1.setEnabled(false);
        etG2.setEnabled(false);

        filaGoles.addView(etG1);
        filaGoles.addView(guion);
        filaGoles.addView(etG2);
        tarjeta.addView(filaGoles);

        Button btnRegistrar = new Button(this);
        btnRegistrar.setText("Registrar resultado");
        btnRegistrar.setAllCaps(false);
        btnRegistrar.setTextColor(Color.WHITE);
        btnRegistrar.setBackground(fondoRedondeado(AZUL, 8, 0));
        btnRegistrar.setLayoutParams(btnParams());

        Button btnGuardar = new Button(this);
        btnGuardar.setText("Guardar resultado");
        btnGuardar.setAllCaps(false);
        btnGuardar.setTextColor(Color.WHITE);
        btnGuardar.setBackground(fondoRedondeado(VERDE, 8, 0));
        btnGuardar.setLayoutParams(btnParams());
        btnGuardar.setVisibility(View.GONE); // aparece al registrar

        btnRegistrar.setOnClickListener(v -> {
            etG1.setEnabled(true);
            etG2.setEnabled(true);
            btnGuardar.setVisibility(View.VISIBLE);
        });

        btnGuardar.setOnClickListener(v -> {
            try {
                guardarResultado(p, etG1.getText().toString(), etG2.getText().toString());
                Toast.makeText(this, "Resultado guardado. Partido finalizado.",
                        Toast.LENGTH_SHORT).show();
                refrescar(p.getFase());
            } catch (DatosIncompletosException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        tarjeta.addView(btnRegistrar);
        tarjeta.addView(btnGuardar);
    }

    /**
     * Agrega a la tarjeta los controles de un partido en estado FINALIZADO:
     * muestra el resultado oficial y un aviso de que ya no puede modificarse.
     *
     * @param tarjeta Tarjeta del partido.
     * @param p Partido correspondiente.
     */
    private void agregarControlesFinalizado(LinearLayout tarjeta, Partido p) {
        Resultado r = buscarResultado(p.getIdPartido());

        TextView tvResultado = new TextView(this);
        if (r != null) {
            tvResultado.setText("Resultado oficial:  " + r.getGolesSeleccion1()
                    + " - " + r.getGolesSeleccion2());
        } else {
            tvResultado.setText("Resultado oficial no disponible.");
        }
        tvResultado.setTextSize(14f);
        tvResultado.setTypeface(null, Typeface.BOLD);
        tvResultado.setTextColor(VERDE);
        tvResultado.setGravity(Gravity.CENTER);
        tvResultado.setPadding(0, dp(8), 0, dp(6));
        tarjeta.addView(tvResultado);

        TextView aviso = new TextView(this);
        aviso.setText("Partido finalizado. No se puede modificar.");
        aviso.setTextSize(11f);
        aviso.setTextColor(GRIS);
        aviso.setGravity(Gravity.CENTER);
        tarjeta.addView(aviso);
    }

    /**
     * Crea los parámetros de diseño estándar para los botones de las tarjetas.
     *
     * @return Parámetros de diseño con ancho completo y margen superior.
     */
    private LinearLayout.LayoutParams btnParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(42));
        lp.setMargins(0, dp(8), 0, 0);
        return lp;
    }

    /**
     * Crea un campo de texto numérico para ingresar goles.
     *
     * @return El campo ({@link EditText}) configurado para números.
     */
    private EditText campoGoles() {
        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setGravity(Gravity.CENTER);
        et.setTextSize(18f);
        et.setTextColor(AZUL);
        et.setBackground(fondoRedondeado(0xFFF7F8FA, 8, 0xFFD8DDE5));
        et.setLayoutParams(new LinearLayout.LayoutParams(dp(56), dp(48)));
        return et;
    }

    /**
     * Cambia el estado de un partido y persiste el cambio reescribiendo por
     * completo el archivo partidos.txt. Carga todos los partidos, modifica el
     * indicado y vuelve a guardar la lista íntegra.
     *
     * @param objetivo Partido cuyo estado se desea cambiar.
     * @param nuevoEstado Nuevo estado a asignar.
     */
    private void cambiarEstado(Partido objetivo, String nuevoEstado) {
        try {
            List<Partido> todos = cargarTodos();
            for (Partido p : todos) {
                if (p.getIdPartido() == objetivo.getIdPartido()) {
                    p.setEstado(nuevoEstado);
                    break;
                }
            }
            guardarPartidos(todos);
        } catch (IOException e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valida y guarda el resultado oficial de un partido. Verifica que los
     * goles no estén vacíos, que sean enteros y que no sean negativos; luego
     * agrega el resultado a resultados.txt y cambia el estado del partido a
     * FINALIZADO.
     *
     * @param p Partido cuyo resultado se registra.
     * @param g1 Goles ingresados para la selección 1, como texto.
     * @param g2 Goles ingresados para la selección 2, como texto.
     * @throws DatosIncompletosException Si algún dato falta o es inválido.
     * @throws IOException Si ocurre un error al escribir los archivos.
     */
    private void guardarResultado(Partido p, String g1, String g2)
            throws DatosIncompletosException, IOException {

        if (g1.trim().isEmpty() || g2.trim().isEmpty()) {
            throw new DatosIncompletosException(
                    "No se han ingresado todos los datos necesarios para registrar el resultado.");
        }
        int goles1, goles2;
        try {
            goles1 = Integer.parseInt(g1.trim());
            goles2 = Integer.parseInt(g2.trim());
        } catch (NumberFormatException e) {
            throw new DatosIncompletosException("Los goles deben ser números enteros válidos.");
        }
        if (goles1 < 0 || goles2 < 0) {
            throw new DatosIncompletosException(
                    "Los goles deben ser números enteros mayores o iguales a cero.");
        }

        // Agrega el resultado a resultados.txt
        List<String> lineas = leerResultadosCrudo();
        int idResultado = lineas.size(); // ya sin cabecera; primer resultado = 1
        Resultado r = new Resultado(idResultado + 1, p.getIdPartido(), goles1, goles2);
        lineas.add(r.toString());

        List<String> conCabecera = new ArrayList<>();
        conCabecera.add("idResultado;idPartido;golesSeleccion1;golesSeleccion2");
        conCabecera.addAll(lineas);
        GestorArchivos.escribirInterno(this, "resultados.txt", conCabecera);

        // Cambia el estado del partido a FINALIZADO y persiste
        cambiarEstado(p, "FINALIZADO");
    }

    /**
     * Reescribe el archivo partidos.txt con la cabecera y todos los partidos.
     *
     * @param todos Lista completa de partidos a guardar.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void guardarPartidos(List<Partido> todos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("idPartido;fase;fecha;horaUTC;estadio;seleccion1;seleccion2;estado");
        for (Partido p : todos) {
            lineas.add(p.toString());
        }
        GestorArchivos.escribirInterno(this, "partidos.txt", lineas);
    }

    /**
     * Lee las líneas de resultados.txt sin incluir la cabecera.
     *
     * @return Lista de líneas de resultados registrados.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private List<String> leerResultadosCrudo() throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeInterno(this, "resultados.txt")) {
            reader.readLine(); // salta cabecera
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) lineas.add(linea.trim());
            }
        }
        return lineas;
    }

    /**
     * Busca el resultado oficial correspondiente a un partido.
     *
     * @param idPartido Identificador del partido buscado.
     * @return El {@link Resultado} del partido, o {@code null} si no existe.
     */
    private Resultado buscarResultado(int idPartido) {
        try {
            for (String linea : leerResultadosCrudo()) {
                String[] d = linea.split(";");
                if (Integer.parseInt(d[1]) == idPartido) {
                    return new Resultado(Integer.parseInt(d[0]), Integer.parseInt(d[1]),
                            Integer.parseInt(d[2]), Integer.parseInt(d[3]));
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}