package org.example.proiect_sgbd_vizionare_filme.services;

import org.example.proiect_sgbd_vizionare_filme.config.Database;
import org.example.proiect_sgbd_vizionare_filme.dao.JdbcGenreDAO;
import org.example.proiect_sgbd_vizionare_filme.dao.JdbcMovieDAO;
import org.example.proiect_sgbd_vizionare_filme.dao.JdbcUserDAO;
import org.example.proiect_sgbd_vizionare_filme.entities.Genre;
import org.example.proiect_sgbd_vizionare_filme.entities.Movie;
import org.example.proiect_sgbd_vizionare_filme.entities.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogService {

    public List<Movie> getAllMovies() throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcMovieDAO movieDAO = new JdbcMovieDAO(con);
            return movieDAO.getAllMovies();
        }
    }

    public List<Movie> getHistoryForUser(String username) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            User user = userDAO.getUserByName(username);
            if (user == null) return new ArrayList<>();

            JdbcMovieDAO mDAO = new JdbcMovieDAO(con);
            return mDAO.getWatchedMoviesByUser(user.getId());
        }
    }

    public List<String> getAllGenreNames() throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcGenreDAO genreDAO = new JdbcGenreDAO(con);
            return genreDAO.getAllGenres().stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList());
        }
    }

    public List<Movie> getFilteredMovies(String searchText, String genreName) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcMovieDAO movieDAO = new JdbcMovieDAO(con);
            JdbcGenreDAO genreDAO = new JdbcGenreDAO(con);

            List<Movie> allMovies = movieDAO.getAllMovies();

            List<Movie> filteredMovies = new ArrayList<>();

            for (Movie m : allMovies) {

                boolean matchesText = false;
                if (searchText == null || searchText.trim().isEmpty()) {
                    matchesText = true;
                } else if (m.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    matchesText = true;
                }

                boolean matchesGenre = false;
                if (genreName == null || genreName.equalsIgnoreCase("All genres")) {
                    matchesGenre = true;
                } else {
                    Genre foundGenre = genreDAO.getGenreById(m.getId());
                    if (foundGenre != null && foundGenre.getName().equalsIgnoreCase(genreName)) {
                        matchesGenre = true;
                    }
                }

                if (matchesText && matchesGenre) {
                    filteredMovies.add(m);
                }
            }
            return filteredMovies;
        }
    }

    public List<String> getRecommendations(String username) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            User user = userDAO.getUserByName(username);
            if (user == null) return new ArrayList<>();

            JdbcMovieDAO mDAO = new JdbcMovieDAO(con);
            return mDAO.getRecommendations(user.getId());
        }
    }
}