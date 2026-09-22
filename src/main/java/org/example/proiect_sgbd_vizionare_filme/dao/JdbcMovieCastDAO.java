package org.example.proiect_sgbd_vizionare_filme.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMovieCastDAO implements MovieCastDAO {

    private Connection con;

    public JdbcMovieCastDAO(Connection con) {
        this.con = con;
    }

    @Override
    public List<Integer> getId_ActorsByMovieId(int id) {
        String sql = "SELECT id_actor FROM MOVIE_CAST WHERE id_movie=?";
        List<Integer> idActors = new ArrayList<>();

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id);

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    idActors.add(rs.getInt("id_actor"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return idActors;
    }

    @Override
    public String getRoleByIdActor(int id_actor, int id_movie) {
        String sql = "SELECT role FROM MOVIE_CAST WHERE id_actor=? AND id_movie=?";
        String role = null;

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_actor);
            p.setInt(2, id_movie);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return role;
    }
}