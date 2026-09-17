package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Genre;

import java.util.List;

public interface GenreDAO {

    List<Genre> getAllGenres();
    Genre getGenreByMovie(String title);
    Genre getGenreById(int id);

}
