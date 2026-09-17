package org.example.proiect_sgbd_vizionare_filme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.proiect_sgbd_vizionare_filme.JdbcDAO.JdbcGenreDAO;
import org.example.proiect_sgbd_vizionare_filme.JdbcDAO.JdbcMovieDAO;
import org.example.proiect_sgbd_vizionare_filme.JdbcDAO.JdbcUserDAO;
import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Genre;
import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Movie;
import org.example.proiect_sgbd_vizionare_filme.concrete_classes.User;
import org.example.proiect_sgbd_vizionare_filme.database.Database;
import javafx.geometry.Pos;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.util.List;

public class DashBoardController {

    @FXML
    private VBox historySection;

    @FXML
    private HBox historyContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> genreFilterComboBox;

    @FXML
    private Label userNameLabel;

    @FXML
    private FlowPane moviesContainer;

    @FXML
    private Label recommendationMessageLabel;

    private String currentUser;
    private JdbcMovieDAO movieDAO;
    private JdbcGenreDAO genreDAO;

    public void setLoggedInUser(String username){
        this.currentUser = username;
        userNameLabel.setText(username);
        
        try {
            populateHistoryContainer();
        } catch (Exception e) {
            System.err.println("Eroare la popularea istoricului: " + e.getMessage());
        }
    }

