package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
@Repository
public interface RestaurantRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business b WHERE b.business_id = ?1")
    Business findByBusiness_id(String business_id);

    @Query("SELECT b FROM Business b WHERE b.name = ?1")
    Business findByName(String name);


    @Query("SELECT b.name as franchise , count(b.name) as number FROM Business b WHERE number > 296" +
            "GROUP BY franchise  ORDER BY number DESC")
    HashMap<String, Integer> findBiggestFranchises();

}