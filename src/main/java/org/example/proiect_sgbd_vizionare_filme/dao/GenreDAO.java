package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Genre;

import java.util.List;

public interface GenreDAO {

    List<Genre> getAllGenres();
    Genre getGenreByMovie(String title);
    Genre getGenreById(int id);

}
