package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.CategoriesRepository;
import com.example.advanceprogramming.analyze.repository.BusinessRepository;
import com.example.advanceprogramming.analyze.service.AnalyzeService;
import com.example.advanceprogramming.analyze.service.AnalyzeServiceImpl;
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

import java.util.*;

@Controller
public class AnalyzeController {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeController.class);

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private AnalyzeServiceImpl analyzeServiceImpl;

    @Autowired
    private AnalyzeService analyzeService;

    @PostMapping(value = "/map/viewMarker/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBusinessById(@RequestBody IdDTO input) {
        log.debug(">>>> Anfrage 'viewMarker' angefangen");

        log.debug(">>>> Anfrage an DB per Repo gestartet");
        Business businessByBusinessID = businessRepository.findByBusiness_id(input.getBusiness_id());
        log.debug(">>>> Anfrage an DB fertig! ");

        log.debug(">>>> Service 'Categories in Postcode started");
        HashMap<String, Integer> countCategorie = analyzeService.getCategorieInPostCode(businessByBusinessID);
        log.debug(">>>> Service 'Categories in Postcode finished");

        log.debug(">>>> Service -> parsing started");
        BasicAnalysisDTO output = analyzeService.parseBasicAnalysisToDTO(businessByBusinessID, countCategorie);
        log.debug(">>>> Service -> parsing finished");


        output = analyzeService.getAverageScorePerSeason(output, input.getBusiness_id());



        log.debug(">>>> Anfrage 'viewMarker' beendet");
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @PostMapping("/map/searchName/")
    public List<JSONObject> getRestaurantByName(@RequestBody String name, Model model) {

        List<Business> businessByName = businessRepository.findByName(name);

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

    @PostMapping(value = "/restaurant/filtered/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listRestaurants(@RequestBody FilterDTO input) {

        List<Business> businesses = businessRepository.selectfirst100();

        List<MarkerDTO> markerList = new ArrayList<>();
        MarkerDTO temp;
        for (Business b : businesses) {
            temp = new MarkerDTO();

            temp.setLatitude(b.getLatitude());
            temp.setLongitude(b.getLongitude());
            temp.setBusiness_id(b.getBusiness_id());

            markerList.add(temp);
        }

        log.debug(input.toString());

        MarkerDTO[] output = markerList.toArray(new MarkerDTO[0]);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @PostMapping(value = "/100restaurants/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listRestaurantsTemp() {

        List<Business> businesses = businessRepository.selectfirst100();

        List<MarkerDTO> markerList = new ArrayList<>();
        MarkerDTO temp;
        for (Business b : businesses) {
            temp = new MarkerDTO();

            temp.setLatitude(b.getLatitude());
            temp.setLongitude(b.getLongitude());
            temp.setBusiness_id(b.getBusiness_id());

            markerList.add(temp);
        }


        MarkerDTO[] output = markerList.toArray(new MarkerDTO[0]);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    /*@RequestMapping("/analyze/averageReviews/")
    public ResponseEntity<?> averageScores(@RequestBody String input) {

        log.debug(input);
        ReviewsAnalysisDTO reviewAnalyDTO = analyzeService.getAverageScorePerSeason(input);

        System.out.println(reviewAnalyDTO);

        return ResponseEntity.status(HttpStatus.OK).body(reviewAnalyDTO);
    }*/

    @RequestMapping("/split/attributes")
    public String splitAttributesToDB() {
        ArrayList<Business> listRaw = businessRepository.selectAll();

        analyzeServiceImpl.splitAttributesToCSV(listRaw);

        return "transformingDB";
    }

    @RequestMapping(value = "/split/categories")
    public String splitCategoriesToTable() {

        //List<Categories> categories;
        List<Business> listBusiness = businessRepository.selectAll();
        analyzeServiceImpl.splitCategoriesToCSV(listBusiness);


        System.out.println("categories saved!");
        return "splittedCategories";
    }

    @RequestMapping(value = "/split/reviews")
    public String splitReviews() {
        analyzeServiceImpl.splitReviewsToCSV();

        return "splitReviews";
    }

    @RequestMapping(value = "/split/business")
    public String splitBusiness() {
        analyzeServiceImpl.splitBusinessToCSV();

        return "splitBusiness";
    }

    /**
     * needs categories from filter
     */
    @RequestMapping(value = "/test/test/1")
    public void getBusinessByFilter(/* @RequestBody FilterDTO filterInput*/) {


        //Prototype Data: get 3760 Business from Philadelphia and with food in the categories (not only "food")
        //Prototype Filter: Categorie, ctiy, stars(double minimum), postcode, is_open, review_count(int minimum)
        //List<Business> allBusiness = businessRepository.selectByFilter("", "Philadelphia", 3, "19146", 1, 50);

        //HashMap<String, List<String>> business = (analyzeServiceImpl.splitCategorie(allBusiness));


    }


}
