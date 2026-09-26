package org.example.proiect_sgbd_vizionare_filme.services;

import org.example.proiect_sgbd_vizionare_filme.config.Database;
import org.example.proiect_sgbd_vizionare_filme.dao.JdbcUserDAO;
import org.example.proiect_sgbd_vizionare_filme.entities.User;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    public List<String> getAllUserNames() throws Exception{
        try(Connection con= Database.getConnection()){
            JdbcUserDAO userDAO=new JdbcUserDAO(con);

            List<User> users=userDAO.getAllUsers();
            return users.stream()
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .collect(Collectors.toList());
        }
    }

}
