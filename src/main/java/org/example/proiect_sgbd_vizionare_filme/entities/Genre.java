package org.example.proiect_sgbd_vizionare_filme.entities;

public class Genre {

    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() { return this.id; }
}

