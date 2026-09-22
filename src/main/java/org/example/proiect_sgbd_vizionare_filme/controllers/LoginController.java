package org.example.proiect_sgbd_vizionare_filme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.proiect_sgbd_vizionare_filme.dao.JdbcUserDAO;
import org.example.proiect_sgbd_vizionare_filme.entities.User;
import org.example.proiect_sgbd_vizionare_filme.config.Database;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class LoginController {

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private Button loginButton;

    public void populateUserContainer() throws Exception {
        Connection con= Database.getConnection();
        JdbcUserDAO userDAO = new JdbcUserDAO(con);

        List<User> users= userDAO.getAllUsers();

        for( var u: users){
            userComboBox.getItems().add(u.getFirst_name() + " " + u.getLast_name());
        }
    }


    @FXML
    public void initialize() throws  Exception{
        populateUserContainer();

    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {

        String selectedUser = userComboBox.getValue();

        if (selectedUser == null || selectedUser.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenție");
            alert.setHeaderText("Logare eșuată");
            alert.setContentText("Te rog să selectezi un cont din listă pentru a continua!");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proiect_sgbd_vizionare_filme/dashboard.fxml"));
        Parent root = loader.load();

        DashBoardController dashboardController = loader.getController();
        dashboardController.setLoggedInUser(selectedUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}