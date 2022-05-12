package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "categories")
@IdClass(CategoriesID.class)
public class Categories implements Serializable {

    @Id
    @Column(name = "business_id")
    private String business_id;

    @Id
    @Column(name = "category")
    private String category;




}
