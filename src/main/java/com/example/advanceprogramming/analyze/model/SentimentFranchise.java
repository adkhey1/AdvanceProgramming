package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "sentimentfranchise")
public class SentimentFranchise {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "name")
    private String name;

    @Column(name = "sentiment")
    private double sentiment;

}
