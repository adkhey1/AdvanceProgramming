package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.CategoriesRepository;
import com.example.advanceprogramming.analyze.repository.BusinessRepository;
import com.example.advanceprogramming.analyze.repository.FranchiseViewRepository;
import com.example.advanceprogramming.analyze.repository.UserBusinessRelationRepository;
import com.example.advanceprogramming.analyze.service.AnalyzeService;
import com.example.advanceprogramming.analyze.service.AnalyzeServiceImpl;
import com.example.advanceprogramming.auth.model.User;
import com.example.advanceprogramming.auth.repository.UserRepository;
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
import org.w3c.dom.Text;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AnalyzeController {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeController.class);

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private FranchiseViewRepository franchiseViewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private UserBusinessRelationRepository userBizRepo;

    @Autowired
    private AnalyzeServiceImpl analyzeServiceImpl;

    @Autowired
    private AnalyzeService analyzeService;

    @PostMapping(value = "/map/viewMarker/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBusinessById(@RequestBody IdDTO input, HttpServletRequest request) {
        //Principal principal = request.getUserPrincipal();
        //User user = userRepository.findByEmail(principal.getName());



        //analyzeService.addBusinessToList(input.getBusiness_id(),user.getId(),0);

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

    @PostMapping(value = "/favorites/add/")
    public ResponseEntity<?> addBusinessToFavorites(@RequestBody IdDTO id, HttpServletRequest request){
        //Todo get User from Name (Email)
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByEmail(principal.getName());

        return analyzeService.addBusinessToList(id.getBusiness_id(), user.getId(),1);
    }

    public ResponseEntity<?> deleteBusinessFromFavorites(@RequestBody IdDTO id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByEmail(principal.getName());

        return analyzeService.addBusinessToList(id.getBusiness_id(), user.getId(), 2);
    }

    @PostMapping(value = "/restaurant/filtered/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listRestaurants(@RequestBody FilterDTO input) {
        log.debug(">>>> recieved Request");
        List<BusinessDTO> businesses = analyzeService.getMarkerFromFilter(input);

        //log.debug(businesses.toString());


        //MarkerDTO[] output = markerList.toArray(new MarkerDTO[0]);

        return ResponseEntity.status(HttpStatus.OK).body(businesses);
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

    @PostMapping(value = "/list/categories/")
    public ResponseEntity<?> listCategories(){
        List<String> output = analyzeService.getPopularCategories();

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @PostMapping(value = "/list/states/")
    public ResponseEntity<?> listStates(){
        List<String> output = businessRepository.selectStates();

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

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


    @RequestMapping(value = "/test/simon/")
    public void getBusinessByFilter(/* @RequestBody FilterDTO filterInput*/) {
        //Prototype Data: get 3760 Business from Philadelphia and with food in the categories (not only "food")
        //Prototype Filter: Categorie, ctiy, stars(double minimum), postcode, is_open, review_count(int minimum)
        //List<Business> allBusiness = businessRepository.selectByFilter("", "Philadelphia", 3, "19146", 1, 50);

        //Business businessByBusinessID = businessRepository.findByBusiness_id("__4gkf_0UJW78rkRzFm6Gw");
        //HashMap<String, Integer> countCategorie = analyzeService.getCategorieInPostCode(businessByBusinessID);
        //BasicAnalysisDTO output = analyzeService.parseBasicAnalysisToDTO(businessByBusinessID, countCategorie);

        String franchise = "McDonald's";

        double avgStars = franchiseViewRepository.averageStars(franchise);
        avgStars = Math.round(avgStars * 100.0) / 100.0;

        //todo performance problems by query 10-15 seconds
        HashMap<String, Integer> countCategories = analyzeServiceImpl.franchiseCategorie(franchise);


        //todo not working (cause of HashMap)
        //HashMap<String, Integer> biggestFranchise = analyzeServiceImpl.countFranchise();
        Map<String, Integer> storesInCity = franchiseViewRepository.storesInCity(franchise);
        //get avgStars for each city

        int bla = 12;

    }


}
