package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Actor;

import java.util.List;

public interface ActorDAO {

    List<Actor> getActorsById_Actors(List<Integer> idActors);

}
