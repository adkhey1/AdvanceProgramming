package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;

import java.util.HashMap;
import java.util.List;

public interface AnalyzeService {

    MarkerDTO parseBusinessToMarkerDTO(Business input);

    BusinessDTO parseBusinessToDTO(Business input);

    List<MarkerDTO> getMarkerFromFilter(FilterDTO input);

    HashMap<String, Integer> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/);

    BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, HashMap<String, Integer> input2);

    ReviewsAnalysisDTO getAverageScorePerSeason(String bID);
}
