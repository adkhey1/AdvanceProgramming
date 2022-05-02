package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "business")
public class Business {

    @Id
    private String business_id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "stars")
    private String stars;

    @Column(name = "review_count")
    private String review_count;

    @Column(name = "is_open")
    private String is_open;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "categories")
    private String categories;

    @Column(name = "hours")
    private String hours;
}
