package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Table(name = "avgscores")
@ToString
public class AvgScore {

    @Id
    @Column(name = "business_id")
    private String bId;

    @Column(name = "countspring")
    private int countspring;

    @Column(name = "countsummer")
    private int countsummer;

    @Column(name = "countfall")
    private int countfall;

    @Column(name = "countwinter")
    private int countwinter;

    @Column(name = "revspring")
    private double revspring;

    @Column(name = "revsummer")
    private double revsummer;

    @Column(name = "revfall")
    private double revfall;

    @Column(name = "revwinter")
    private double revwinter;

    @Column(name = "sentspring")
    private double sentspring;

    @Column(name = "sentsummer")
    private double sentsummer;

    @Column(name = "sentfall")
    private double sentfall;

    @Column(name = "sentwinter")
    private double sentwinter;

}
