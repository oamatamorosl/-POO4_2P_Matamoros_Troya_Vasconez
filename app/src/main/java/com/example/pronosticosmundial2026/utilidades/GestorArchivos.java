package com.example.pronosticosmundial2026.utilidades;

import android.content.Context;
import java.io.*;
import java.util.List;

/**
 * Clase utilitaria que centraliza las operaciones de lectura y escritura de
 * archivos de la aplicación, tanto de texto (assets y almacenamiento interno)
 * como de objetos serializados.
 *
 * @author Equipo POO
 * @version 1.0
 */
public class GestorArchivos {

    /**
     * Abre un archivo ubicado en la carpeta assets para su lectura.
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo dentro de assets.
     * @return Un {@link BufferedReader} posicionado al inicio del archivo.
     * @throws IOException Si ocurre un error al abrir el archivo.
     */
    public static BufferedReader leerDeAssets(Context ctx, String nombreArchivo) throws IOException {
        return new BufferedReader(new InputStreamReader(ctx.getAssets().open(nombreArchivo)));
    }

    /**
     * Garantiza que exista una copia del archivo en el almacenamiento interno.
     * Si el archivo aún no existe internamente, lo copia desde la carpeta assets.
     * Esto permite trabajar con archivos que inicialmente vienen en el APK pero
     * que deben poder modificarse durante la ejecución.
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo a asegurar.
     * @throws IOException Si ocurre un error durante la copia.
     */
    public static void asegurarCopiaInterna(Context ctx, String nombreArchivo) throws IOException {
        File archivo = new File(ctx.getFilesDir(), nombreArchivo);
        if (!archivo.exists()) {
            try (InputStream entrada = ctx.getAssets().open(nombreArchivo);
                 OutputStream salida = ctx.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)) {
                byte[] buffer = new byte[1024];
                int n;
                while ((n = entrada.read(buffer)) != -1) salida.write(buffer, 0, n);
            }
        }
    }

    /**
     * Abre un archivo del almacenamiento interno para su lectura, asegurando
     * primero que exista una copia interna (copiándola desde assets si es necesario).
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo a leer.
     * @return Un {@link BufferedReader} posicionado al inicio del archivo interno.
     * @throws IOException Si ocurre un error al abrir o copiar el archivo.
     */
    public static BufferedReader leerDeInterno(Context ctx, String nombreArchivo) throws IOException {
        asegurarCopiaInterna(ctx, nombreArchivo);
        return new BufferedReader(new InputStreamReader(ctx.openFileInput(nombreArchivo)));
    }

    /**
     * Escribe una lista de líneas en un archivo del almacenamiento interno,
     * reemplazando por completo el contenido anterior.
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo a escribir.
     * @param lineas Lista de líneas que se guardarán en el archivo.
     * @throws IOException Si ocurre un error durante la escritura.
     */
    public static void escribirInterno(Context ctx, String nombreArchivo, List<String> lineas) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(ctx.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)))) {
            for (String linea : lineas) {
                writer.println(linea);
            }
        }
    }

    /**
     * Guarda un objeto en el almacenamiento interno mediante serialización.
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo donde se guardará el objeto.
     * @param objeto Objeto serializable que se desea almacenar.
     * @throws IOException Si ocurre un error durante la serialización.
     */
    public static void guardarObjeto(Context ctx, String nombreArchivo, Object objeto) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                ctx.openFileOutput(nombreArchivo, Context.MODE_PRIVATE))) {
            out.writeObject(objeto);
        }
    }

    /**
     * Lee un objeto previamente serializado desde el almacenamiento interno.
     *
     * @param ctx Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo que contiene el objeto.
     * @return El objeto leído, o {@code null} si el archivo no existe.
     * @throws IOException Si ocurre un error durante la lectura.
     * @throws ClassNotFoundException Si no se encuentra la clase del objeto serializado.
     */
    public static Object leerObjeto(Context ctx, String nombreArchivo) throws IOException, ClassNotFoundException {
        File archivo = new File(ctx.getFilesDir(), nombreArchivo);
        if (!archivo.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(ctx.openFileInput(nombreArchivo))) {
            return in.readObject();
        }
    }
}