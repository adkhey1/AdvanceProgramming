package com.example.advanceprogramming.analyze.DTO;

import com.example.advanceprogramming.analyze.model.Franchise;
import com.fasterxml.jackson.annotation.JsonGetter;
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
