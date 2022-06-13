package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.SentimentFranchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentimentFranchiseRepository extends JpaRepository<SentimentFranchise, Long>{

    @Query(value = "Select avg(s.sentiment) from sentimentfranchise s where s.name = ?1",
            nativeQuery = true)
    double getAvgSentiment(String name);

}
