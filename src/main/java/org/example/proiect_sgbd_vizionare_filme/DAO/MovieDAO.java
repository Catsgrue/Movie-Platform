package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Movie;

import java.util.List;

public interface MovieDAO {

    List<Movie> getAllMovies();
    Movie getMovieById(int id);
    List<String> getRecommendations(int idUser);
    List<Movie> getWatchedMoviesByUser(int idUser);
}
