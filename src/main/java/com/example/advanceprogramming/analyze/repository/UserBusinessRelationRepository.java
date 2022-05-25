package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.UserBusinessRelation;
import com.example.advanceprogramming.analyze.model.UserBusinessRelationID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBusinessRelationRepository extends JpaRepository<UserBusinessRelation, UserBusinessRelationID> {

    @Query("SELECT u.businessId FROM UserBusinessRelation u WHERE u.userId = ?1 AND u.isFavorite = true")
    List<String> selectAllFavoritesFromUser(long userId);

    @Query("SELECT u.businessId FROM UserBusinessRelation u WHERE u.userId = ?1 AND u.isFavorite = false")
    List<String> selectAllHistoryFromUser(long userId);

    @Query("SELECT u FROM UserBusinessRelation u WHERE u.userId = ?1")
    List<UserBusinessRelation> selectAllFromUser(long userId);

    @Query("SELECT u FROM UserBusinessRelation u WHERE u.userId = ?1 AND u.businessId = ?2")
    UserBusinessRelation findByNameAndBusinessId(long userId, String business_id);

    @Query("UPDATE UserBusinessRelation SET isFavorite = ?3 WHERE userId = ?1 AND businessId = ?2")
    Void changeFavorite(long userId, String business_id, Boolean favorite);


    /*
    @Query("SELECT u FROM UserBusinessRelation u WHERE u.userId = ?1 ")
    List<UserBusinessRelation> checkFavortie(long userId);

     */
}

