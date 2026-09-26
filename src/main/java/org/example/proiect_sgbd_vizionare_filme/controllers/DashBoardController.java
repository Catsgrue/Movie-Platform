package org.example.proiect_sgbd_vizionare_filme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.proiect_sgbd_vizionare_filme.entities.Movie;
import org.example.proiect_sgbd_vizionare_filme.services.CatalogService;

import java.io.InputStream;
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
    private final CatalogService catalogService = new CatalogService();

    public void setLoggedInUser(String username) {
        this.currentUser = username;
        userNameLabel.setText(username);
        try {
            populateHistoryContainer();
        } catch (Exception e) {
            System.err.println("Error populating the history:" + e.getMessage());
        }
    }

    @FXML
    public void initialize() throws Exception {
        populateGenreFilter();
        populateMovieContainer(catalogService.getAllMovies());
    }

    private void populateHistoryContainer() throws Exception {
        List<Movie> watchedMovies = catalogService.getHistoryForUser(this.currentUser);

        if (watchedMovies.isEmpty()) {
            historySection.setVisible(false);
            historySection.setManaged(false);
            return;
        }

        historySection.setVisible(true);
        historySection.setManaged(true);
        historyContainer.getChildren().clear();

        for (Movie m : watchedMovies) {
            historyContainer.getChildren().add(createMovieCard(m));
        }
    }

    private void populateMovieContainer(List<Movie> movies) {
        moviesContainer.getChildren().clear();
        for (Movie m : movies) {
            moviesContainer.getChildren().add(createMovieCard(m));
        }
    }

    private void populateGenreFilter() throws Exception {
        genreFilterComboBox.getItems().add("All genres");
        genreFilterComboBox.getItems().addAll(catalogService.getAllGenreNames());
    }

    @FXML
    public void getMoviesByGenre() {
        applyFilters();
    }

    @FXML
    public void getSearchedMovie() {
        applyFilters();
    }

    private void applyFilters() {
        recommendationMessageLabel.setVisible(false);
        recommendationMessageLabel.setManaged(false);

        String searchText = searchField.getText();
        String selectedGenre = genreFilterComboBox.getValue();

        try {
            List<Movie> filteredMovies = catalogService.getFilteredMovies(searchText, selectedGenre);
            populateMovieContainer(filteredMovies);

            boolean isDefaultState = (searchText == null || searchText.isEmpty()) &&
                    (selectedGenre == null || selectedGenre.equalsIgnoreCase("All genres"));

            if (isDefaultState && !historyContainer.getChildren().isEmpty()) {
                historySection.setVisible(true);
                historySection.setManaged(true);
            } else {
                historySection.setVisible(false);
                historySection.setManaged(false);
            }
        } catch (Exception e) {
            System.err.println("Error applying filters:" + e.getMessage());
        }
    }

    @FXML
    public void handleRecommendations(ActionEvent event) {
        historySection.setVisible(false);
        historySection.setManaged(false);

        try {
            List<String> recommendedTitles = catalogService.getRecommendations(this.currentUser);
            List<Movie> allMovies = catalogService.getAllMovies();

            List<Movie> recommendedMovies = allMovies.stream()
                    .filter(m -> recommendedTitles.contains(m.getTitle()))
                    .toList();

            recommendationMessageLabel.setVisible(true);
            recommendationMessageLabel.setManaged(true);

            populateMovieContainer(recommendedMovies);
        } catch (Exception e) {
            System.err.println("Error generating recommendations:" + e.getMessage());
        }
    }


    private VBox createMovieCard(Movie m) {
        VBox movieCard = new VBox(10);
        movieCard.setAlignment(Pos.CENTER);
        movieCard.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-background-radius: 8; -fx-cursor: hand;");

        ImageView posterView = new ImageView();
        posterView.setFitWidth(160);
        posterView.setFitHeight(240);
        posterView.setPreserveRatio(true);

        String imagePath = m.getImageURL();

        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            posterView.setImage(new Image(imageStream));
        } else {
            InputStream defaultStream = getClass().getResourceAsStream("/org/example/movie-posters/default_poster.jpg");
            if (defaultStream != null) posterView.setImage(new Image(defaultStream));
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
                e.printStackTrace();
            }
        });

        movieCard.getChildren().addAll(posterView, titleLabel);
        return movieCard;
    }

    private void clickMovieCard(MouseEvent event, int id) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proiect_sgbd_vizionare_filme/movie_details.fxml"));
        Parent root = loader.load();

        MovieDetailsController controller = loader.getController();
        controller.initialize(id, currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.show();
    }
}