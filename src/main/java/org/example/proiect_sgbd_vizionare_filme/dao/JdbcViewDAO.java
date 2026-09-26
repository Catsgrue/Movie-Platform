package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.daoInterfaces.ViewDAO;

import java.sql.*;

public class JdbcViewDAO implements ViewDAO {

    private Connection con;

    public JdbcViewDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void createView(int id_user, int id_version, int watched_minutes) {
        String sql = "INSERT INTO VIEWS (id_view,id_user,id_version,watched_minutes,view_status) VALUES (seq_views.NEXTVAL,?,?,?,?)";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_user);
            p.setInt(2, id_version);
            p.setInt(3, watched_minutes);
            p.setString(4, "PENDING");

            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getPreviousWatchedMinutes(int id_user, int id_version) {
        String sql = "SELECT NVL(MAX(watched_minutes), 0) AS max_minutes " +
                "FROM VIEWS " +
                "WHERE id_user = ? AND id_version = ? AND view_status = 'IN_PROGRESS'";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_user);
            p.setInt(2, id_version);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_minutes");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public int getLastViewIdForMovie(int id_user, int id_movie) {
        String sql = "SELECT MAX(v.id_view) as last_view_id " +
                "FROM VIEWS v " +
                "JOIN MOVIE_VERSIONS mv ON v.id_version = mv.id_version " +
                "WHERE v.id_user = ? AND mv.id_movie = ?";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_user);
            p.setInt(2, id_movie);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("last_view_id");
                    return (id > 0) ? id : -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}