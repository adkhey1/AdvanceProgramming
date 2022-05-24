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
    private String business_id, name, address, city, state, attributes, categories, hours, postal_code;
    private double stars,spring, summer, fall, winter;
    private int review_count, is_open;

    //analysis


    //ob es in der Umgebung mehrere Restaurants von der categorie gibt mit postcode schauen
    private HashMap<String, Integer> countCategorie;
    //categorie und wie gut es in dieser ist (mit sternen schauen)


}