package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, String> {

    @Query("SELECT r FROM Review r WHERE r.business_id = ?1")
    List<Review> selectReviewsWithBusinessId(String businessID);


}
