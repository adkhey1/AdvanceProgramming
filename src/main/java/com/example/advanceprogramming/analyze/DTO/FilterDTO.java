package com.example.advanceprogramming.analyze.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FilterDTO {
    private String stars, name, time, day, state, city, plz, category, attribute;
}
