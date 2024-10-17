package com.example.arcadialibraryv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import controladores.BookBD;

public class Login extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    Button btnLogin;
    TextView tvRegistro;
    BookBD bookBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar campos y botones
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        tvRegistro = findViewById(R.id.register_button);
        btnLogin = findViewById(R.id.login_button);

        // Inicializar base de datos
        bookBD = new BookBD(this, "BookBD.db", null, 2);

        // Evento para el botón de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                // Validar que los campos no estén vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si el usuario existe
                if (verificarUsuario(username, password)) {
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Guardar estado en SharedPreferences para indicar que el usuario ya inició sesión
                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isRegistered", true);  // Cambiar a true cuando el usuario haya iniciado sesión
                    editor.apply();

                    // Redirigir a la actividad principal (librería)
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();  // Cerrar la actividad de login
                } else {
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Evento para redirigir al registro
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirigir a la pantalla de registro
                Intent intent = new Intent(Login.this, registro.class);
                startActivity(intent);
            }
        });
    }

    // Función para verificar si el usuario existe en la base de datos
    private boolean verificarUsuario(String email, String password) {
        Cursor cursor = bookBD.getReadableDatabase().rawQuery(
                "SELECT * FROM User WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
