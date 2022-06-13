package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.*;
import com.example.advanceprogramming.analyze.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeServiceImpl.class);

    @Autowired
    private BusinessRepository businessRepo;

    @Autowired
    private StateAnalyzeRepository stateAnalyzeRepository;

    @Autowired
    private CityAnalyzeRepository cityAnalyzeRepository;

    @Autowired
    private avgScoreRepository avgScoreRepo;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private SentimentFranchiseRepository sentimentFranchiseRepository;

    @Autowired
    private UserBusinessRelationRepository userBizRepo;

    @Autowired
    private PostalCodeAnalyzeRepository postalCodeAnalyzeRepository;

    @Autowired
    private ReviewsRepository reviewsRepo;


    @Override
    public MarkerDTO parseBusinessToMarkerDTO(Business input) {
        MarkerDTO dto = new MarkerDTO();

        if (input != null) {
            dto.setBusiness_id(input.getBusiness_id());
            dto.setLatitude(input.getLatitude());
            dto.setLongitude(input.getLongitude());
        }

        return dto;
    }

    @Override
    public BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, HashMap<String, Integer> countPostalcode,
                                                    HashMap<String, Integer> countState,  HashMap<String, Integer> countCity) {
        BasicAnalysisDTO dto = new BasicAnalysisDTO();

        if (input != null) {
            dto.setBusiness_id(input.getBusiness_id());
            dto.setName(input.getName());
            dto.setAddress(input.getAddress());
            dto.setHours(input.getHours());
            dto.setCity(input.getCity());
            dto.setState(input.getState());
            dto.setPostal_code(input.getPostal_code());
            dto.setCategories(input.getCategories());
            dto.setAttributes(input.getAttributes());
            dto.setIs_open(input.getIs_open());
            dto.setStars(input.getStars());
            dto.setReview_count(input.getReview_count());
            dto.setCountPostalcode(countPostalcode);
            dto.setCountState(countState);
            dto.setCountCity(countCity);
        }

        return dto;
    }

    @Override
    public BusinessDTO parseBusinessToDTO(Business input) {
        BusinessDTO dto = new BusinessDTO();

        if (input != null) {
            dto.setBusiness_id(input.getBusiness_id());
            dto.setName(input.getName());
            dto.setAddress(input.getAddress());
            dto.setHours(input.getHours());
            dto.setCity(input.getCity());
            dto.setPostal_code(input.getPostal_code());
            dto.setLongitude(input.getLongitude());
            dto.setLatitude(input.getLatitude());
            dto.setState(input.getState());
            dto.setCategories(input.getCategories());
            dto.setAttributes(input.getAttributes());
            dto.setIs_open(input.getIs_open());
            dto.setStars(input.getStars());
            dto.setReview_count(input.getReview_count());
        }

        return dto;
    }

    public UserBusinessRelation createUserBusinessRelation(long userId, String bId, boolean isFavorite) {

        UserBusinessRelation input = new UserBusinessRelation();

        input.setUserId(userId);
        input.setBusinessId(bId);
        input.setFavorite(isFavorite);
        userBizRepo.save(input);

        return input;
    }

    public ResponseEntity<?> addBusinessToList(String bId, long userId, int change) {
/*      0 = Im Verlauf -> Existiert nicht -> erzeugen mit "isfavorite" false
                          existiert -> an "isfavorite" nicht ändern
        1 = In Favoriten -> Exisiter nicht -> mit "isfavorite" 'true' erzeugen
                            Exisitert -> "isfavorite" auf 'true' setzen
        2 = Aus Favoriten löschen -> "isfavorite" auf 'false' setzen
  */


        //andi das existiert braucht man doch eigentlich gar nicht, weil es wird ja sowieso in dem Verlauf
        //gespeichert und falls man es in die Favoriten speichert möchte, müssen sie bereits existieren, da
        //man sie vorher mit getBusinessById aufgerufen hat. Also habe ich nur geschaut, ob sie als Favorit
        //gespeichert sind oder nicht.

        UserBusinessRelation user = userBizRepo.findByNameAndBusinessId(userId, bId);

        switch (change) {
            case 0:

                createUserBusinessRelation(userId, bId, false);
                break;

            case 1:
                //falls es true ist, passiert nichts
                //falls es false ist, wird es überschrieben

                if (user.isFavorite()) {
                    //nothing
                } else {
                    user.setFavorite(true);
                    userBizRepo.save(user);
                }

                break;
            case 2:
                if (user.isFavorite()) {
                    user.setFavorite(false);
                    userBizRepo.save(user);
                } else {
                    //nothing
                }
                break;
        }

        /*
            if (isFavorite){
                return ResponseEntity.status(HttpStatus.OK).body("Business successfully added to favorites!");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Business successfully added to history!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("This business is already selected as favorite!");
         */
        return null;
    }

    @Override
    public List<BusinessDTO> getMarkerFromFilter(FilterDTO input) {
        String category = input.getCategory();
        String attribute = input.getAttribute();
        if (category.startsWith(" ")) {
            category = category.substring(1);
        }
        category = "%" + category + "%";

        if (attribute.equals("")) {
            attribute = "%";
        } else {
            attribute = "%" + attribute + "____True%";
        }

        if (input.getCity().equals("")) {
            input.setCity("%");
        }
        if (input.getPlz().equals("")) {
            input.setPlz("%");
        }
        if (input.getState().equals("")) {
            input.setState("%");
        }
        if (input.getName().equals("")) {
            input.setName("%");
        }

        List<Business> rawList = businessRepo.selectByFilter(Double.parseDouble(input.getStars()), input.getName(), input.getState(), input.getCity(), input.getPlz(), category, attribute);

        List<BusinessDTO> output = new ArrayList<>();
        for (Business b : rawList) {
            output.add(parseBusinessToDTO(b));
        }

        return output;
    }

    public String[] splitCategorie(Business input) {
        //Todo Split all tuples and insert into table "categories"
        String[] categories = input.getCategories().split(",");
        return categories;
    }

    public List<HashMap<String, Integer>> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/) {

        //Testing categorie by PostCode
        String[] categories = splitCategorie(business);
        List<HashMap<String, Integer>> countCategorie = new ArrayList<>();
        HashMap<String, Integer> eachPostal = new HashMap<>();
        HashMap<String, Integer> eachState = new HashMap<>();
        HashMap<String, Integer> eachCity = new HashMap<>();

        for (String x : categories) {
            int i = postalCodeAnalyzeRepository.selectAllCategories(x, business.getPostal_code());
            int z = stateAnalyzeRepository.selectAllState(x, business.getState());
            int y = cityAnalyzeRepository.selectAllCity(x, business.getCity());
            eachPostal.put(x, i);
            eachState.put(x, z);
            eachCity.put(x, y);
        }

        countCategorie.add(eachPostal);
        countCategorie.add(eachState);
        countCategorie.add(eachCity);

        return countCategorie;
    }

    @Override
    public BasicAnalysisDTO getAverageScorePerSeason(BasicAnalysisDTO inputDTO, String bID) {

        AvgScore average = avgScoreRepo.selectById(bID);

        inputDTO.setSpring(average.getRevspring());
        inputDTO.setSummer(average.getRevsummer());
        inputDTO.setFall(average.getRevfall());
        inputDTO.setWinter(average.getRevwinter());
        inputDTO.setSentSpring(average.getSentspring());
        inputDTO.setSentSummer(average.getSentsummer());
        inputDTO.setSentFall(average.getSentfall());
        inputDTO.setSentWinter(average.getSentwinter());

        return inputDTO;
    }

    public FranchiseAnalyzeDTO parseFranchiseAnalyzeDTO(String franchise, List<FranchiseAnalyzeResult> countFranchise,
                                                        List<FranchiseAnalyzeResult> eachAverage, List<FranchiseAnalyzeResult> avgSentiment,
                                                        List<FranchiseAnalyzeResult2> storesInCity,
                                                        List<FranchiseAnalyzeResult2> worstCity, List<FranchiseAnalyzeResult2> countWorstReview,
                                                        List<FranchiseAnalyzeResult2> bestCity, List<FranchiseAnalyzeResult2> countBestReview,
                                                        List<FranchiseAnalyzeResult2> countCategories) {
        FranchiseAnalyzeDTO dto = new FranchiseAnalyzeDTO();

        if (franchise != null) {
            dto.setFranchise(franchise);
            dto.setCountFranchise(countFranchise);
            dto.setEachAverage(eachAverage);
            dto.setAvgSentiment(avgSentiment);
            dto.setStoresInCity(storesInCity);
            dto.setWorstCity(worstCity);
            dto.setCountWorstReview(countWorstReview);
            dto.setCountBestReview(countBestReview);
            dto.setBestCity(bestCity);
            dto.setCountCategories(countCategories);
        }

        return dto;
    }

    public String[] splitCategorieFr(Franchise input) {
        //Todo Split all tuples and insert into table "categories"
        String[] categories = input.getCategories().split(",");
        return categories;
    }

    public FranchiseAnalyzeDTO franchiseCategorie(String franchise) {

        List<FranchiseAnalyzeResult> countFranchise = franchiseRepository.findBiggestFranchises();
        List<FranchiseAnalyzeResult> eachAverage = franchiseRepository.eachAverage();

        List<FranchiseAnalyzeResult> sentimentScore = new ArrayList<>(10);

        List<FranchiseAnalyzeResult2> countCategorie = new ArrayList<>(10);

        //name = number of restaurants      counter = number of review
        List<FranchiseAnalyzeResult2> countBestReviews = new ArrayList<>(10);
        List<FranchiseAnalyzeResult2> bestCity = new ArrayList<>(10);

        List<FranchiseAnalyzeResult2> countWorstReviews = new ArrayList<>(10);
        List<FranchiseAnalyzeResult2> worstCity = new ArrayList<>(10);

        List<FranchiseAnalyzeResult2> storesInCity = new ArrayList<>(10);

        for(int i = 0; i<= countFranchise.size() -1 ; i++){

            String input = countFranchise.get(i).getName1();

            double avg = sentimentFranchiseRepository.getAvgSentiment(input);

            FranchiseAnalyzeResult eachSentiment = new FranchiseAnalyzeResult() {
                @Override
                public String getName1() {
                    return input;
                }

                @Override
                public double getCounter() {
                    return avg;
                }
            };

            sentimentScore.add(eachSentiment);

            List<Franchise> all = franchiseRepository.selectFirst10(input);
            Set<String> allCategories = new HashSet<>();

            List<FranchiseAnalyzeResult> categorieCount = new ArrayList<>();

            for (Franchise x : all) {
                String[] categories = splitCategorieFr(x);
                for (String z : categories) {
                    if (z.startsWith(" ")) {
                        z = z.replaceFirst("\\s+", "");
                    }
                    allCategories.add(z);
                }
            }

            List<String> finalCategorie = new ArrayList<>(allCategories);

            for (String z : finalCategorie) {
                //todo performance probleme
                FranchiseAnalyzeResult categorieModel = new FranchiseAnalyzeResult() {
                    @Override
                    public String getName1() {
                        return z;
                    }

                    @Override
                    public double getCounter() {
                        return franchiseRepository.basicCategorie(input, z);
                    }
                };

                categorieCount.add(categorieModel);
            }

            FranchiseAnalyzeResult2 categorie = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return categorieCount;
                }
            };

            countCategorie.add(categorie);



            FranchiseAnalyzeResult2 test = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return franchiseRepository.averageStarsByCity(input);
                }
            };

            String best1 = test.getListe().get(0).getName1();
            String best2 = test.getListe().get(1).getName1();
            String best3 = test.getListe().get(2).getName1();
            String best4 = test.getListe().get(3).getName1();
            String best5 = test.getListe().get(4).getName1();

            FranchiseAnalyzeResult2 test2 = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return franchiseRepository.countReviews(input, best1, best2, best3, best4, best5);
                }
            };

            countBestReviews.add(test2);
            bestCity.add(test);



            FranchiseAnalyzeResult2 test3 = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return franchiseRepository.averageStarsByCityWorst(input);
                }
            };

            String worst1 = test3.getListe().get(0).getName1();
            String worst2 = test3.getListe().get(1).getName1();
            String worst3 = test3.getListe().get(2).getName1();
            String worst4 = test3.getListe().get(3).getName1();
            String worst5 = test3.getListe().get(4).getName1();

            FranchiseAnalyzeResult2 test4 = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return franchiseRepository.countReviews(input, worst1, worst2, worst3, worst4, worst5);
                }
            };
            worstCity.add(test3);
            countWorstReviews.add(test4);


            FranchiseAnalyzeResult2 test5 = new FranchiseAnalyzeResult2() {
                @Override
                public String getFranchise1() {
                    return input;
                }

                @Override
                public List<FranchiseAnalyzeResult> getListe() {
                    return franchiseRepository.storesInCity(input);
                }
            };

            storesInCity.add(test5);
        }

        FranchiseAnalyzeDTO output = parseFranchiseAnalyzeDTO(franchise, countFranchise, eachAverage, sentimentScore,
                storesInCity, worstCity, countWorstReviews, bestCity, countBestReviews, countCategorie);


        return output;
    }
