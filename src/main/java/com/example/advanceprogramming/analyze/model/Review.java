package com.example.advanceprogramming.analyze.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity

public class Review {

    @Id
    @Column(name = "review_id")
    private String review_id;



}
