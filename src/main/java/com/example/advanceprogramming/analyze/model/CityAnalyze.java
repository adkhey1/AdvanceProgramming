package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cityanalyze")
public class CityAnalyze {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "city")
    private String city;

    @Column(name = "categories")
    private String categories;

}