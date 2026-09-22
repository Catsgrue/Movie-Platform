package org.example.proiect_sgbd_vizionare_filme.entities;

import java.sql.Date;

public class Movie {

    private int id_movie;
    private String title;
    private String description;
    private int id_genre;
    private Date release_date;
    private int movie_duration;
    private double rating_mediu;

    public Movie(int id_movie, String title, String description, int id_genre,int movie_duration,Date release_date, double rating_mediu) {
        this.id_movie = id_movie;
        this.title = title;
        this.description = description;
        this.id_genre = id_genre;
        this.movie_duration=movie_duration;
        this.release_date = release_date;
        this.rating_mediu = rating_mediu;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id_movie;
    }

    public String getDescription() {
        return description;
    }

    public int getId_genre() {
        return id_genre;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public double getRating_mediu() {
        return rating_mediu;
    }

    public int getMovie_duration() {
        return movie_duration;
    }
}
