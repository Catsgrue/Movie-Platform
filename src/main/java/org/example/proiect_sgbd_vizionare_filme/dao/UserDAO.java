package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();
    User getUserByName(String name);
    String getUserFullNameByViewId(int id_view);

}
