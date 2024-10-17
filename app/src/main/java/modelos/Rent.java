package modelos;

import java.net.Inet4Address;

public class Rent {

    private Integer idRent, idUser, idBook;
    private String date;

    //constructos sin argumentos o por defecto vacio
    public Rent(){

    }

    //constructor con argumentos
    public Rent(Integer idRent, Integer idUser, Integer idBook, String date){
        this.idRent = idRent;
        this.idUser = idUser;
        this.idBook = idBook;
        this.date = date;
    }

    public Integer getIdRent() {
        return idRent;
    }

    public void setIdRent(Integer idRent) {
        this.idRent = idRent;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
