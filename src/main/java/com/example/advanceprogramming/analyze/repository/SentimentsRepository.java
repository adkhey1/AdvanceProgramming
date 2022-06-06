package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Sentiments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SentimentsRepository extends JpaRepository<Sentiments, Integer> {

    @Query(value = "SELECT s FROM Sentiments s ")
    List<Sentiments> selectAll();
}
