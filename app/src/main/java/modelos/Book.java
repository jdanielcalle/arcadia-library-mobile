package modelos;

public class Book {

    //atributos de la clase Book
    private Integer idbook;
    private String text, cost, available;

    //constructos sin argumentos o por defecto vacio
    public Book(){

    }

    //constructor con argumentos
    public Book(Integer idBook, String text, String cost, String available) {
        this.idbook = idBook;
        this.text = text;
        this.cost = cost;
        this.available = available;
    }

    public Integer getIdbook() {
        return idbook;
    }

    public void setIdbook(Integer idbook) {
        this.idbook = idbook;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Book{" +
                "idbook=" + idbook +
                ", available=" + available +
                ", text='" + text + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}//Book
