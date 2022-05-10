package com.example.advanceprogramming.analyze.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BasicAnalysisDTO {

    //attributes
    private String name, address, city, state, attributes, categories, hours;
    private double stars;
    private int review_count, is_open;

    //analysis

    //categorie und wie gut es in dieser ist (mit sternen schauen)
    //ob es in der Umgebung mehrere Restaurants von der Categorie gibt


}