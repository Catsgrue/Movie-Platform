package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Review;

import java.util.List;

public interface ReviewDAO {

    void addReview(Review review);
    void insertReview(int idView, int rating, String commentText, String predefinedOption);
    List<Review> getReviewsByMovieId(int id_movie);
}
