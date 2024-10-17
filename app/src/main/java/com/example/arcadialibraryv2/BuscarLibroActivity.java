package com.example.arcadialibraryv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import controladores.BookBD;
import modelos.Book;

public class BuscarLibroActivity extends AppCompatActivity implements View.OnClickListener{

    //permite buscar un libro por su titulo
    //Usa un campo de texto para ingresar el titulo y un boton de buscar.
    //si se encuentra el libro, pasa al modulo de GestionarLibroActivity para su posible edicion o eliminacion.
    Context context;
    EditText txttext;
    Button btnbuscar;
    BookBD BookBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_libro);

        init();
    }

    private void init(){
        context = getApplicationContext();
        txttext = findViewById(R.id.bus_txttext);
        btnbuscar = findViewById(R.id.bus_btnbuscar);
    }
    //asociar con los botones, este se puede restringir
    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.bus_btnbuscar){
            String text = txttext.getText().toString();

            Book book = buscarlibro(text);
            if(book != null){
                Bundle bolsa = new Bundle();
                bolsa.putInt("idBook", book.getIdbook());
                bolsa.putString("text", book.getText());
                bolsa.putString("cost", book.getCost());
                bolsa.putString("available", book.getAvailable());

                Intent i = new Intent(context, GestionarLibroActivity.class);
                i.putExtras(bolsa);
                startActivity(i);
            }else{
                Toast.makeText(context, "No existe el libro con ese titulo", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Book buscarlibro(String text) {
        BookBD = new BookBD(context, "BookBD.db", null, 2);
        Book Book = BookBD.elemento(text);
        return Book;
    }
}