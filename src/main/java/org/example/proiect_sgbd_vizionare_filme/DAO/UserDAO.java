package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();
    User getUserByName(String name);
    String getUserFullNameByViewId(int id_view);

}
