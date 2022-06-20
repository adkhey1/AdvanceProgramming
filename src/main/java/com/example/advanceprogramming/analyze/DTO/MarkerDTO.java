package com.example.advanceprogramming.analyze.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MarkerDTO {
    private String business_id, name;
    private double latitude, longitude;


}
