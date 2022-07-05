package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.UserBusinessRelation;
import com.example.advanceprogramming.analyze.model.UserBusinessRelationID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBusinessRelationRepository extends JpaRepository<UserBusinessRelation, UserBusinessRelationID> {

    @Query("SELECT u FROM UserBusinessRelation u WHERE u.userId = ?1 AND u.businessId = ?2")
    UserBusinessRelation findByNameAndBusinessId(long userId, String business_id);


    @Query("SELECT u.businessId FROM UserBusinessRelation u WHERE u.userId = ?1")
    List<String> selectAllBusinessIDFromUser(long userId);

    @Query("SELECT u FROM UserBusinessRelation u WHERE u.userId = ?1")
    List<UserBusinessRelation> selectAllFromUser(long userId);

    /*

    @Modifying
    @Query("DELETE FROM UserBusinessRelation u WHERE u.userId = ?1")
    void deleteAllByUserId(long userId);

    */

}

