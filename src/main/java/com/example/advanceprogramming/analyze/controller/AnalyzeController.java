package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.RestaurantRepository;
import com.example.advanceprogramming.analyze.service.AnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class AnalyzeController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AnalyzeService analyzeService;

    @GetMapping("/marker/{id}/")
    public String getMarkedRestaurant(@PathVariable String id){

        String business_id = "Pns2l4eNsfO8kk83dixA6A";
        Business businessByBusinessID = restaurantRepository.findByBusiness_id(business_id);
        //Business businessByBusinessID = restaurantRepository.findByBusiness_id(business_id);

        String name = "Dio Modern Mediteranean";
        Business businessByName = restaurantRepository.findByName(name);

        HashMap<String, Integer> ketten = restaurantRepository.findBiggest();


        MarkerDTO markerDTO = analyzeService.parseBusinessToMarkerDTO(businessByBusinessID);

        return "register_page";
        //return ResponseEntity.status(HttpStatus.OK).body(markerDTO)
    }

    @PostMapping(value = "/restaurant/filtered/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listRestaurants (@RequestBody FilterDTO input){

        List<MarkerDTO> businesses = analyzeService.getMarkerFromFilter(input);
        MarkerDTO[] output = businesses.toArray(new MarkerDTO[0]);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

}
