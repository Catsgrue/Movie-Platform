package org.example.proiect_sgbd_vizionare_filme.services;

import org.example.proiect_sgbd_vizionare_filme.config.Database;
import org.example.proiect_sgbd_vizionare_filme.dao.*;
import org.example.proiect_sgbd_vizionare_filme.entities.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovieDetailsService {

    public Movie getMovieById(int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcMovieDAO movieDAO = new JdbcMovieDAO(con);
            return movieDAO.getMovieById(movieId);
        }
    }

    public Genre getGenreById(int genreId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcGenreDAO genreDAO = new JdbcGenreDAO(con);
            return genreDAO.getGenreById(genreId);
        }
    }

    public List<String> getFormats(int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcVersionDAO versionDAO = new JdbcVersionDAO(con);
            return versionDAO.getFormatsByMovieId(movieId);
        }
    }

    public List<String> getLanguages(int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcVersionDAO versionDAO = new JdbcVersionDAO(con);
            return versionDAO.getLanguagesByMovieId(movieId);
        }
    }

    public List<Actor> getMovieActors(int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcMovieCastDAO movieCastDAO = new JdbcMovieCastDAO(con);
            List<Integer> idActors = movieCastDAO.getId_ActorsByMovieId(movieId);

            if (idActors != null && !idActors.isEmpty()) {
                JdbcActorDAO actorDAO = new JdbcActorDAO(con);
                return actorDAO.getActorsById_Actors(idActors);
            }
            return new ArrayList<>();
        }
    }

    public String getActorRole(int actorId, int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcMovieCastDAO movieCastDAO = new JdbcMovieCastDAO(con);
            return movieCastDAO.getRoleByIdActor(actorId, movieId);
        }
    }

    public List<Review> getMovieReviews(int movieId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcReviewDAO reviewDAO = new JdbcReviewDAO(con);
            return reviewDAO.getReviewsByMovieId(movieId);
        }
    }

    public String getUserNameForReview(int viewId) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            return userDAO.getUserFullNameByViewId(viewId);
        }
    }

    public void submitReview(String username, int movieId, int rating, String commentText, String predefinedOption) throws Exception {
        try (Connection con = Database.getConnection()) {
            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            JdbcViewDAO viewDAO = new JdbcViewDAO(con);
            JdbcReviewDAO reviewDAO = new JdbcReviewDAO(con);

            User loggedInUser = userDAO.getUserByName(username);
            if (loggedInUser == null) {
                throw new IllegalStateException("User account not found.");
            }

            int idView = viewDAO.getLastViewIdForMovie(loggedInUser.getId(), movieId);
            if (idView == -1) {
                throw new IllegalStateException("You cannot review a movie you haven't watched. Please press PLAY first.");
            }

            String finalComment = (commentText != null && !commentText.trim().isEmpty()) ? commentText : null;
            String finalOption = (predefinedOption == null || predefinedOption.trim().isEmpty()) ? "General" : predefinedOption;

            reviewDAO.insertReview(idView, rating, finalComment, finalOption);
        }
    }

    public int watchMovie(String username, int movieId, String format, String language) throws Exception {
        if (language == null || format == null || language.trim().isEmpty() || format.trim().isEmpty()) {
            throw new IllegalArgumentException("You must choose a format and a language in order to watch the movie!");
        }

        try (Connection con = Database.getConnection()) {
            JdbcVersionDAO versionDAO = new JdbcVersionDAO(con);
            int id_version = versionDAO.getIdVersionByIdMovie(movieId, format, language);

            if (id_version == -1) {
                throw new IllegalStateException("Selected format/language version is not available.");
            }

            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            User user = userDAO.getUserByName(username);
            if (user == null) {
                throw new IllegalStateException("User account not found.");
            }

            JdbcMovieDAO movieDAO = new JdbcMovieDAO(con);
            Movie movie = movieDAO.getMovieById(movieId);

            JdbcViewDAO viewDAO = new JdbcViewDAO(con);

            int movie_duration = movie.getMovieDuration();
            int id_user = user.getId();
            int previous_minutes = viewDAO.getPreviousWatchedMinutes(id_user, id_version);
            int watched_minutes;
            Random random = new Random();

            if (previous_minutes >= movie_duration) {
                watched_minutes = movie_duration;
            } else if (previous_minutes > 0) {
                int min = previous_minutes + 1;
                int max = movie_duration;
                watched_minutes = random.nextInt(max - min + 1) + min;
            } else {
                watched_minutes = random.nextInt(movie_duration) + 1;
            }

            viewDAO.createView(id_user, id_version, watched_minutes);
            return watched_minutes;
        }
    }
}