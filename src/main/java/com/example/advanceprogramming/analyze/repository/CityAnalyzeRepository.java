package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.CityAnalyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityAnalyzeRepository extends JpaRepository<CityAnalyze, Long> {

    @Query(value = "SELECT count(*) FROM cityanalyze c WHERE c.categories LIKE %?1% " +
            "and c.city LIKE %?2%",
            nativeQuery = true)
    Integer selectAllCity(String categorie, String city);


}
