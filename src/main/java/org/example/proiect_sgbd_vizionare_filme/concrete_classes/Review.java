package org.example.proiect_sgbd_vizionare_filme.concrete_classes;

public class Review {

    private int id_review;
    private int id_view;
    private int rating;
    private String comment_text;
    private String predefined_option;
    private String sentiment_score;

    public Review(int id_review, int id_view, int rating, String comment_text, String predefined_option, String sentiment_score) {
        this.id_review = id_review;
        this.id_view = id_view;
        this.rating = rating;
        this.comment_text = comment_text;
        this.predefined_option = predefined_option;
        this.sentiment_score = sentiment_score;
    }

    public int getId_review() {
        return id_review;
    }

    public int getId_view() {
        return id_view;
    }

    public int getRating() {
        return rating;
    }

    public String getComment_text() {
        return comment_text;
    }

    public String getPredefined_option() {
        return predefined_option;
    }

    public String getSentiment_score() {
        return sentiment_score;
    }
}
