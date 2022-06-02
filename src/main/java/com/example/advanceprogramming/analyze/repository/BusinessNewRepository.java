package com.example.advanceprogramming.analyze.repository;

import com.example.advanceprogramming.analyze.model.BusinessNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessNewRepository extends JpaRepository<BusinessNew, Long> {


}