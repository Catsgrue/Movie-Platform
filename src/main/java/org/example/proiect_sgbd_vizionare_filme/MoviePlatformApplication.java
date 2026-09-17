package org.example.proiect_sgbd_vizionare_filme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MoviePlatformApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader= new FXMLLoader(MoviePlatformApplication.class.getResource("login.fxml"));
        Scene scene= new Scene(fxmlLoader.load());

        stage.setTitle("Movie Platform");
        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();
    }
}
