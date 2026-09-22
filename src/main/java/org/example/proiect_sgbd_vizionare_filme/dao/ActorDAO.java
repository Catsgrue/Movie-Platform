package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Actor;

import java.util.List;

public interface ActorDAO {

    List<Actor> getActorsById_Actors(List<Integer> idActors);

}
