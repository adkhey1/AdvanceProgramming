package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.AvgScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface avgScoreRepository extends JpaRepository<String, AvgScore> {

    @Query(value = "SELECT s FROM AvgScore s WHERE s.bId = ?1")
    AvgScore selectById(String input);
}
