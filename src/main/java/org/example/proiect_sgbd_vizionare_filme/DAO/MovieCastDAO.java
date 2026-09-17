package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Actor;

import java.util.List;

public interface MovieCastDAO {

    List<Integer> getId_ActorsByMovieId(int id);
    String getRoleByIdActor(int id_actor, int id_movie);

}
