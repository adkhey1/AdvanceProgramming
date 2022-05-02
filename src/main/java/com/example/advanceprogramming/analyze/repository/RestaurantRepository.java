package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;

public interface RestaurantRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business b WHERE b.business_id = ?1")
    Business findByBusiness_id(String business_id);


    @Query("SELECT b.name , count(b.name) FROM Business b " +
            "GROUP BY b.name  ORDER BY count(b.name)DESC")
    HashMap<String, Integer> findBiggest();

}