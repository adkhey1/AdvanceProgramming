package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Categories;
import com.example.advanceprogramming.analyze.model.CategoriesID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, CategoriesID> {

    @Query( value = "SELECT * FROM Categories",
            nativeQuery = true)
    List<Categories> selectAll();


}
