package com.example.arcadialibraryv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import controladores.BookBD;
import controladores.SelectListener;
import modelos.Book;

public class ListadoLibrosActivity extends AppCompatActivity implements SelectListener {

    //Muestra una lista de todos los libros almacenados en la base de datos
    //Los nombres de los libros se muestran en un ListView y, al hacer click en un item,
    //Se lleva al usuario a la pantalla de GestionarLibroActivity con los detalles del libro.
    //- Se utiliza ArrayAdapter para poblar el ListView


    ListView ListView;
    ArrayList<String> nombresLibros;
    ArrayList<Integer> idBooks;
    BookBD bookBD;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_libros);

        init();
    }

    private void init(){
        context =this.getApplicationContext();
        bookBD = new BookBD(context, "BookBD.db", null, 2);
        ListView = findViewById(R.id.listaLibros);
        llenarListView();
    }

    private void llenarListView() {
        nombresLibros = new ArrayList<String>();
        idBooks = new ArrayList<Integer>();

        List<Book> listaLibros = bookBD.Lista();
        for(int i=0; i<listaLibros.size(); i++){
            Book b = listaLibros.get(i);
            nombresLibros.add(b.getText());
            idBooks.add(b.getIdbook());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_list_item_1,
                        nombresLibros
                );
        ListView.setAdapter(adapter);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int i, long b) {
                                                Book book = bookBD.elemento(idBooks.get(i));
                                                Bundle bolsa = new Bundle();
                                                bolsa.putInt("idBook", book.getIdbook());
                                                bolsa.putString("text", book.getText());
                                                bolsa.putString("cost", book.getCost());
                                                bolsa.putString("available", book.getAvailable());

                                                Intent intent = new Intent(context, GestionarLibroActivity.class);
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
