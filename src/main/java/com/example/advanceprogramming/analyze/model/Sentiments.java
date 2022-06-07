package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "senttab")
public class Sentiments {

    @Id
    @Column(name = "Id")
    int id;

    @Column(name = "review_id")
    String review_id;

    @Column(name = "user_id")
    String user_id;

    @Column(name = "business_id")
    String business_id;

    @Column(name = "stars")
    double stars;

    @Column(name = "normalStars")
    double normalStars;

    @Column(name = "sentimentNormal")
    double sentimentNormal;

}
