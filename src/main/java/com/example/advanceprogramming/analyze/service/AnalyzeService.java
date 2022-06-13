package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.model.FranchiseAnalyzeResult;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface AnalyzeService {

    MarkerDTO parseBusinessToMarkerDTO(Business input);

    BusinessDTO parseBusinessToDTO(Business input);

    List<BusinessDTO> getMarkerFromFilter(FilterDTO input);

    List<HashMap<String, Integer>> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/);

    BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, HashMap<String, Integer> countPostalcode,
                                             HashMap<String, Integer> countState,  HashMap<String, Integer> countCity);

    BasicAnalysisDTO getAverageScorePerSeason(BasicAnalysisDTO inputDTO,String bID);

    ResponseEntity<?> addBusinessToList(String bId, long userId, int change);

    List<String> getPopularCategories();

    CorrelationAnalysisDTO calcCorrelation(String attibute);
}
