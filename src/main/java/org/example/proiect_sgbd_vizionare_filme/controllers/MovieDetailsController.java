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
import org.example.proiect_sgbd_vizionare_filme.JdbcDAO.*;
import org.example.proiect_sgbd_vizionare_filme.concrete_classes.*;
import org.example.proiect_sgbd_vizionare_filme.database.Database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Random;

public class MovieDetailsController {

    @FXML
    private ImageView moviePoster;

    @FXML
    private HBox castContainer;

    @FXML
    private Label star1;

    @FXML
    private Label star2;

    @FXML
    private Label star3;

    @FXML
    private Label star4;

    @FXML
    private Label star5;

    @FXML
    private ComboBox<String> predefinedOptionComboBox;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> formatComboBox;


    @FXML
    private TextArea commentArea;

    @FXML
    private VBox reviewsListContainer;

    @FXML
    private Label titleLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label usernameLabel;


    private String currentUser;
    private int id_movie;
    private int currentRating = 0;

    @FXML
    public void initialize(int id,String currentUser) throws Exception {

        this.currentUser=currentUser;
        this.id_movie=id;

        setCurrentUser();
        setMovieDetails(id);
        setFormatComboBox(id);
        setLanguageComboBox(id);
        populatePredefinedOptions();
        populateCast(id);

        loadReviews();
    }

    public void populateCast(int id) throws Exception{
        Connection con=Database.getConnection();
        JdbcMovieCastDAO movieCastDAO=new JdbcMovieCastDAO(con);

        List<Integer> idActors=movieCastDAO.getId_ActorsByMovieId(id);

        if(idActors!=null){
            JdbcActorDAO actorDAO=new JdbcActorDAO(con);
            List<Actor> actors=actorDAO.getActorsById_Actors(idActors);

            for(Actor a:actors){
                VBox actorCard = new VBox(10);
                actorCard.setAlignment(Pos.CENTER);
                actorCard.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-background-radius: 8;");

                ImageView posterView = new ImageView();
                posterView.setFitWidth(160);
                posterView.setFitHeight(240);
                posterView.setPreserveRatio(true);

                String imagePath = "/org/example/actor-posters/" + a.getStage_name() + ".jpg";
                InputStream imageStream = getClass().getResourceAsStream(imagePath);

                if (imageStream != null) {
                    posterView.setImage(new Image(imageStream));
                } else {
                    InputStream defaultStream = getClass().getResourceAsStream("/org/example/actor-posters/default_poster.jpg");
                    if(defaultStream != null) {
                        posterView.setImage(new Image(defaultStream));
                    }
                }

                String role=movieCastDAO.getRoleByIdActor(a.getId_actor(),id);
                if (role != null) {
                    Label titleLabel = new Label(a.getStage_name() + "(" + role + ")");
                    titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
                    titleLabel.setMaxWidth(160);
                    titleLabel.setWrapText(true);
                    titleLabel.setAlignment(Pos.CENTER);

                    actorCard.getChildren().addAll(posterView, titleLabel);
                    castContainer.getChildren().add(actorCard);

                }else continue;
            }
        }else return ;
    }

    public void populatePredefinedOptions() {
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

    public void setFormatComboBox(int id_movie) throws Exception {
        Connection con = Database.getConnection();
        JdbcVersionDAO versionDAO = new JdbcVersionDAO(con);

        List<String> formats = versionDAO.getFormatsByMovieId(id_movie);

        formatComboBox.getItems().clear();
        formatComboBox.getItems().addAll(formats);
    }

    public void setLanguageComboBox(int id_movie) throws Exception {
        Connection con = Database.getConnection();
        JdbcVersionDAO versionDAO = new JdbcVersionDAO(con);

        List<String> languages = versionDAO.getLanguagesByMovieId(id_movie);

        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll(languages);
    }

    public void setMovieDetails(int id) throws Exception {
        Connection con= Database.getConnection();
        JdbcMovieDAO movieDAO=new JdbcMovieDAO(con);
        JdbcGenreDAO genreDAO=new JdbcGenreDAO(con);

        Movie movie=movieDAO.getMovieById(id);

        if(movie!=null){
            titleLabel.setText(movie.getTitle());
            ratingLabel.setText(Double.toString(movie.getRating_mediu()));
            descriptionLabel.setText(movie.getDescription());
            yearLabel.setText("Release date: " + movie.getRelease_date());

            String imagePath = "/org/example/movie-posters/" + movie.getTitle() + ".jpg";
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                moviePoster.setImage(new Image(imageStream));
            } else {
                InputStream defaultStream = getClass().getResourceAsStream("/org/example/movie-posters/default_poster.jpg");
                if(defaultStream != null) {
                    moviePoster.setImage(new Image(defaultStream));
                }
            }

            Genre genre=genreDAO.getGenreById(movie.getId_genre());
            if(genre!=null){
               genreLabel.setText("Genre: "+genre.getGenre_name());
            } else return ;
        }else return ;
    }

    public void setCurrentUser(){
        usernameLabel.setText(this.currentUser);
    }

