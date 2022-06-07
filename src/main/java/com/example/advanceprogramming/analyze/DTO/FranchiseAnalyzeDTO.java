package com.example.advanceprogramming.analyze.DTO;

import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult;
import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FranchiseAnalyzeDTO {

    private String franchise;
    private List<FranchiseAnalyzeResult> countFranchise, eachAverage;
    private List<FranchiseAnalyzeResult2> bestCity, countBestReview, worstCity, countWorstReview,
            storesInCity, countCategories;

}
