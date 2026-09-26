package org.example.proiect_sgbd_vizionare_filme.entities;

import java.sql.Date;

public class Movie {

    private int id;
    private String title;
    private String description;
    private int idGenre;
    private Date releaseDate;
    private int movieDuration;
    private double ratingMediu;
    private String imageURL;

    public Movie(int id, String title, String description, int idGenre,int movieDuration,Date releaseDate, double ratingMediu, String imageURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.idGenre = idGenre;
        this.movieDuration=movieDuration;
        this.releaseDate = releaseDate;
        this.ratingMediu = ratingMediu;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getRatingMediu() {
        return ratingMediu;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public String getImageURL() {
        return imageURL;
    }
}
