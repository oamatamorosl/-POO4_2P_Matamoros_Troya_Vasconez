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

/**
 * Pantalla que muestra la tabla de posiciones de todos los participantes,
 * ordenada de mayor a menor puntaje (y alfabéticamente en caso de empate).
 * Combina los datos de usuarios.txt y participantes.txt para construir la tabla.
 *
 * @author Sebastian_Vasconez
 * @version 1.0
 */
public class TablaPosicionesActivity extends AppCompatActivity {

    /** Contenedor donde se agregan dinámicamente las filas de la tabla. */
    private TableLayout tablaLayout;

    /** Etiqueta que muestra el nombre del participante autenticado. */
    private TextView lblNombreParticipante;

    /**
     * Inicializa la pantalla, carga los participantes, los ordena y construye
     * la tabla de posiciones. Muestra un mensaje si ocurre un error de lectura.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
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

    /**
     * Carga la lista de participantes combinando los nombres de usuarios.txt
     * con los puntajes de participantes.txt. El nombre de usuario se usa para
     * el desempate alfabético y el nombre completo para mostrar en la tabla.
     *
     * @return Lista de participantes con su puntaje acumulado.
     * @throws IOException Si ocurre un error al leer alguno de los archivos.
     */
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

    /**
     * Agrega la fila de encabezado (Posición, Participante, Puntos) a la tabla.
     */
    private void mostrarEncabezado() {
        TableRow fila = new TableRow(this);
        fila.addView(crearCelda("Pos.", true));
        fila.addView(crearCelda("Participante", true));
        fila.addView(crearCelda("Puntos", true));
        tablaLayout.addView(fila);
    }

    /**
     * Agrega una fila por cada participante, mostrando su posición, nombre y puntaje.
     *
     * @param lista Lista de participantes ya ordenada.
     */
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

    /**
     * Crea una celda de texto para la tabla, resaltándola en negrita si es
     * parte del encabezado.
     *
     * @param texto Contenido de la celda.
     * @param esEncabezado {@code true} si la celda pertenece al encabezado.
     * @return La celda ({@link TextView}) configurada.
     */
    private TextView crearCelda(String texto, boolean esEncabezado) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(24, 16, 24, 16);
        if (esEncabezado) tv.setTypeface(null, android.graphics.Typeface.BOLD);
        return tv;
    }

    /**
     * Regresa al menú principal del participante.
     *
     * @param v Vista que originó el evento.
     */
    public void volver(View v) {
        finish();
    }
}