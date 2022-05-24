package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query(value = "SELECT * FROM Business b WHERE b.business_id = ?1",
    nativeQuery = true)
    Business findByBusiness_id(String business_id);

    @Query("SELECT b FROM Business b WHERE b.name = ?1")
    List<Business> findByName(String name);

    @Query(value = "SELECT b.name as franchise , count(b.name) as number FROM Business b" +
            "GROUP BY franchise  ORDER BY number DESC LIMIT 10", nativeQuery = true)
    HashMap<String, Integer> findBiggestFranchises();

    @Query(
            value = "SELECT * FROM Business b",
            nativeQuery = true)
    ArrayList<Business> selectAll();


    @Query(value = "SELECT * FROM Business b  LIMIT 100",
            nativeQuery = true)
    ArrayList<Business> selectfirst100();

    @Query("SELECT b FROM Business b WHERE b.categories LIKE %?1% AND b.city LIKE %?2% " +
            "AND b.stars >= ?3 AND b.postal_code LIKE %?4% AND b.is_open = ?5 AND b.review_count >= ?6")
    List<Business> selectByFilter(String categories, String city, double stars,
                                  String postal_code, int is_open, int review_count);

    @Query("SELECT b.business_id FROM Business b WHERE  b.postal_code LIKE %?1% ")
    List<String> selectByPostalCode(String postal_code);


}