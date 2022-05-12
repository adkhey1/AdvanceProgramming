package com.example.advanceprogramming.analyze.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class CategoriesID implements Serializable {
    private String business_id, category;

}
