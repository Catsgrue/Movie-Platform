package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDAO implements UserDAO {

    private Connection con;

    public JdbcUserDAO(Connection con) {
        this.con = con;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();

        try (PreparedStatement p = con.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_user"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("city")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    @Override
    public User getUserByName(String name) {
        String[] userName = name.split(" ");
        String first_name = userName[0];
        String last_name = userName[1];

        String sql = "SELECT id_user,email,city FROM USERS WHERE first_name=? AND last_name=?";
        User user = null;

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, first_name);
            p.setString(2, last_name);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id_user"),
                            first_name,
                            last_name,
                            rs.getString("email"),
                            rs.getString("city")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public String getUserFullNameByViewId(int id_view) {
        String sql = "SELECT u.first_name, u.last_name " +
                "FROM USERS u " +
                "JOIN VIEWS v ON u.id_user = v.id_user " +
                "WHERE v.id_view = ?";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_view);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("first_name") + " " + rs.getString("last_name");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Utilizator Necunoscut";
    }
}