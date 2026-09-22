package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Movie;

import java.util.List;

public interface MovieDAO {

    List<Movie> getAllMovies();
    Movie getMovieById(int id);
    List<String> getRecommendations(int idUser);
    List<Movie> getWatchedMoviesByUser(int idUser);
}
