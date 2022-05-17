package com.example.advanceprogramming.analyze.temp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ReviewMapping {
    private String review_id, user_id, business_id, text, date;
    private double stars;
    private int useful, funny, cool;
}
