package com.example.advanceprogramming.analyze.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BasicAnalysisDTO {

    //attributes
    private String business_id, name, address, city, state, attributes, categories, hours, postal_code;
    private double stars, spring, summer, fall, winter;
    private double sentSpring, sentSummer, sentFall, sentWinter;
    private int review_count, is_open;

    //analysis

    private HashMap<String, Integer> countPostalcode, countState, countCity;
    //categorie und wie gut es in dieser ist (mit sternen schauen)


}