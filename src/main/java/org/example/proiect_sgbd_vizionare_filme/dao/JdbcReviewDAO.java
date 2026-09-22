package org.example.proiect_sgbd_vizionare_filme.dao;

import org.example.proiect_sgbd_vizionare_filme.entities.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcReviewDAO implements ReviewDAO {

    private Connection con;

    public JdbcReviewDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void addReview(Review review) {
        String sql = "INSERT INTO reviews (id_review,id_view,rating,comment_text,predefined_option,sentiment_score) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, review.getId_review());
            p.setInt(2, review.getId_view());
            p.setInt(3, review.getRating());
            p.setString(4, review.getComment_text());
            p.setString(5, review.getPredefined_option());
            p.setString(6, review.getSentiment_score());

            p.executeUpdate();
            System.out.println("Review was added!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertReview(int idView, int rating, String commentText, String predefinedOption) {
        String sql = "INSERT INTO REVIEWS (id_review, id_view, rating, comment_text, predefined_option) " +
                "VALUES (seq_reviews.NEXTVAL, ?, ?, ?, ?)";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, idView);
            p.setInt(2, rating);
            p.setString(3, commentText);
            p.setString(4, predefinedOption);

            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea recenziei: " + e.getMessage());
        }
    }

    @Override
    public List<Review> getReviewsByMovieId(int id_movie) {
        List<Review> reviewsList = new ArrayList<>();
        String sql = "SELECT r.id_review, r.id_view, r.rating, r.comment_text, r.predefined_option, r.sentiment_score " +
                "FROM REVIEWS r " +
                "JOIN VIEWS v ON r.id_view = v.id_view " +
                "JOIN MOVIE_VERSIONS mv ON v.id_version = mv.id_version " +
                "WHERE mv.id_movie = ? " +
                "ORDER BY r.id_review DESC";

        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.setInt(1, id_movie);

            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review(
                            rs.getInt("id_review"),
                            rs.getInt("id_view"),
                            rs.getInt("rating"),
                            rs.getString("comment_text"),
                            rs.getString("predefined_option"),
                            rs.getString("sentiment_score")
                    );
                    reviewsList.add(review);
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la extragerea recenziilor: " + e.getMessage());
        }

        return reviewsList;
    }
}