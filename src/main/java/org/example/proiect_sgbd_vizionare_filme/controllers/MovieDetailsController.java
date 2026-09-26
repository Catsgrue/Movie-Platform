package org.example.proiect_sgbd_vizionare_filme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.proiect_sgbd_vizionare_filme.entities.Actor;
import org.example.proiect_sgbd_vizionare_filme.entities.Genre;
import org.example.proiect_sgbd_vizionare_filme.entities.Movie;
import org.example.proiect_sgbd_vizionare_filme.entities.Review;
import org.example.proiect_sgbd_vizionare_filme.services.MovieDetailsService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MovieDetailsController {

    @FXML private ImageView moviePoster;
    @FXML private HBox castContainer;
    @FXML private Label star1, star2, star3, star4, star5;
    @FXML private ComboBox<String> predefinedOptionComboBox;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private TextArea commentArea;
    @FXML private VBox reviewsListContainer;
    @FXML private Label titleLabel, genreLabel, yearLabel, ratingLabel, descriptionLabel, usernameLabel;

    private String currentUser;
    private int id_movie;
    private int currentRating = 0;

    private final MovieDetailsService detailsService = new MovieDetailsService();

    @FXML
    public void initialize(int id, String currentUser) {
        this.currentUser = currentUser;
        this.id_movie = id;

        usernameLabel.setText(this.currentUser);
        populatePredefinedOptions();

        try {
            setMovieDetails(id);
            setFormatComboBox(id);
            setLanguageComboBox(id);
            populateCast(id);
            loadReviews();
        } catch (Exception e) {
            System.err.println("Error initializing the details page:" + e.getMessage());
        }
    }

    private void setMovieDetails(int id) throws Exception {
        Movie movie = detailsService.getMovieById(id);

        if (movie != null) {
            titleLabel.setText(movie.getTitle());
            ratingLabel.setText(Double.toString(movie.getRatingMediu()));
            descriptionLabel.setText(movie.getDescription());
            yearLabel.setText("Release date: " + movie.getReleaseDate());

            String imagePath = movie.getImageURL();

            if (imagePath == null || imagePath.trim().isEmpty()) {
                imagePath = "/org/example/movie-posters/default_poster.jpg";
            }

            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                moviePoster.setImage(new Image(imageStream));
            } else {
                InputStream defaultStream = getClass().getResourceAsStream("/org/example/movie-posters/default_poster.jpg");
                if (defaultStream != null) {
                    moviePoster.setImage(new Image(defaultStream));
                }
            }

            Genre genre = detailsService.getGenreById(movie.getId());
            if (genre != null) {
                genreLabel.setText("Genre: " + genre.getName());
            }
        }
    }

    private void setFormatComboBox(int id_movie) throws Exception {
        formatComboBox.getItems().clear();
        formatComboBox.getItems().addAll(detailsService.getFormats(id_movie));
    }

    private void setLanguageComboBox(int id_movie) throws Exception {
        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll(detailsService.getLanguages(id_movie));
    }

    private void populateCast(int id) throws Exception {
        List<Actor> actors = detailsService.getMovieActors(id);

        for (Actor a : actors) {
            VBox actorCard = new VBox(10);
            actorCard.setAlignment(Pos.CENTER);
            actorCard.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-background-radius: 8;");

            ImageView posterView = new ImageView();
            posterView.setFitWidth(160);
            posterView.setFitHeight(240);
            posterView.setPreserveRatio(true);

            String imagePath = a.getImageURL();

            if (imagePath == null || imagePath.trim().isEmpty()) {
                imagePath = "/org/example/actor-posters/default_poster.jpg";
            }

            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                posterView.setImage(new Image(imageStream));
            } else {
                InputStream defaultStream = getClass().getResourceAsStream("/org/example/actor-posters/default_poster.jpg");
                if (defaultStream != null) {
                    posterView.setImage(new Image(defaultStream));
                }
            }

            String role = detailsService.getActorRole(a.getId(), id);

            if (role != null) {
                Label nameLabel = new Label(a.getStageName() + " (" + role + ")");
                nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
                nameLabel.setMaxWidth(160);
                nameLabel.setWrapText(true);
                nameLabel.setAlignment(Pos.CENTER);

                actorCard.getChildren().addAll(posterView, nameLabel);
                castContainer.getChildren().add(actorCard);
            }
        }
    }

    private void populatePredefinedOptions() {
        predefinedOptionComboBox.getItems().clear();
        predefinedOptionComboBox.getItems().addAll(
                "An absolute masterpiece",
                "Great acting and visuals",
                "Excellent storytelling",
                "It was okay, neutral",
                "A bit boring",
                "Disappointing plot",
                "Terrible, total waste of time"
        );
    }

    @FXML
    public void handleSubmitReview(ActionEvent event) {
        if (currentRating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Missing Rating");
            alert.setContentText("Please select a star rating (1-5) before submitting your review.");
            alert.showAndWait();
            return;
        }

        try {
            detailsService.submitReview(
                    currentUser,
                    id_movie,
                    currentRating,
                    commentArea.getText(),
                    predefinedOptionComboBox.getValue()
            );

            commentArea.clear();
            predefinedOptionComboBox.setValue(null);
            currentRating = 0;
            updateStarUI();
            loadReviews();

        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Action Required");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadReviews() throws Exception {
        if (reviewsListContainer.getChildren().size() > 1) {
            reviewsListContainer.getChildren().remove(1, reviewsListContainer.getChildren().size());
        }

        List<Review> reviews = detailsService.getMovieReviews(this.id_movie);

        for (Review review : reviews) {
            VBox reviewCard = new VBox(5);
            reviewCard.setPadding(new javafx.geometry.Insets(15));

            String borderColor = "#333333";
            String backgroundColor = "#1e1e1e";
            String sentiment = review.getSentimentScore();

            if ("Positive".equalsIgnoreCase(sentiment)) {
                borderColor = "#2e7d32";
                backgroundColor = "#1b2e1b";
            } else if ("Negative".equalsIgnoreCase(sentiment)) {
                borderColor = "#c62828";
                backgroundColor = "#331616";
            } else if ("Neutral".equalsIgnoreCase(sentiment)) {
                borderColor = "#757575";
                backgroundColor = "#2a2a2a";
            }

            reviewCard.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: 2; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; " +
                    "-fx-background-color: " + backgroundColor + ";");

            String userName = detailsService.getUserNameForReview(review.getIdView());

            Label nameAndRatingLabel = new Label(userName + " • Rating: " + review.getRating() + "/5 ★");
            nameAndRatingLabel.setStyle("-fx-text-fill: #e5e5e5; -fx-font-weight: bold; -fx-font-size: 14px;");

            String commentText = review.getCommentText();
            String displayText = (commentText != null && !commentText.trim().isEmpty())
                    ? commentText
                    : "Quick Tag: " + review.getPredefinedOption();

            Label textLabel = new Label(displayText);
            textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
            textLabel.setWrapText(true);

            reviewCard.getChildren().addAll(nameAndRatingLabel, textLabel);
            reviewsListContainer.getChildren().add(reviewCard);
        }
    }

    @FXML
    public void handleWatchMovie(ActionEvent event) {
        try {
            int watchedMinutes = detailsService.watchMovie(
                    currentUser,
                    id_movie,
                    formatComboBox.getValue(),
                    languageComboBox.getValue()
            );

            Movie movie = detailsService.getMovieById(id_movie);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success!");

            if (watchedMinutes != movie.getMovieDuration()) {
                successAlert.setHeaderText("Enjoy the movie!");
                successAlert.setContentText("You watched " + watchedMinutes + " minutes of the movie.");
            } else {
                successAlert.setHeaderText("Hope you enjoyed the movie!");
                successAlert.setContentText("You watched the whole movie!🥳");
            }
            successAlert.showAndWait();

        } catch (IllegalArgumentException | IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention!");
            alert.setHeaderText("Action failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStarClick(MouseEvent event) {
        Label clickedStar = (Label) event.getSource();
        String starId = clickedStar.getId();
        this.currentRating = Integer.parseInt(starId.replace("star", ""));
        updateStarUI();
    }

    private void updateStarUI() {
        Label[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            if (i < currentRating) {
                stars[i].setText("★");
            } else {
                stars[i].setText("☆");
            }
        }
    }

    @FXML
    public void handleGoBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proiect_sgbd_vizionare_filme/dashboard.fxml"));
        Parent root = loader.load();

        DashBoardController dashBoardController = loader.getController();
        dashBoardController.setLoggedInUser(currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.show();
    }
}