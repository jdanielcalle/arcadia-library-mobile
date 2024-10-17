package com.example.arcadialibraryv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import controladores.BookBD;
import modelos.User;

public class GestionarUsuarioActivity extends AppCompatActivity {

    EditText Id, Username, Email, password, estadoUser;
    Button Delete, Update, Search;

    BookBD bookBD = new BookBD(this, "BookBD.db", null, 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_usuario);

        // Asignar referencias a los elementos de la UI
        Id = findViewById(R.id.IdUser);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        estadoUser = findViewById(R.id.estadoUser);
        Delete = findViewById(R.id.btnEliminarUsuario);
        Update = findViewById(R.id.btnActualizarUsuario);
        Search = findViewById(R.id.btnbuscar);


        // Obtener los datos del Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Id.setText(String.valueOf(extras.getInt("idUser")));
            Username.setText(extras.getString("username"));
            Email.setText(extras.getString("email"));
            password.setText(extras.getString("password"));

            Id.setEnabled(false);
        }

        // Evento de búsqueda
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Id.getText().toString().isEmpty()) {
                    // Buscar usuario por ID
                    ArrayList<User> result = searchID(Id.getText().toString());
                    if (!result.isEmpty()) {
                        // Obtener el primer usuario de la lista
                        User user = result.get(0);

                        // Asignar los valores a los campos
                        Username.setText(user.getName());
                        Email.setText(user.getEmail());
                        password.setText(user.getPassword());

                        // Mostrar el estado del usuario y gestionar el botón "Eliminar"
                        String estado = user.getStatus() == 1 ? "No Sancionado" : "Sancionado";
                        estadoUser.setText(estado);

                        // Desactivar el botón "Eliminar" si el usuario está sancionado
                        if (estado.equals("Sancionado")) {
                            Delete.setEnabled(false); // Desactivar el botón
                        } else {
                            Delete.setEnabled(true);  // Activar el botón
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "El usuario no se encuentra registrado en la base de datos", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Campo de búsqueda vacío", Toast.LENGTH_LONG).show();
                }
            }
        });



        // Evento de actualización
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mIdUser = Id.getText().toString();
                String mUsername = Username.getText().toString();
                String mEmail = Email.getText().toString();
                String mPassword = password.getText().toString();
                String mStatus = estadoUser.getText().toString().equals("No Sancionado") ? "1" : "0";

                if (checkData(mIdUser, mUsername, mEmail, mPassword)) {
                    // Verificar si el usuario existe
                    if (searchID(mIdUser).size() > 0) {
                        int id = Integer.parseInt(mIdUser);
                        // Actualizar datos del usuario
                        bookBD.actualizarUsuario(id, mUsername, mEmail, mPassword, mStatus);
                        Toast.makeText(getApplicationContext(), "Datos actualizados con éxito", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "El usuario no se encuentra registrado", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Faltan campos por llenar", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Evento de eliminación
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mIdUser = Id.getText().toString();
                String mStatus = estadoUser.getText().toString();

                if(mStatus == "Sancionado"){
                    Delete.setEnabled(true);
                }
                else {
                    if (!mIdUser.isEmpty()) {
                        if (searchID(mIdUser).size() > 0) {
                            new AlertDialog.Builder(GestionarUsuarioActivity.this)
                                    .setTitle("Eliminar usuario")
                                    .setMessage("¿Estás seguro de que deseas borrar este usuario?")
                                    .setPositiveButton("Eliminar", (dialogInterface, i) -> {
                                        int id = Integer.parseInt(mIdUser);
                                        if (!bookBD.eliminarUsuario(id)) {
                                            Toast.makeText(getApplicationContext(), "No puedes eliminar el usuario, tiene deudas", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Usuario eliminado exitosamente", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(), "El usuario no se encuentra registrado", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Campo de búsqueda vacío", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    // Método para buscar usuario por ID
    private ArrayList<User> searchID(String mIdUser) {
        Integer idUser = Integer.parseInt(mIdUser);
        ArrayList<User> arrUser = new ArrayList<>();
        SQLiteDatabase userRead = bookBD.getReadableDatabase();

        // Incluir la columna status en la consulta SQL
        String query = "SELECT username, email, password, status FROM User WHERE _idUser = '" + idUser + "'";
        Cursor cUser = userRead.rawQuery(query, null);

        if (cUser.moveToFirst()) {
            User tblUser = new User(); // Crear nuevo objeto User
            tblUser.setIdUser(idUser);
            tblUser.setName(cUser.getString(0));
            tblUser.setEmail(cUser.getString(1));
            tblUser.setPassword(cUser.getString(2));
            tblUser.setStatus(cUser.getInt(3)); // Asignar el estado
            arrUser.add(tblUser);
        }
        cUser.close(); // Cerrar cursor
        return arrUser;
    }

    // Verificar si todos los campos tienen datos
    private boolean checkData(String mIdUser, String mUsername, String mEmail, String mPassword) {
        return !mIdUser.isEmpty() && !mUsername.isEmpty() && !mEmail.isEmpty() && !mPassword.isEmpty();
    }
}
