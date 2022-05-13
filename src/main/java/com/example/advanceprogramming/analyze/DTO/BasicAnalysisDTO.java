package com.example.advanceprogramming.analyze.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class BasicAnalysisDTO {

    //attributes
    private String name, address, city, state, attributes, categories, hours;
    private double stars;
    private int review_count, is_open;

    //analysis


    //ob es in der Umgebung mehrere Restaurants von der categorie gibt mit postcode schauen
    private HashMap<String, Integer> countCategorie;
    //categorie und wie gut es in dieser ist (mit sternen schauen)


}