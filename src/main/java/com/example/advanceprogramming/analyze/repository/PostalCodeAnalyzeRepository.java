package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.PostalCodeAnalyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostalCodeAnalyzeRepository extends JpaRepository<PostalCodeAnalyze, Long> {

    @Query(value = "SELECT count(*) FROM postalcodeanalyze p WHERE p.categories LIKE %?1% " +
            "and p.postal_code LIKE %?2%",
            nativeQuery = true)
    Integer selectAllCategories(String categorie, String postal_code);

    @Query(value = "SELECT count(*) FROM postalcodeanalyze p ", nativeQuery = true)
    Integer selectAll();


}
