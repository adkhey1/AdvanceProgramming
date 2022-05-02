package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class AnalyzeController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/marker")
    public String getMarkedRestaurant(/*String business_id*/){

        String business_id = "Pns2l4eNsfO8kk83dixA6A";
        Business businessByBusinessID = restaurantRepository.findByBusiness_id(business_id);

        String name = "Dio Modern Mediteranean";
        Business businessByName = restaurantRepository.findByName(name);

        HashMap<String, Integer> ketten = restaurantRepository.findBiggest();

        return "register_page";
    }
}
