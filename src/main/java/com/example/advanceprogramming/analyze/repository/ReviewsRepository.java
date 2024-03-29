package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r WHERE r.business_id = ?1")
    List<Review> selectReviewsWithBusinessId(String businessID);

    @Query(value = "SELECT r FROM Review r WHERE r.review_id = ?1")
    Review selectReviewWithId(String reviewId);

    @Query(value = "SELECT month(r.date) FROM Review r WHERE r.review_id = ?1")
    int selectDateWithId(String reviewId);


}
