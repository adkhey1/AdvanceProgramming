package com.example.advanceprogramming.analyze.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class OnlyLatLongDTO {
    private HashMap<Double, Double> output;
}
