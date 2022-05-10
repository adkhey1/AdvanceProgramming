package com.example.advanceprogramming.analyze.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class BusinessDTO {
    private String business_id, name, address, city, state, postal_code, attributes, categories, hours;
    private double stars, latitude, longitude;
    private int review_count, is_open;

}
