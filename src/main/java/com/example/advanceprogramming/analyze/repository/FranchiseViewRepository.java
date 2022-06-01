package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Franchise;
import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FranchiseViewRepository extends JpaRepository<Franchise, Long> {


    @Query(value = "SELECT f.name as name, count(f.name) as counter FROM franchise f GROUP BY name ORDER BY counter DESC", nativeQuery = true)
    List<FranchiseAnalyzeResult> findBiggestFranchises();

    @Query(value = "SELECT avg(f.stars) FROM franchise f WHERE f.name = ?1",
            nativeQuery = true)
    double averageStars(String franchiseName);

    @Query(value = "SELECT f.city as name, avg(f.stars) as counter FROM franchise f WHERE f.name = ?1 " +
            "group by name order by counter DESC LIMIT 5",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> averageStarsByCity(String franchiseName);

    @Query(value = "SELECT f.city as name, avg(f.stars) as counter FROM franchise f WHERE f.name = ?1 " +
            "group by name order by counter ASC LIMIT 5",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> averageStarsByCityWorst(String franchiseName);

    @Query(value = "SELECT count(*) as name, sum(f.review_count) as counter FROM franchise f WHERE f.name = ?1 " +
            "and f.city IN (?2, ?3, ?4, ?5, ?6) ",
            nativeQuery = true)
    FranchiseAnalyzeResult countReviews(String franchiseName, String city1, String city2, String city3, String city4, String city5);

    @Query(value = "SELECT f.city as name, count(f.review_count) as counter FROM franchise f WHERE f.name = ?1 " +
            "and f.city IN (SELECT f.city FROM franchise f WHERE f.name = ?1 group by name " +
            "order by avg(f.stars) DESC LIMIT 5) group by name order by counter DESC LIMIT 5",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> reviewsInBestCity(String franchiseName);

    @Query(value = "SELECT f.city as name, count(*) as counter FROM franchise f WHERE f.name = ?1 group by name" +
            " order by counter DESC LIMIT 10", nativeQuery = true)
    List<FranchiseAnalyzeResult> storesInCity(String franchiseName);

    @Query(value = "SELECT count(f.categories) FROM franchise f WHERE f.name = ?1 and f.categories LIKE %?2%",
            nativeQuery = true)
    Integer basicCategorie(String franchiseName, String categorie);

    @Query(value = "SELECT * FROM franchise WHERE name LIKE %?1% LIMIT 10",
            nativeQuery = true)
    List<Franchise> selectFirst10(String franchiseName);

    @Query("SELECT count(name) FROM Franchise group by name")
    List<String> selectAllNames();

}
