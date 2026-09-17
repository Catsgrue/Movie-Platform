package org.example.proiect_sgbd_vizionare_filme.DAO;

import org.example.proiect_sgbd_vizionare_filme.concrete_classes.Review;

import java.util.List;

public interface ReviewDAO {

    void addReview(Review review);
    void insertReview(int idView, int rating, String commentText, String predefinedOption);
    List<Review> getReviewsByMovieId(int id_movie);
}