/*
    public HashMap<String, Integer> countFranchise(){

        List<String> countFranchise = franchiseViewRepository.selectAllNames();
        HashMap<String, Integer> countRestaurante = new HashMap<>();

        for(String i : countFranchise){
            switch
        }
        return countRestaurante;
    }

 */


    @Override
    public List<String> getPopularCategories() {
        List<String> listCategories = businessRepo.selectPopularCategories();

        List<String> outputList = new ArrayList<>();
        int commaIndex = 0;
        for (String s : listCategories) {
            commaIndex = s.indexOf(",");
            outputList.add(s.substring(0, commaIndex));
        }
        return outputList;
    }

    @Override
    public CorrelationAnalysisDTO calcCorrelation(String attibute) {
        //TODO Werte berechnen und in DB überführen ?
        int n, attributeCount;
        double avgAttributeCount = 0.0, avgScore = 0.0, varCount = 0.0, varScore = 0.0;
        Business temp;
        String[] splittedAttributes;

        ArrayList<Business> allBusinesses = businessRepo.selectAll();

        n = allBusinesses.size();

        for(int i = 0; i < allBusinesses.size(); i++){
            avgAttributeCount = 0;

            temp = allBusinesses.get(i);
            avgScore += temp.getStars();
            splittedAttributes = temp.getAttributes().split("");

            //TODO 'splitAttributesToCSV' Algorithmus kopieren oder aus 'Attributes' Table ziehen (content_is_table=false, content=True/False)
            //TODO ausnahme für wifi = free ??

        }

        try {
            String headline = "title,corr,";

        }catch (Exception e){
            System.out.println("Ein Fehler ist aufgetreten");
        }

        return null;
    }
}
