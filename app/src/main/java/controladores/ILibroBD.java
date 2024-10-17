package controladores;

import android.database.Cursor;

import java.util.List;

import modelos.Book;

public interface ILibroBD {

    //en este apartado estan los elementos que queremos que se ejecuten dentro de la clase
    //con la base de datos

    Book elemento(int idBook); //devuelve el elemento dado con su idbook
    Book elemento(String text); //devuelve el elemento dado su titulo

    List<Book> Lista(); //devuelve una lista con todos lso elementos registrados

    void agregar(Book book); //añade el elemento indicado
    void actualizar(int idbook, Book book); //actualiza datos del elemento dado su idbook
    void borrar(int idbook); //Elimina el elemento indicado con el idbook


    // Obtener todos los usuarios
    Cursor getAllUsers();

    // Actualizar un usuario
    boolean actualizarUsuario(int id,String username, String email, String password, String status);

    // Eliminar un usuario
    boolean eliminarUsuario(int id);
}