    public void populateHistoryContainer() throws Exception {
        Connection con = Database.getConnection();
        JdbcUserDAO userDAO = new JdbcUserDAO(con);

        User user = userDAO.getUserByName(this.currentUser);
        if (user == null) return;

        JdbcMovieDAO mDAO = new JdbcMovieDAO(con);
        List<Movie> watchedMovies = mDAO.getWatchedMoviesByUser(user.getId_user());

        if (watchedMovies.isEmpty()) {
            historySection.setVisible(false);
            historySection.setManaged(false);
            return;
        }

        historySection.setVisible(true);
        historySection.setManaged(true);
        historyContainer.getChildren().clear();

        for (Movie m : watchedMovies) {
            VBox movieCard = new VBox(10);
            movieCard.setAlignment(Pos.CENTER);
            movieCard.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-background-radius: 8; -fx-cursor: hand;");

            ImageView posterView = new ImageView();
            posterView.setFitWidth(160);
            posterView.setFitHeight(240);
            posterView.setPreserveRatio(true);

            String imagePath = "/org/example/movie-posters/" + m.getTitle() + ".jpg";
            InputStream imageStream = getClass().getResourceAsStream(imagePath);

            if (imageStream != null) {
                posterView.setImage(new Image(imageStream));
            } else {
                InputStream defaultStream = getClass().getResourceAsStream("/org/example/movie-posters/default_poster.jpg");
                if (defaultStream != null) {
                    posterView.setImage(new Image(defaultStream));
                }
            }

            Label titleLabel = new Label(m.getTitle());
            titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            titleLabel.setMaxWidth(160);
            titleLabel.setWrapText(true);
            titleLabel.setAlignment(Pos.CENTER);

            movieCard.setOnMouseClicked(event -> {
                try {
                    clickMovieCard(event, m.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            movieCard.getChildren().addAll(posterView, titleLabel);
            historyContainer.getChildren().add(movieCard);
        }
    }

    public void populateMovieContainer() throws Exception {
        Connection con = Database.getConnection();
        movieDAO = new JdbcMovieDAO(con);

        List<Movie> movies = movieDAO.getAllMovies();

        moviesContainer.getChildren().clear();

        for (Movie m : movies) {
            VBox movieCard = new VBox(10);
            movieCard.setAlignment(Pos.CENTER);
            movieCard.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-background-radius: 8; -fx-cursor: hand;");

            ImageView posterView = new ImageView();
            posterView.setFitWidth(160);
            posterView.setFitHeight(240);
            posterView.setPreserveRatio(true);

            String imagePath = "/org/example/movie-posters/" + m.getTitle() + ".jpg";
            InputStream imageStream = getClass().getResourceAsStream(imagePath);

            if (imageStream != null) {
                posterView.setImage(new Image(imageStream));
            } else {
                InputStream defaultStream = getClass().getResourceAsStream("/org/example/movie-posters/default_poster.jpg");
                if(defaultStream != null) {
                    posterView.setImage(new Image(defaultStream));
                }
            }

            Label titleLabel = new Label(m.getTitle());
            titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            titleLabel.setMaxWidth(160);
            titleLabel.setWrapText(true);
            titleLabel.setAlignment(Pos.CENTER);

            movieCard.setOnMouseClicked(event -> {

                try {
                    clickMovieCard(event,m.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });

            movieCard.getChildren().addAll(posterView, titleLabel);
            moviesContainer.getChildren().add(movieCard);
        }
    }

    public void clickMovieCard(MouseEvent event, int id) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/example/proiect_sgbd_vizionare_filme/movie_details.fxml"));
        Parent root = loader.load();

        MovieDetailsController controller=loader.getController();
        controller.initialize(id,currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void populateGenreFilter() throws Exception{
        Connection con= Database.getConnection();
        genreDAO = new JdbcGenreDAO(con);

        genreFilterComboBox.getItems().add("All genres");
        List<Genre> genres= genreDAO.getAllGenres();
        for(var g:genres){
            genreFilterComboBox.getItems().add(g.getGenre_name());
        }

    }

    @FXML
    public void initialize() throws Exception {
        populateGenreFilter();
        populateMovieContainer();
    }


    @FXML
    public void getMoviesByGenre(){
        recommendationMessageLabel.setVisible(false);
        recommendationMessageLabel.setManaged(false);
        applyFilters();
    }

    @FXML
    public void getSearchedMovie(){
        recommendationMessageLabel.setVisible(false);
        recommendationMessageLabel.setManaged(false);
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase().trim();

        String selectedGenre = genreFilterComboBox.getValue();
        boolean showAllGenres = (selectedGenre == null || selectedGenre.equalsIgnoreCase("All genres"));
        if (selectedGenre != null) {
            selectedGenre = selectedGenre.toLowerCase();
        }


        boolean isDefaultState = searchText.isEmpty() && showAllGenres;
        boolean hasHistory = !historyContainer.getChildren().isEmpty();

        if (isDefaultState && hasHistory) {
            historySection.setVisible(true);
            historySection.setManaged(true);
        } else {
            historySection.setVisible(false);
            historySection.setManaged(false);
        }

        for (Node node : moviesContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox movieCard = (VBox) node;
                String movieTitle = "";
                String movieGenre = "";

                for (Node innerNode : movieCard.getChildren()) {
                    if (innerNode instanceof Label) {
                        Label label = (Label) innerNode;
                        movieTitle = label.getText();
                        break;
                    }
                }

                Genre foundGenre = genreDAO.getGenreByMovie(movieTitle);
                if (foundGenre != null) {
                    movieGenre = foundGenre.getGenre_name().toLowerCase();
                }

                boolean matchesText = searchText.isEmpty() || movieTitle.toLowerCase().contains(searchText);
                boolean matchesGenre = showAllGenres || movieGenre.equals(selectedGenre);

                if (matchesText && matchesGenre) {
                    movieCard.setVisible(true);
                    movieCard.setManaged(true);
                } else {
                    movieCard.setVisible(false);
                    movieCard.setManaged(false);
                }
            }
        }
    }

    @FXML
    public void handleRecommendations(ActionEvent event) throws Exception {

        historySection.setVisible(false);
        historySection.setManaged(false);

        Connection con = Database.getConnection();
        JdbcUserDAO userDAO = new JdbcUserDAO(con);

        User user = userDAO.getUserByName(this.currentUser);
        if (user == null) {
            System.err.println("Eroare: Utilizatorul curent nu a fost găsit.");
            return;
        }

        List<String> recommendedTitles = movieDAO.getRecommendations(user.getId_user());

        // 3. Modificăm starea elementului grafic de mesaj pentru a deveni vizibil și a ocupa spațiu
        recommendationMessageLabel.setVisible(true);
        recommendationMessageLabel.setManaged(true);

        for (Node node : moviesContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox movieCard = (VBox) node;
                String currentMovieTitle = "";

                for (Node innerNode : movieCard.getChildren()) {
                    if (innerNode instanceof Label) {
                        currentMovieTitle = ((Label) innerNode).getText();
                        break;
                    }
                }

                if (recommendedTitles.contains(currentMovieTitle)) {
                    movieCard.setVisible(true);
                    movieCard.setManaged(true);
                } else {
                    movieCard.setVisible(false);
                    movieCard.setManaged(false);
                }
            }
        }
    }
}