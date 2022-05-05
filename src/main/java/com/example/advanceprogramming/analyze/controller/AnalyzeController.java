package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Attributes;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.RestaurantRepository;
import com.example.advanceprogramming.analyze.service.AnalyzeService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class AnalyzeController {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AnalyzeService analyzeService;

    @GetMapping("/marker/{id}/")
    public JSONObject getRestaurantByMarked(@PathVariable String id) {

        Business businessByBusinessID = restaurantRepository.findByBusiness_id(id);

        JSONObject Business = new JSONObject();
        Business.put("Business", businessByBusinessID);

        return Business;
    }

    @GetMapping("/search/{name}/")
    public List<JSONObject> getRestaurantByName(@PathVariable String name, Model model) {

        List<Business> businessByName = restaurantRepository.findByName(name);

        if (!businessByName.isEmpty()) {

            List<JSONObject> Business = new ArrayList<>();
            for (int i = 0; i <= businessByName.size(); i++) {
                JSONObject businessByIndex = new JSONObject();
                businessByIndex.put("Business", businessByName.get(i));
                Business.add(i, businessByIndex);
            }

            return Business;
        }

        model.addAttribute("search", "The Business you were looking for is not known to us!");
        return null;
    }

    @PostMapping(value = "/restaurant/filtered/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listRestaurants(@RequestBody FilterDTO input) {

        List<MarkerDTO> businesses = analyzeService.getMarkerFromFilter(input);
        MarkerDTO[] output = businesses.toArray(new MarkerDTO[0]);

        MarkerDTO output1 = new MarkerDTO();
        output1.setLongitude(200);
        output1.setLatitude(211);
        output1.setBusinessID("hahah23");

        return ResponseEntity.status(HttpStatus.OK).body(output1);
    }

    @RequestMapping("/transfromdb")
    public String transfromDB() {
        ArrayList<String> listRaw = restaurantRepository.selectAllFromBusiness();

        log.debug("Start transfrom with " + listRaw.size() + " entries");


        ArrayList<String> keyValue = new ArrayList<>();
        Attributes attribute;

        String businessID;
        String attributes;
        String name;
        String content;


        int firstComma = 0;
        int nextComma = 0;
        int openBracket;
        int closeBracket;

        boolean looper = true;
        String temp;
        for (String s : listRaw) {
            String[] splitted = s.split(",", 2);
            businessID = splitted[0];
            attributes = splitted[1];

            if (attributes.length() > 2) {//Attribute sind vorhanden
                attributes = attributes.replace("'", "");
                System.out.println("Attributes exist- " + attributes);

                if (!attributes.contains(",")) { //Nur ein Attribut vorhanden
                    openBracket = attributes.indexOf("{");
                    closeBracket = attributes.indexOf("}");

                    temp = attributes.substring(openBracket + 1, closeBracket);
                    String[] split = temp.split(": ");

                    name = split[0];
                    content = split[1];

                    System.out.println("\t" + name);
                    System.out.println("\t" + content);
                } else {

                    while (looper) {
                        firstComma = attributes.indexOf(",", nextComma + 1);
                        nextComma = attributes.indexOf(",", firstComma + 1);

                        if (attributes.indexOf("{", firstComma) == -1)

                            attribute = new Attributes();

                        if (nextComma == -1) {
                            looper = false;
                        }
                    }
                }

            } else {//Attribute sind leer
                System.out.println("No Attributes: " + attributes);

            }


        }


        return "transformingDB";
    }


}
