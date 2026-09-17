package org.example.proiect_sgbd_vizionare_filme.concrete_classes;

public class User {

    private int id_user;
    private String first_name;
    private String last_name;
    private String email;
    private String city;

    public User(int id_user, String first_name, String last_name, String email, String city) {
        this.id_user = id_user;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.city = city;
    }

    public int getId_user() {
        return id_user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }
}
