package com.example.advanceprogramming.auth.repository;


import com.example.advanceprogramming.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//SAVE AND LOAD FROM DATABASE
public interface UserRepository extends JpaRepository<User,Long> {

    //List<User> findExistingEmail(String email);

    //to find a User by email
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);
}
