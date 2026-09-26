package org.example.proiect_sgbd_vizionare_filme.entities;

import java.sql.Date;

public class Actor {
    private int id;
    private String stageName;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String imageURL;

    public Actor(int id, String stageName, String firstName, String lastName, Date dateOfBirth, String imageURL) {
        this.id = id;
        this.stageName = stageName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.imageURL = imageURL;
    }

    public String getStageName() {
        return stageName;
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }
}
