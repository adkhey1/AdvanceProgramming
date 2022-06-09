package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface AnalyzeService {

    MarkerDTO parseBusinessToMarkerDTO(Business input);

    BusinessDTO parseBusinessToDTO(Business input);

    List<BusinessDTO> getMarkerFromFilter(FilterDTO input);

    void sentimentToCSV();

    List<HashMap<String, Integer>> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/);

    BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, List<HashMap<String, Integer>> input2);

    BasicAnalysisDTO getAverageScorePerSeason(BasicAnalysisDTO inputDTO,String bID);

    ResponseEntity<?> addBusinessToList(String bId, long userId, int change);

    List<String> getPopularCategories();
}
