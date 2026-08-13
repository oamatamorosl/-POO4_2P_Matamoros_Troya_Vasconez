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

public class LoginActivity extends AppCompatActivity {

    private EditText editUsuario, editContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsuario = findViewById(R.id.editUsuario);
        editContrasenia = findViewById(R.id.editContrasenia);
    }

    public void iniciarSesion(View v) {
        String user = editUsuario.getText().toString().trim();
        String pass = editContrasenia.getText().toString().trim();
        try {
            Usuario autenticado = validarCredenciales(user, pass);

            // TODO: descomentar cuando existan MenuParticipanteActivity y MenuAdministradorActivity
            /*
            Intent intent = autenticado.getTipoUsuario().equals("PARTICIPANTE")
                    ? new Intent(this, MenuParticipanteActivity.class)
                    : new Intent(this, MenuAdministradorActivity.class);
            intent.putExtra("idUsuario", autenticado.getIdUsuario());
            intent.putExtra("nombreCompleto", autenticado.getNombreCompleto());
            startActivity(intent);
            */

            // Prueba temporal mientras no existen los menús:
            Toast.makeText(this,
                    "Login OK: " + autenticado.getNombreCompleto() + " (" + autenticado.getTipoUsuario() + ")",
                    Toast.LENGTH_LONG).show();

        } catch (CredencialesInvalidasException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Problemas técnicos. Estamos resolviendo.", Toast.LENGTH_SHORT).show();
        }
    }

    private Usuario validarCredenciales(String user, String pass) throws IOException, CredencialesInvalidasException {
        for (Usuario u : cargarUsuarios()) {
            if (u.getNombreUsuario().equals(user) && u.getContrasena().equals(pass)) {
                return u;
            }
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }

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