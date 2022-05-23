package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.BasicAnalysisDTO;
import com.example.advanceprogramming.analyze.DTO.BusinessDTO;
import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Business;

import java.util.HashMap;
import java.util.List;

public interface AnalyzeService {

    MarkerDTO parseBusinessToMarkerDTO(Business input);

    BusinessDTO parseBusinessToDTO(Business input);

    List<MarkerDTO> getMarkerFromFilter(FilterDTO input);

    HashMap<String, Integer> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/);

    BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, HashMap<String, Integer> input2);
}
