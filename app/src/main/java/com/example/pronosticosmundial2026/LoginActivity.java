package com.example.pronosticosmundial2026;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pronosticosmundial2026.excepciones.CredencialesInvalidasException;
import com.example.pronosticosmundial2026.modelo.Administrador;
import com.example.pronosticosmundial2026.modelo.Participante;
import com.example.pronosticosmundial2026.modelo.Usuario;
import com.example.pronosticosmundial2026.utilidades.GestorArchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de inicio de sesión de la aplicación. Valida las credenciales
 * ingresadas contra el archivo usuarios.txt y, según el tipo de usuario
 * autenticado, redirige al menú de participante o de administrador.
 *
 * @author Aidan_Troya
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {

    /** Campos de texto para el nombre de usuario y la contraseña. */
    private EditText editUsuario, editContrasenia;

    /**
     * Inicializa la pantalla de inicio de sesión y enlaza los campos de texto.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsuario = findViewById(R.id.editUsuario);
        editContrasenia = findViewById(R.id.editContrasenia);
    }

    /**
     * Maneja el evento del botón de inicio de sesión. Valida las credenciales
     * y, si son correctas, abre el menú correspondiente al tipo de usuario.
     * Si son incorrectas, muestra el mensaje de error mediante un Toast.
     *
     * @param v Vista que originó el evento (el botón de iniciar sesión).
     */
    public void iniciarSesion(View v) {
        String user = editUsuario.getText().toString().trim();
        String pass = editContrasenia.getText().toString().trim();
        try {
            Usuario autenticado = validarCredenciales(user, pass);

            Intent intent = autenticado.getTipoUsuario().equals("PARTICIPANTE")
                    ? new Intent(this, MenuParticipanteActivity.class)
                    : new Intent(this, MenuAdministradorActivity.class);
            intent.putExtra("idUsuario", autenticado.getIdUsuario());
            intent.putExtra("nombreCompleto", autenticado.getNombreCompleto());
            startActivity(intent);

        } catch (CredencialesInvalidasException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Busca un usuario cuyo nombre y contraseña coincidan con los ingresados.
     *
     * @param user Nombre de usuario ingresado.
     * @param pass Contraseña ingresada.
     * @return El usuario autenticado si las credenciales coinciden.
     * @throws IOException Si ocurre un error al leer el archivo de usuarios.
     * @throws CredencialesInvalidasException Si no existe un usuario con esas credenciales.
     */
    private Usuario validarCredenciales(String user, String pass) throws IOException, CredencialesInvalidasException {
        for (Usuario u : cargarUsuarios()) {
            if (u.getNombreUsuario().equals(user) && u.getContrasena().equals(pass)) {
                return u;
            }
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }

    /**
     * Carga la lista de usuarios desde el archivo usuarios.txt ubicado en assets,
     * creando instancias de {@link Participante} o {@link Administrador} según
     * el tipo indicado en cada línea.
     *
     * @return Lista de usuarios registrados en la aplicación.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private List<Usuario> cargarUsuarios() throws IOException {
        List<Usuario> lista = new ArrayList<>();
        try (BufferedReader reader = GestorArchivos.leerDeAssets(this, "usuarios.txt")) {
            String linea = reader.readLine(); // salta cabecera
            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";");
                String idUsuario = d[0];
                String nombreUsuario = d[1], contrasena = d[2], nombreCompleto = d[3], tipo = d[4];
                if (tipo.equals("PARTICIPANTE")) {
                    lista.add(new Participante(idUsuario, nombreUsuario, contrasena, nombreCompleto, 0));
                } else {
                    lista.add(new Administrador(idUsuario, nombreUsuario, contrasena, nombreCompleto, ""));
                }
            }
        }
        return lista;
    }
}