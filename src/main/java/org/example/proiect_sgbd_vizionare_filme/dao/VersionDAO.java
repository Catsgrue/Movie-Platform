package org.example.proiect_sgbd_vizionare_filme.dao;

import java.util.List;

public interface VersionDAO {

    void createVersion(int id_movie,String format,String language);
    int getIdVersionByIdMovie(int id_movie,String format,String language);
    List<String> getFormatsByMovieId(int id_movie);
    List<String> getLanguagesByMovieId(int id_movie);

}
