package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name = "postalcodeanalyze")
@IdClass(PostalCodeAnalyzeID.class)
public class PostalCodeAnalyze implements Serializable {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "categories")
    private String categories;

}
