package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Attributes;
import com.example.advanceprogramming.analyze.model.AttributesID;
import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributesRepository extends JpaRepository<Attributes, AttributesID> {

    @Query(value = "SELECT count(*) FROM Attributes a WHERE a.aName LIKE %?1% AND a.content LIKE %True%" +
            " AND a.bID IN (SELECT b.business_id FROM Business b WHERE b.postal_code LIKE %?2%" +
            " AND b.categories LIKE %?3%)",
            nativeQuery = true)
    Integer selectByAttributes(String attribute, String postal_code, String categorie);


    /*
    @Query(value = "SELECT count(*) FROM Attributes a WHERE a.aName LIKE %?1%" +
            " AND a.bID IN (SELECT b.business_id FROM Business b WHERE b.postal_code LIKE %?2%)",
            nativeQuery = true)
    Integer selectByAttributes2(String attribute, String postal_code);
    */
}
