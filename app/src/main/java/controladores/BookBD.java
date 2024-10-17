package controladores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import modelos.Book;
import modelos.User;

public class BookBD extends SQLiteOpenHelper implements ILibroBD {

    //Es el controlador de la base de datos que extiende SQLiteOpenHelper
    //Se define la estructura de la base de datos, solo con la tabla Book que contiene sus respectivos atributos
    //se implementa los metodos de onCreate - PAra crear la base de datos
    // elemento(int idBook) y elemento(String text) para obtener un libro segun su id o titulo
    Context contexto;
    private List<Book> LibroList = new ArrayList<>();
    private List<User> UserList = new ArrayList<>();


    public BookBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);
        this.contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        // Crear la estructura de la BD
        String sql = "CREATE TABLE Book (" +
                "_idBook INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "text TEXT, " +
                "cost TEXT, " +
                "available TEXT)";

        // Ejecutar la creación de la tabla
        sqLiteDatabase.execSQL(sql);

        // Insertar los datos
        String insert = "INSERT INTO Book VALUES (null, 'POSDATA: Te amo', '80.000', 'Disponible')";
        sqLiteDatabase.execSQL(insert);

        insert = "INSERT INTO Book VALUES (null, 'Bajo la misma estrella', '110.000', 'No disponible')";
        sqLiteDatabase.execSQL(insert);


        //TABLA USUARIO
        // Crear tabla User
        String sqlUser =  "CREATE TABLE User (" +
                "_idUser INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT UNIQUE, " + // Agregar UNIQUE para el email
                "password TEXT, " +
                "status INTEGER)";
        // Ejecutar la creación de la tabla
        sqLiteDatabase.execSQL(sqlUser);

        // Insertar usuarios predeterminados
        String insertUser1 = "INSERT INTO User (username, email, password, status) VALUES ('Manuela', 'manuela@gmail.com', 'manu2024', 1)";
        sqLiteDatabase.execSQL(insertUser1);

        String insertUser2 = "INSERT INTO User (username, email, password, status) VALUES ('Sebastian', 'sebastian@gmail.com', 'sebas2024', 1)";
        sqLiteDatabase.execSQL(insertUser2);

        //TABLA RENTA LIBRO

        String sqlRent = "CREATE TABLE Rent (" +
                "_idRent INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_idUser INTEGER, " +
                "_idBook INTEGER, " +
                "date TEXT, " +  // Se usa TEXT para almacenar la fecha en formato 'YYYY-MM-DD'
                "FOREIGN KEY(_idUser) REFERENCES User(_idUser), " +
                "FOREIGN KEY(_idBook) REFERENCES Book(_idBook))";

        sqLiteDatabase.execSQL(sqlRent);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes eliminar la tabla anterior y crear una nueva si es necesario
        db.execSQL("DROP TABLE IF EXISTS Book");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);

    }

    @Override
    public Book elemento(int idBook) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery( "SELECT * FROM Book WHERE _idBook=" + idBook, null);
        try {

            if (cursor.moveToNext())
                return extraerBook(cursor);
            else
                return null;
        }catch (Exception e){
            Log.d("TAG","ERROR ELEMENTO(idBook) ID BookBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }



    private Book extraerBook(Cursor cursor) {
        Book book = new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        book.setIdbook(cursor.getInt( 0));
        book.setText(cursor.getString( 1));
        book.setCost(cursor.getString( 2));
        book.setAvailable(cursor.getString( 3));

        return book;
    }

    private User extraerUser (Cursor cursor){
        User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        user.setIdUser(cursor.getInt(0));
        user.setName(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setStatus(cursor.getInt(4));

        return user;
    }

    @Override
    public Book elemento(String text) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery( "SELECT * FROM Book WHERE text='" + text + "'", null);
        try {
            if (cursor.moveToNext())
                return extraerBook(cursor);
            else
                return null;
        }catch (Exception e){
            Log.d("TAG","ERROR ELEMENTO(text) BookBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    public User elementoUser(int idUser) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE _idUser=" + idUser, null);
        try {
            if (cursor.moveToNext())
                return extraerUser(cursor);
            else
                return null;
        } catch (Exception e) {
            Log.d("TAG", "ERROR ELEMENTO(idUser) BookBD" + e.getMessage());
            throw e;
        } finally {
            if (cursor != null) cursor.close();
        }
    }


    @Override
    public List<Book> Lista() {
        //devuelve el registro de datos encontrados en la BD
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM Book ORDER BY text ASC";

        Cursor cursor = database.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                LibroList.add(
                        new Book( cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3) )
                );
            }while(cursor.moveToNext());
        }
        cursor.close();
        return LibroList;
    }

    public List<User> ListaUser(){
        //devuelve el registro de datos encontrados en la BD
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM User ORDER BY email ASC";

        Cursor cursor = database.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                UserList.add(
                        new User(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getInt(4))
                );
            }while(cursor.moveToNext());
        }

        return UserList;
    }



    @Override
    public void agregar(Book book) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("text", book.getText());
        values.put("cost", book.getCost());
        values.put("available", book.getAvailable());
        database.insert("Book", null, values );

    }

    @Override
    public void actualizar(int idbook, Book book) {

        SQLiteDatabase database = getWritableDatabase();
        String[] parametros = { String.valueOf(idbook)};
        ContentValues values = new ContentValues();

        values.put("text", book.getText());
        values.put("cost", book.getCost());
        values.put("available", book.getAvailable());

        database.update("Book", values, "_idBook=?", parametros );

    }

    @Override
    public void borrar(int idBook) {
        SQLiteDatabase database = getWritableDatabase();
        String[] parametros = { String.valueOf(idBook)};

        database.delete("Book", "_idBook=?", parametros);
    }


    public String BuscarUserforRent(String email){

        String Email = email;
        SQLiteDatabase db = getReadableDatabase();
        String query = db.rawQuery("Select status From User where email = '"+Email+"'",null).toString();
        return query;
    }

    // Obtener todos los usuarios
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM User", null);
    }

    // Actualizar un usuario
    public boolean actualizarUsuario(int id, String username, String email, String password, String status) {
        // Verificar que los parámetros no sean nulos o vacíos
        if (username == null || email == null || password == null || status == null) {
            Log.e("DB_UPDATE", "Uno o más parámetros son nulos");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("status", status);

        Log.d("DB_UPDATE", "Actualizando usuario con ID: " + id + " Username: " + username + " Email: " + email + " Password: " + password);

        try {
            int rowsAffected = db.update("User", contentValues, "_idUser = ?", new String[]{String.valueOf(id)});
            Log.d("DB_UPDATE", "Filas afectadas: " + rowsAffected);
            return rowsAffected > 0; // True si se actualizó al menos un registro
        } catch (Exception e) {
            Log.e("DB_UPDATE", "Error al actualizar el usuario: " + e.getMessage());
            return false;
        } finally {
            db.close(); // Cerrar la base de datos
        }
    }

    public void actualizarStatus(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("available", "No disponible");

        // Aquí asumes que el ID del libro ya está asociado a un usuario y lo actualizas
        db.update("books", contentValues, "email=?", new String[]{email});
        db.close();
    }


    // Eliminar un usuario
    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Eliminar el usuario según su _idUser
        int result = db.delete("User", "_idUser = ?", new String[]{String.valueOf(id)});
        return result > 0;  // Retorna true si se eliminó el usuario
    }

}//BookDB
