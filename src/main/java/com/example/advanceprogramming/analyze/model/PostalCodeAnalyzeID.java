package com.example.advanceprogramming.analyze.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor

public class PostalCodeAnalyzeID implements Serializable {
    private String business_id, postal_code, categories;
}
