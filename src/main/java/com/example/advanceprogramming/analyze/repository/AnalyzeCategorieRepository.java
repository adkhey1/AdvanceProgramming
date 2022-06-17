package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.AnalyzeCategorie;
import com.example.advanceprogramming.analyze.model.CategorieAnaylzeResult;
import com.example.advanceprogramming.analyze.model.StateAnalyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyzeCategorieRepository extends JpaRepository<AnalyzeCategorie, Long> {

    @Query(value = "SELECT count(a.city) as city, count(a.state) as state, count(a.postal_code) as postalcode" +
            "FROM analyzecategorie a WHERE a.categories LIKE %?1% and a.city LIKE %?2% and a.state Like %?2% and a.postalcode Like %?3%",
            nativeQuery = true)
    CategorieAnaylzeResult selectAllCounts(String categorie, String city, String state, String postal_code);

}
