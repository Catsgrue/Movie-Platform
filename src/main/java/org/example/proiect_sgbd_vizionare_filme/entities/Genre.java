package org.example.proiect_sgbd_vizionare_filme.entities;

public class Genre {

    private int id_genre;
    private String genre_name;

    public Genre(int id_genre, String genre_name) {
        this.id_genre = id_genre;
        this.genre_name = genre_name;
    }

    public String getGenre_name() {
        return genre_name;
    }
}

