package com.example.advanceprogramming.analyze.controller;


import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.model.Franchise;
import com.example.advanceprogramming.analyze.model.LongLatResult;
import com.example.advanceprogramming.analyze.repository.*;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Controller
public class AnalyzeController {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeController.class);

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserBusinessRelationRepository userBusinessRelationRepository;

    @Autowired
    private AttributesRepository attributesRepository;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SentimentFranchiseRepository sentimentFranchiseRepository;

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


        Business businessByBusinessID = businessRepository.findByBusiness_id(input.getBusiness_id());


        log.debug(">>>> Service 'Categories in Postcode started");
        List<HashMap<String, Integer>> countCategorie = analyzeService.getCategorieInPostCode(businessByBusinessID);
        log.debug(">>>> Service 'Categories in Postcode finished");

        HashMap<String, Integer> postalcode = countCategorie.get(0);
        HashMap<String, Integer> state = countCategorie.get(1);
        HashMap<String, Integer> city = countCategorie.get(2);


        BasicAnalysisDTO output = analyzeService.parseBasicAnalysisToDTO(businessByBusinessID, postalcode, state, city);

        log.debug(">>>> Service 'Average Reviews started");
        output = analyzeService.getAverageScorePerSeason(output, input.getBusiness_id());
        log.debug(">>>> Service-Method 'Average Reviews finished'");

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
    public ResponseEntity<?> addBusinessToFavorites(@RequestBody IdDTO id, HttpServletRequest request) {
        //Todo get User from Name (Email)
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByEmail(principal.getName());

        return analyzeService.addBusinessToList(id.getBusiness_id(), user.getId(), 1);
    }

    @GetMapping(value = "/categorieMap", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> categorieMap(@RequestBody PeersInAreaDTO input) {

        //String business_id = "_Gygd2tnDo3t1adE2bsUtQ";
        //String categorie = "Burgers";
        //int choice = 2;

        Business business = businessRepository.findByBusiness_id(input.getBusiness_id());
        List<LongLatResult> output = new ArrayList<>();

        //choice: 0 = postalcode / 1 = city / 2 = state
        switch (input.getChoice()) {
            case 0:
                String postalcode = business.getPostal_code();
                output.addAll(businessRepository.selectBusinessPostalCode(input.getCategorie(), postalcode));
                break;
            case 1:
                String city = business.getCity();
                output.addAll(businessRepository.selectBusinessCity(input.getCategorie(), city));
                break;
            case 2:
                String state = business.getState();
                output.addAll(businessRepository.selectBusinessState(input.getCategorie(), state));
                break;
            default:
                //nothing
                break;
        }

        OnlyLatLongDTO output2 = new OnlyLatLongDTO();
        output2.setOutput(output);

        return ResponseEntity.status(HttpStatus.OK).body(output2);
    }

    @GetMapping(value = "/get/history/")
    public String getCalledBusiness(Model model, HttpServletRequest request) {

        //todo erst weiter machen, wenn gracjan security fertig hat
        //Principal principal = request.getUserPrincipal();
        //User user = userRepository.findByEmail(principal.getName());

        //List<String> allBusinessesIds = userBusinessRelationRepository.selectAllBusinessIDFromUser(user.getId());

        List<String> testData = new ArrayList<>();
        testData.add("LHSTtnW3YHCeUkRDGyJOyw");
        testData.add("gebiRewfieSdtt17PTW6Zg");
        testData.add("lj-E32x9_FA7GmUrBGBEWg");
        testData.add("RZtGWDLCAtuipwaZ-UfjmQ");

        List<Business> allBusinesses = businessRepository.findByBusinessIdInList(testData);

        for (Business b : allBusinesses) {
            String a = b.getHours().replaceAll("}", "");
            String c = a;
            c = c.replaceAll("'", "");
            String d = c;
            d = d.replace("{", "");

            b.setHours(d);
        }

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByEmail(principal.getName());

        List<String> allBusinessesIds = userBusinessRelationRepository.selectAllBusinessIDFromUser(user.getId());

        //List<Business> allBusinesses = businessRepository.findByBusinessIdInList(allBusinessesIds);

        return null;
    }

    public ResponseEntity<?> deleteBusinessFromFavorites(@RequestBody IdDTO id, HttpServletRequest request) {
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

    @GetMapping(value = "/analyze/franchise/")
    public ResponseEntity<?> franchiseAnalyze(/*@RequestParam String franchiseName*/) {

        String franchise = "McDonald's";
        FranchiseAnalyzeDTO output = analyzeServiceImpl.franchiseCategorie(franchise);

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

    @PostMapping(value = "/list/categories/")
    public ResponseEntity<?> listCategories() {
        List<String> output = analyzeService.getPopularCategories();

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @PostMapping(value = "/list/states/")
    public ResponseEntity<?> listStates() {
        List<String> output = businessRepository.selectStates();

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @RequestMapping(value = "/list/attributes/")
    public ResponseEntity<?> listAttributes() {
        List<String> output = attributesRepository.selectAllAttributes("True", "False");

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    /**
     * Deprecated Stuff to get csv etc.
     *
     * @return
     */

    /*
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

    @RequestMapping(value = "/test/andi/")
    public String testingstuff() {
        //analyzeService.sentimentToCSV();

        return "Andistests";
    }*/
    @RequestMapping("/franchise")
    private String getMap() {
        return "franchise";
    }

    /*@GetMapping("/test/simon")
    private void testSimon(){


        double avg = sentimentFranchiseRepository.getAvgSentiment("McDonald's");
        double avg1 = sentimentFranchiseRepository.getAvgSentiment("Subway");
        double avg2 = sentimentFranchiseRepository.getAvgSentiment("Starbucks");

        Business business = businessRepository.findByBusiness_id("__CYdei4W4pVb4SThJ-HYg");
        List<HashMap<String, Integer>> categorie = analyzeServiceImpl.getCategorieInPostCode(business);

        int test = 23;

    }

     */
}
