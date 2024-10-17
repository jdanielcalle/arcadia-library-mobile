package com.example.arcadialibraryv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import controladores.BookBD;
import controladores.SelectListener;
import modelos.Book;
import modelos.User;

public class ListarUsuarios extends AppCompatActivity implements SelectListener {

    ListView ListView;
    ArrayList<String> nombresUsuario;
    ArrayList<Integer> idUser;
    BookBD bookBD;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        init();

    }

    private void init(){
        context =this.getApplicationContext();
        bookBD = new BookBD(context, "BookBD.db", null, 2);
        ListView = findViewById(R.id.listaUsuarios);
        llenarListView();
    }

    private void llenarListView() {
        nombresUsuario = new ArrayList<String>();
        idUser = new ArrayList<Integer>();

        List<User> listaUsuarios = bookBD.ListaUser();
        for(int i=0; i<listaUsuarios.size(); i++){
            User u = listaUsuarios.get(i);
            nombresUsuario.add(u.getEmail());
            idUser.add(u.getIdUser());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_list_item_1,
                        nombresUsuario
                );
        ListView.setAdapter(adapter);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int i, long u) {
                                                User user = bookBD.elementoUser(idUser.get(i));
                                                Bundle bolsa = new Bundle();
                                                bolsa.putInt("idUser", user.getIdUser());
                                                bolsa.putString("username", user.getName());
                                                bolsa.putString("email", user.getEmail());
                                                bolsa.putString("password", user.getPassword());

                                                Intent intent = new Intent(context, GestionarUsuarioActivity.class);
                                                intent.putExtras(bolsa);
                                                startActivity(intent);
                                            }
                                        }
        );
    }


    @Override
    public void onItemClick(String text) {

    }
}
