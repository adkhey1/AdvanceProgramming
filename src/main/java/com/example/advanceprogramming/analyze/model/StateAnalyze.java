package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "stateanalyze")
public class StateAnalyze{

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "state")
    private String state;

    @Column(name = "categories")
    private String categories;

}