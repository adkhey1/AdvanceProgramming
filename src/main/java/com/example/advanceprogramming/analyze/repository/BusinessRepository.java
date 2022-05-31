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

    /*
    @Query("SELECT b FROM Business b WHERE b.categories LIKE %?1% AND b.city LIKE %?2% " +
            "AND b.stars >= ?3 AND b.postal_code LIKE %?4% AND b.is_open = ?5 AND b.review_count >= ?6")
    List<Business> selectByFilter(String categories, String city, double stars,
                                  String postal_code, int is_open, int review_count);

     */



    //TODO  Create View hier in Java mit allen Restaurantes in der Postleitzahl mit dem den folgenden Attributen
    //TODO  und dem Namen der Postleitzahl und lösche es am Ende

    /*
    @Query(value = "CREATE VIEW ?1 AS SELECT b.business_id, b.postal_code, b.categories, b.attributes " +
            "FROM Business b WHERE b.postal_code = ?1")
    void createView(String postal_code);

    @Query(value = "DROP VIEW ?1")
    void deleteView(String postal_code);
     */


    /*
    @Query(value = "SELECT count(*) FROM Business b WHERE b.categories LIKE %?1% " +
            "and b.postal_code LIKE %?2%",
            nativeQuery = true)
    Integer selectAllCategories(String categorie, String Postal_code);

    @Query(value = "SELECT count(*) FROM Business b WHERE b.attributes LIKE %?1% " +
            "and b.postal_code LIKE %?2% and b.categories LIKE %?3%",
            nativeQuery = true)
    Integer selectAllAttributes(String attribute, String Postal_code, String categorie);
     */

}