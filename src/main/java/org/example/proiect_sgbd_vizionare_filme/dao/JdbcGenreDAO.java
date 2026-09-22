package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcGenreDAO implements GenreDAO {

    private Connection con;

    public JdbcGenreDAO(Connection con) {
        this.con = con;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql="SELECT * FROM GENRES";

        List<Genre> genres= new ArrayList<>();

        try(PreparedStatement p=con.prepareStatement(sql)){

            try(ResultSet rs=p.executeQuery()){
                while(rs.next()){
                    genres.add(new Genre(
                            rs.getInt("id_genre"),
                            rs.getString("genre_name")
                    ));
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return genres;
    }

    @Override
    public Genre getGenreByMovie(String title) {

        String sql="SELECT g.id_genre,g.genre_name FROM GENRES g JOIN MOVIES m ON g.id_genre=m.id_genre WHERE m.title=?";

        Genre genre=null;

        try(PreparedStatement p=con.prepareStatement(sql)){

            p.setString(1,title);

            try(ResultSet rs=p.executeQuery()){
                if(rs.next()){
                    genre=new Genre(
                            rs.getInt("id_genre"),
                            rs.getString("genre_name"));
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return genre;
    }

    @Override
    public Genre getGenreById(int id) {

        String sql="SELECT id_genre,genre_name FROM GENRES WHERE id_genre=?";
        Genre genre=null;

        try(PreparedStatement p=con.prepareStatement(sql)){

            p.setInt(1,id);

            try(ResultSet rs=p.executeQuery()){
                if(rs.next()){
                    genre=new Genre(
                            rs.getInt("id_genre"),
                            rs.getString("genre_name")
                    );
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return genre;
    }
}
