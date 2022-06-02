package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.model.PostalCodeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface PostalCodeViewRepository extends JpaRepository<PostalCodeView, Long> {

    @Query(value = "SELECT count(*) FROM postalcodeview p WHERE p.categories LIKE %?1% " +
            "and p.postal_code LIKE %?2%",
            nativeQuery = true)
    Integer selectAllCategories(String categorie, String Postal_code);

    @Query(value = "SELECT count(*) FROM Business b WHERE b.attributes LIKE %?1% " +
            "and b.postal_code LIKE %?2% and b.categories LIKE %?3%",
            nativeQuery = true)
    Integer selectAllAttributes(String attribute, String Postal_code, String categorie);

}
