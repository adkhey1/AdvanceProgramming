package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.StateAnalyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateAnalyzeRepository extends JpaRepository<StateAnalyze, Long> {


    @Query(value = "SELECT count(*) FROM stateanalyze s WHERE s.categories LIKE %?1% " +
            "and s.state LIKE %?2%",
            nativeQuery = true)
    Integer selectAllState(String categorie, String state);

}
