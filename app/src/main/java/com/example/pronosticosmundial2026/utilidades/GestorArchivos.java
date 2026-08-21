package com.example.pronosticosmundial2026.utilidades;

import android.content.Context;
import java.io.*;
import java.util.List;

public class GestorArchivos {

    public static BufferedReader leerDeAssets(Context ctx, String nombreArchivo) throws IOException {
        return new BufferedReader(new InputStreamReader(ctx.getAssets().open(nombreArchivo)));
    }

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

    public static BufferedReader leerDeInterno(Context ctx, String nombreArchivo) throws IOException {
        asegurarCopiaInterna(ctx, nombreArchivo);
        return new BufferedReader(new InputStreamReader(ctx.openFileInput(nombreArchivo)));
    }


    public static void escribirInterno(Context ctx, String nombreArchivo, List<String> lineas) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(ctx.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)))) {
            for (String linea : lineas) {
                writer.println(linea);
            }
        }

    }


    public static void guardarObjeto(Context ctx, String nombreArchivo, Object objeto) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                ctx.openFileOutput(nombreArchivo, Context.MODE_PRIVATE))) {
            out.writeObject(objeto);
        }
    }


    public static Object leerObjeto(Context ctx, String nombreArchivo) throws IOException, ClassNotFoundException {
        File archivo = new File(ctx.getFilesDir(), nombreArchivo);
        if (!archivo.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(ctx.openFileInput(nombreArchivo))) {
            return in.readObject();
        }
    }


}