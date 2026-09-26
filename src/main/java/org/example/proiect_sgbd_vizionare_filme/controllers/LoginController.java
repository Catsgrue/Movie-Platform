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
import org.example.proiect_sgbd_vizionare_filme.services.UserService;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private Button loginButton;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        try {
            List<String> userNames = userService.getAllUserNames();
            userComboBox.getItems().addAll(userNames);
        } catch (Exception e) {

            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Database failure");
            alert.setContentText("The server is shut down.");
            alert.showAndWait();

            System.err.println("Critical error loading users: " + e.getMessage());
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String selectedUser = userComboBox.getValue();

        if (selectedUser == null || selectedUser.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Login failed");
            alert.setContentText("Please select an account from the list to continue!");
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