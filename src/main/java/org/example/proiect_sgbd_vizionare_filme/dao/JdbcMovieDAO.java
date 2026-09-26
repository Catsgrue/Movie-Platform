package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.daoInterfaces.MovieDAO;
import org.example.proiect_sgbd_vizionare_filme.entities.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMovieDAO implements MovieDAO {

    private Connection con;

    public JdbcMovieDAO(Connection con) {
        this.con = con;
    }

    @Override
    public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM MOVIES";
        List<Movie> movies = new ArrayList<>();

        try (PreparedStatement p = con.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id_movie"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("id_genre"),
                        rs.getInt("movie_duration"),
                        rs.getDate("release_date"),
                        rs.getDouble("rating_mediu"),
                        rs.getString("image_url")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    @Override
    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM MOVIES WHERE id_movie=?";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id_movie"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("id_genre"),
                            rs.getInt("movie_duration"),
                            rs.getDate("release_date"),
                            rs.getDouble("rating_mediu"),
                            rs.getString("image_url")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getRecommendations(int idUser) {
        List<String> recommendedTitles = new ArrayList<>();
        String sql = "{call get_recommendations(?, ?, ?, ?)}";

        try (CallableStatement stmt = con.prepareCall(sql)) {

            stmt.setInt(1, idUser);

            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.VARCHAR);

            stmt.execute();

            String rec1 = stmt.getString(2);
            String rec2 = stmt.getString(3);
            String rec3 = stmt.getString(4);

            if (rec1 != null && !rec1.equalsIgnoreCase("N/A")) recommendedTitles.add(rec1);
            if (rec2 != null && !rec2.equalsIgnoreCase("N/A")) recommendedTitles.add(rec2);
            if (rec3 != null && !rec3.equalsIgnoreCase("N/A")) recommendedTitles.add(rec3);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return recommendedTitles;
    }

    @Override
    public List<Movie> getWatchedMoviesByUser(int idUser) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.id_movie, m.title, m.description, m.id_genre, m.movie_duration, m.release_date, m.rating_mediu " +
                "FROM MOVIES m " +
                "JOIN MOVIE_VERSIONS mv ON m.id_movie = mv.id_movie " +
                "JOIN VIEWS v ON mv.id_version = v.id_version " +
                "WHERE v.id_user = ? " +
                "GROUP BY m.id_movie, m.title, m.description, m.id_genre, m.movie_duration, m.release_date, m.rating_mediu " +
                "ORDER BY MAX(v.view_date) DESC";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, idUser);

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    movies.add(new Movie(
                            rs.getInt("id_movie"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("id_genre"),
                            rs.getInt("movie_duration"),
                            rs.getDate("release_date"),
                            rs.getDouble("rating_mediu"),
                            rs.getString("image_url")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la extragerea istoricului: " + e.getMessage());
        }
        return movies;
    }
}