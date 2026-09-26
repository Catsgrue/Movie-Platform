package org.example.proiect_sgbd_vizionare_filme.entities;

public class Review {

    private int id;
    private int idView;
    private int rating;
    private String commentText;
    private String predefinedOption;
    private String sentimentScore;

    public Review(int id, int idView, int rating, String commentText, String predefinedOption, String sentimentScore) {
        this.id = id;
        this.idView = idView;
        this.rating = rating;
        this.commentText = commentText;
        this.predefinedOption = predefinedOption;
        this.sentimentScore = sentimentScore;
    }

    public int getId() {
        return id;
    }

    public int getIdView() {
        return idView;
    }

    public int getRating() {
        return rating;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getPredefinedOption() {
        return predefinedOption;
    }

    public String getSentimentScore() {
        return sentimentScore;
    }
}
