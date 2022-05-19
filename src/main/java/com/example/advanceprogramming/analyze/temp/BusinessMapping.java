package com.example.advanceprogramming.analyze.temp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BusinessMapping {
    private String business_id, name, address, city, state, categories, postal_code;
    private double latitude, longitude, stars;
    private int review_count, is_open;
    private Object attributes, hours;

}
