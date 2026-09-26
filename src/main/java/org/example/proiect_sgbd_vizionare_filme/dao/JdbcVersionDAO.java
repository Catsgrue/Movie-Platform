package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.daoInterfaces.VersionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcVersionDAO implements VersionDAO {

    private Connection con;

    public JdbcVersionDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void createVersion(int id_movie, String format, String language) {
        String sql = "INSERT INTO MOVIE_VERSIONS (id_movie,format,language) VALUES (?,?,?)";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_movie);
            p.setString(2, format);
            p.setString(3, language);

            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getIdVersionByIdMovie(int id_movie, String format, String language) {
        String sql = "SELECT id_version FROM MOVIE_VERSIONS WHERE id_movie=? AND format=? AND language=?";
        int id_version = -1;

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_movie);
            p.setString(2, format);
            p.setString(3, language);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    id_version = rs.getInt("id_version");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id_version;
    }

    @Override
    public List<String> getFormatsByMovieId(int id_movie) {
        String sql = "SELECT DISTINCT format FROM MOVIE_VERSIONS WHERE id_movie = ?";
        List<String> formats = new ArrayList<>();

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_movie);

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    formats.add(rs.getString("format"));
                }
            }
        } catch (SQLException e) {
            System.out.println( e.getMessage());
        }

        return formats;
    }

    @Override
    public List<String> getLanguagesByMovieId(int id_movie) {
        String sql = "SELECT DISTINCT language FROM MOVIE_VERSIONS WHERE id_movie = ?";
        List<String> languages = new ArrayList<>();

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_movie);

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    languages.add(rs.getString("language"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return languages;
    }
}