package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.daoInterfaces.ActorDAO;
import org.example.proiect_sgbd_vizionare_filme.entities.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcActorDAO implements ActorDAO {

    private Connection con;

    public JdbcActorDAO(Connection con) {
        this.con = con;
    }

    @Override
    public List<Actor> getActorsById_Actors(List<Integer> idActors) {

        String sql="SELECT * FROM ACTORS WHERE id_actor=?";

        List<Actor> actors=new ArrayList<>();

        try(PreparedStatement p=con.prepareStatement(sql)){


            for( var id: idActors){
                p.setInt(1,id);
                try(ResultSet rs=p.executeQuery()){
                    if(rs.next()){
                        actors.add(new Actor(
                                rs.getInt("id_actor"),
                                rs.getString("stage_name"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getDate("date_of_birth"),
                                rs.getString("image_url")
                        ));
                    }
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return actors;

    }
}
