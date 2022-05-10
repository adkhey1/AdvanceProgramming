package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Business;

import java.util.List;

public interface AnalyzeService {

    MarkerDTO parseBusinessToMarkerDTO(Business input);

    List<MarkerDTO> getMarkerFromFilter(FilterDTO input);
}
