package com.example.arcadialibraryv2;

import android.content.ContentValues;
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

import modelos.User;
import controladores.BookBD;

public class registro extends AppCompatActivity {

    EditText txtUsername, txtEmail, txtPassword;
    Button btnRegistrar;
    TextView tvLogin;
    BookBD bookBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de los campos y botones
        txtUsername = findViewById(R.id.Rusername);
        txtEmail = findViewById(R.id.Remail);
        txtPassword = findViewById(R.id.Rpassword);
        btnRegistrar = findViewById(R.id.register_button);
        tvLogin = findViewById(R.id.login_button);

        // Inicializar la base de datos
        bookBD = new BookBD(this, "BookBD.db", null, 2);

        // Manejar el evento de clic en el botón de registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        // Manejar el evento de clic para ir a la pantalla de inicio de sesión
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar a la actividad de inicio de sesión
                Intent intent = new Intent(registro.this, Login.class);  // Reemplaza Login.class con la clase de tu pantalla de inicio de sesión
                startActivity(intent);
                finish();  // Cerrar la actividad de registro
            }
        });
    }

    private void registrarUsuario() {
        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        // Validar campos
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo electrónico ya está registrado
        if (emailExists(email)) {
            Toast.makeText(this, "Este correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo usuario
        User newUser = new User();
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setStatus(1); // Establecer el estado según sea necesario

        // Insertar el nuevo usuario en la base de datos
        if (agregarUsuario(newUser)) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            // Guardar el estado de registro en SharedPreferences
            SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isRegistered", true);  // Marcar como registrado
            editor.apply();

            // Redirigir al Login o pantalla principal
            Intent intent = new Intent(registro.this, Login.class);  // Cambia a la actividad que prefieras
            startActivity(intent);
            finish();  // Cerrar la actividad de registro
        } else {
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean agregarUsuario(User user) {
        // Agregar el usuario a la base de datos
        ContentValues values = new ContentValues();
        values.put("username", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("status", user.getStatus());

        long result = bookBD.getWritableDatabase().insert("User", null, values);
        return result != -1; // Devuelve true si la inserción fue exitosa
    }

    private boolean emailExists(String email) {
        // Verificar si el correo electrónico ya existe en la base de datos
        Cursor cursor = bookBD.getReadableDatabase().rawQuery(
                "SELECT 1 FROM User WHERE email = ?",
                new String[]{email}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
