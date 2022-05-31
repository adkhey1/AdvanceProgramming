package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "franchise")
@ToString
public class Franchise {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "stars")
    private double stars;

    @Column(name = "review_count")
    private int review_count;

    @Column(name = "is_open")
    private int is_open;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "categories")
    private String categories;

    @Column(name = "hours")
    private String hours;
}

