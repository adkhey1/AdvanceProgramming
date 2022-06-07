package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Franchise;
import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult;
import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {


    @Query(value = "SELECT f.name as name1, count(f.name) as counter FROM franchise f GROUP BY name1 ORDER BY counter DESC", nativeQuery = true)
    List<FranchiseAnalyzeResult> findBiggestFranchises();

    @Query(value = "SELECT f.city as name1, avg(f.stars) as counter FROM franchise f Where f.name Like ?1" +
            " group by name1 order by counter DESC LIMIT 5",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> averageStarsByCity(String franchiseName);


    @Query(value = "SELECT f.city as name1, avg(f.stars) as counter FROM franchise f WHERE f.name = ?1 " +
            "group by name1 order by counter ASC LIMIT 5",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> averageStarsByCityWorst(String franchiseName);

    @Query(value = "SELECT count(*) as name1, sum(f.review_count) as counter FROM franchise f WHERE f.name = ?1 " +
            "and f.city IN (?2, ?3, ?4, ?5, ?6) ",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> countReviews(String franchiseName, String city1, String city2, String city3, String city4, String city5);

    @Query(value = "SELECT f.city as name1, count(*) as counter FROM franchise f WHERE f.name = ?1 group by name1" +
            " order by counter DESC LIMIT 10", nativeQuery = true)
    List<FranchiseAnalyzeResult> storesInCity(String franchiseName);

    @Query(value = "SELECT count(f.categories) FROM franchise f WHERE f.name = ?1 and f.categories LIKE %?2%",
            nativeQuery = true)
    Integer basicCategorie(String franchiseName, String categorie);

    @Query(value = "SELECT * FROM franchise WHERE name LIKE %?1% LIMIT 10",
            nativeQuery = true)
    List<Franchise> selectFirst10(String franchiseName);

    @Query(value = "SELECT f.name as name1, avg(f.stars) as counter FROM franchise f group by name1",
            nativeQuery = true)
    List<FranchiseAnalyzeResult> eachAverage();

}
