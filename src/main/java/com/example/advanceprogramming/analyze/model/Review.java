package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "review_id")
    private String review_id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "business_id")
    private String business_id;

    @Column(name = "stars")
    private double stars;

    @Column(name = "useful")
    private int useful;

    @Column(name = "funny")
    private int funny;

    @Column(name = "cool")
    private int cool;

    @Column(name = "date")
    private LocalDateTime date;
}
