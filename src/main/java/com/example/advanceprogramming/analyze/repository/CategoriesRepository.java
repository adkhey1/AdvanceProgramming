package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Categories;
import com.example.advanceprogramming.analyze.model.CategoriesID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, CategoriesID> {

    @Query( value = "SELECT * FROM Categories",
            nativeQuery = true)
    List<Categories> selectAll();

    @Query( value = "SELECT count(*) FROM Categories c WHERE c.category LIKE %?1% " +
            "and c.business_id IN (SELECT b.business_id FROM Business b WHERE  b.postal_code LIKE %?2%)",
            nativeQuery = true)
    Integer selectAllCategories( String categorie, String Postal_code);

    //List<Categories> findByBusiness_idIn(List<String> business_ids);


}