    @FXML
    public void handleSubmitReview(ActionEvent event) throws Exception {
        if (currentRating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Missing Rating");
            alert.setContentText("Please select a star rating (1-5) before submitting your review.");
            alert.showAndWait();
            return;
        }

        Connection con = Database.getConnection();
        JdbcUserDAO userDAO = new JdbcUserDAO(con);
        JdbcViewDAO viewDAO = new JdbcViewDAO(con);
        JdbcReviewDAO reviewDAO = new JdbcReviewDAO(con);

        User loggedInUser = userDAO.getUserByName(currentUser);

        if (loggedInUser != null) {
            int id_view = viewDAO.getLastViewIdForMovie(loggedInUser.getId_user(), this.id_movie);

            if (id_view == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Viewing Required");
                alert.setContentText("You cannot review a movie you haven't watched. Please press PLAY first.");
                alert.showAndWait();
                return;
            }

            String commentText = commentArea.getText();
            String predefinedOption = predefinedOptionComboBox.getValue();

            if (commentText != null && commentText.trim().isEmpty()) {
                commentText = null;
            }
            if (predefinedOption == null || predefinedOption.trim().isEmpty()) {
                predefinedOption = "General";
            }

            reviewDAO.insertReview(id_view, currentRating, commentText, predefinedOption);

            commentArea.clear();
            predefinedOptionComboBox.setValue(null);
            currentRating = 0;

            Label[] stars = {star1, star2, star3, star4, star5};
            for (Label star : stars) {
                star.setText("☆");
            }

            loadReviews();
        }
    }

    public void loadReviews() throws Exception {
        if (reviewsListContainer.getChildren().size() > 1) {
            reviewsListContainer.getChildren().remove(1, reviewsListContainer.getChildren().size());
        }

        Connection con = Database.getConnection();
        JdbcReviewDAO reviewDAO = new JdbcReviewDAO(con);
        JdbcUserDAO userDAO = new JdbcUserDAO(con);

        List<Review> reviews = reviewDAO.getReviewsByMovieId(this.id_movie);

        for (Review review : reviews) {
            VBox reviewCard = new VBox(5);
            reviewCard.setPadding(new javafx.geometry.Insets(15));

            String borderColor = "#333333";
            String backgroundColor = "#1e1e1e";

            if ("Positive".equalsIgnoreCase(review.getSentiment_score())) {
                borderColor = "#2e7d32";
                backgroundColor = "#1b2e1b";
            } else if ("Negative".equalsIgnoreCase(review.getSentiment_score())) {
                borderColor = "#c62828";
                backgroundColor = "#331616";
            } else if ("Neutral".equalsIgnoreCase(review.getSentiment_score())) {
                borderColor = "#757575";
                backgroundColor = "#2a2a2a";
            }

            reviewCard.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: 2; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; " +
                    "-fx-background-color: " + backgroundColor + ";");

            String userName = userDAO.getUserFullNameByViewId(review.getId_view());

            Label nameAndRatingLabel = new Label(userName + " • Rating: " + review.getRating() + "/5 ★");
            nameAndRatingLabel.setStyle("-fx-text-fill: #e5e5e5; -fx-font-weight: bold; -fx-font-size: 14px;");

            String displayText = (review.getComment_text() != null && !review.getComment_text().trim().isEmpty())
                    ? review.getComment_text()
                    : "Quick Tag: " + review.getPredefined_option();

            Label textLabel = new Label(displayText);
            textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
            textLabel.setWrapText(true);

            reviewCard.getChildren().addAll(nameAndRatingLabel, textLabel);
            reviewsListContainer.getChildren().add(reviewCard);
        }
    }

    @FXML
    public void handleWatchMovie(ActionEvent event) throws Exception {

        String language=languageComboBox.getValue();
        String format=formatComboBox.getValue();

        if(language==null || format==null || language.trim().isEmpty() || format.trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention!");
            alert.setHeaderText("Format or Language Empty");
            alert.setContentText("You must choose a format and a language in order to watch the movie!");
            alert.showAndWait();

            return ;
        }

        Connection con=Database.getConnection();
        JdbcVersionDAO versionDAO=new JdbcVersionDAO(con);

        int id_version=versionDAO.getIdVersionByIdMovie(id_movie,format,language);

        if (id_version != -1) {
            JdbcUserDAO userDAO = new JdbcUserDAO(con);
            User user = userDAO.getUserByName(currentUser);

            if (user != null) {
                JdbcViewDAO viewDAO = new JdbcViewDAO(con);
                JdbcMovieDAO movieDAO = new JdbcMovieDAO(con);
                Random random = new Random();

                Movie movie = movieDAO.getMovieById(id_movie);

                int movie_duration = movie.getMovie_duration();
                int id_user = user.getId_user();

                int previous_minutes = viewDAO.getPreviousWatchedMinutes(id_user, id_version);
                int watched_minutes;

                if (previous_minutes >= movie_duration) {
                    watched_minutes = movie_duration;

                } else if (previous_minutes > 0) {
                    int min = previous_minutes + 1;
                    int max = movie_duration;
                    watched_minutes = random.nextInt(max - min + 1) + min;

                } else {
                    watched_minutes = random.nextInt(movie_duration) + 1;

                }

                viewDAO.createView(id_user, id_version, watched_minutes);

                if(watched_minutes!=movie_duration)
                {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success!");
                    successAlert.setHeaderText("Enjoy the movie!");
                    successAlert.setContentText("You watched " + watched_minutes + " minutes of the movie.");
                    successAlert.showAndWait();
                }else{
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success!");
                    successAlert.setHeaderText("Hope you enjoyed the movie!");
                    successAlert.setContentText("You watched the whole movie!🥳");
                    successAlert.showAndWait();
                }
            }
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

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/example/proiect_sgbd_vizionare_filme/dashboard.fxml"));
        Parent root=loader.load();

        DashBoardController dashBoardController=loader.getController();
        dashBoardController.setLoggedInUser(currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
