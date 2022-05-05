package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business b WHERE b.business_id = ?1")
    Business findByBusiness_id(String business_id);

    @Query("SELECT b FROM Business b WHERE b.name = ?1")
    List<Business> findByName(String name);

    @Query("SELECT b.name as franchise , count(b.name) as number FROM Business b WHERE number > 296" +
            "GROUP BY franchise  ORDER BY number DESC")
    HashMap<String, Integer> findBiggestFranchises();

    @Query("SELECT b.business_id as id, b.attributes as attributes FROM Business b WHERE b.stars = 1")
    ArrayList<String> selectAllFromBusiness();


}