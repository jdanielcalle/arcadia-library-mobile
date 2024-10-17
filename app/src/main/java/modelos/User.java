package modelos;

public class User {

    private Integer  idUser, status;
    private String  username, email, password;


    //constructos sin argumentos o por defecto vacio
    public User(){

    }

    //constructor con argumentos
    public User(Integer idUser, String username, String email, String password, Integer status) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser =" + idUser +
                ", name =" + username +
                ", email ='" + email + '\'' +
                ", password ='" + password + '\'' +
                ", status ='" + status + '\'' +'}';
    }
}
