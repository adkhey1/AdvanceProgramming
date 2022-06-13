package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AvgScore {

    @Id
    @Column(name = "bID")
    private String bId;

    @Column(name = "springRev")
    private double springReviews;

    @Column(name = "summerRev")
    private double summerReviews;

    @Column(name = "fallRev")
    private double fallReviews;

    @Column(name = "winterRev")
    private double winterReviews;

    @Column(name = "sentSpring")
    private double sentSpring;

    @Column(name = "sentSummer")
    private double sentSummer;

    @Column(name = "sentFall")
    private double sentfall;

    @Column(name = "sentWinter")
    private double sentWinter;

}
