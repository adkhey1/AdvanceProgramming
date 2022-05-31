package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface FranchiseViewRepository extends JpaRepository<Franchise, Long>{


    @Query(value = "SELECT f.name, count(f.name) FROM franchise f" +
            "GROUP BY f.name ORDER BY count(f.name)  DESC", nativeQuery = true)
    HashMap<String, Integer> findBiggestFranchises();


    @Query(value = "SELECT avg(f.stars) FROM franchise f WHERE f.name = ?1",
            nativeQuery = true)
    double averageStars(String franchiseName);

    @Query(value = "SELECT f.city, count(*) as counter FROM franchise f WHERE f.name = ?1 group by f.city" +
            " order by counter DESC", nativeQuery = true)
    Map<String, Integer> storesInCity(String franchiseName);

    @Query(value = "SELECT count(f.categories) FROM franchise f WHERE f.name = ?1 and f.categories LIKE %?2%",
            nativeQuery = true)
    Integer basicCategorie(String franchiseName, String categorie);

    @Query(value = "SELECT * FROM franchise WHERE name LIKE %?1% LIMIT 10",
            nativeQuery = true)
    List<Franchise> selectFirst10(String franchiseName);


}
