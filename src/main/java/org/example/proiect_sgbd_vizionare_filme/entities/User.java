package org.example.proiect_sgbd_vizionare_filme.entities;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;

    public User(int id, String firstName, String lastName, String email, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
