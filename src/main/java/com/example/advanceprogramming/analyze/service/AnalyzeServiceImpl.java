package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.*;
import com.example.advanceprogramming.analyze.model.*;
import com.example.advanceprogramming.analyze.repository.*;
import com.example.advanceprogramming.analyze.temp.BusinessMapping;
import com.example.advanceprogramming.analyze.temp.ReviewMapping;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeServiceImpl.class);

    @Autowired
    private BusinessRepository businessRepo;

    @Autowired
    private SentimentsRepository sentimentRepo;

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private UserBusinessRelationRepository userBizRepo;

    @Autowired
    private PostalCodeViewRepository postalCodeViewRepository;

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
    public BasicAnalysisDTO parseBasicAnalysisToDTO(Business input, HashMap<String, Integer> input2) {
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
            dto.setCountCategorie(input2);
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
        if (input.getCity().equals("")) {
            log.debug("Ersetzen durch % ausgelöst");
            input.setCity("%");
        }
        if (input.getPlz().equals("")) {
            log.debug("Ersetzen durch % ausgelöst");
            input.setPlz("%");
        }
        if (input.getState().equals("")) {
            log.debug("Ersetzen durch % ausgelöst");
            input.setState("%");
        }
        if (input.getName().equals("")) {
            log.debug("Ersetzen durch % ausgelöst");
            input.setName("%");
        }

        List<Business> rawList = businessRepo.selectByFilter(Double.parseDouble(input.getStars()), input.getName(), "FL", input.getCity(), input.getPlz());

        log.debug("Länger der sql-Antwort an Objekten " + rawList.size());

        List<Business> filteredList = new ArrayList<>();

        String[] splittedCategories;
        boolean containsCategory;
        for (Business b : rawList) {
            containsCategory = false;
            splittedCategories = b.getCategories().split(",");

            for (String s : splittedCategories) {
                //if (s.equals(input.getCategory())){ TODO Liste der Kategorieren an Front-End
                if (s.equals(" Burgers")) {
                    containsCategory = true;
                }
            }
            if (containsCategory) {
                filteredList.add(b);
            }
        }

        log.debug("Länger der filteredList " + filteredList.size());

        /*List<MarkerDTO> output = new ArrayList<>();

        MarkerDTO temp;
        for (Business b : rawList) {
            temp = new MarkerDTO();
            temp.setLatitude(b.getLatitude());
            temp.setLongitude(b.getLongitude());
            temp.setBusiness_id(b.getBusiness_id());
            output.add(temp);
        }*/
        List<BusinessDTO> output = new ArrayList<>();
        for (Business b : filteredList) {
            output.add(parseBusinessToDTO(b));
        }

        return output;
    }

    public String[] splitCategorie(Business input) {
        //Todo Split all tuples and insert into table "categories"
        String[] categories = input.getCategories().split(",");
        return categories;
    }

    public HashMap<String, Integer> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/) {

        //Testing categorie by PostCode
        String[] categories = splitCategorie(business);
        String[] attributes = {"'GoodForKids': 'True'", "'BusinessAcceptsCreditCards': 'True'", "'RestaurantsDelivery': 'True'"};
        HashMap<String, Integer> countCategorie = new HashMap<>();
        int counter = 0;


        for (String x : categories) {
            int i = postalCodeViewRepository.selectAllCategories(x, business.getPostal_code());
            countCategorie.put(x, i);

            /*
            for (String z : attributes){
                int anzahl = postalCodeViewRepository.selectAllAttributes(z, business.getPostal_code(), x);
                countCategorie.put(z + Integer.toString(counter), anzahl);
            }
             */
            counter++;
        }

        return countCategorie;
    }

    @Override
    public BasicAnalysisDTO getAverageScorePerSeason(BasicAnalysisDTO inputDTO, String bID) {
        double spring = 0.0;
        double summer = 0.0;
        double fall = 0.0;
        double winter = 0.0;

        /*log.debug(">>>> In Service query started!");
        //List<Review> allReviews = reviewsRepo.selectReviewsWithBusinessId(bID);
        log.debug(">>>> In Service query finished");

        for (Review r : allReviews) {
            switch (r.getDate().getMonthValue()) {
                case 1, 2, 3:
                    counterSpring++;
                    spring += r.getStars();
                    break;
                case 4, 5, 6:
                    counterSummer++;
                    summer += r.getStars();
                    break;
                case 7, 8, 9:
                    counterFall++;
                    fall += r.getStars();
                    break;
                case 10, 11, 12:
                    counterWinter++;
                    winter += r.getStars();
                    break;
                default:
                    System.out.println("Error im switch-case");
            }
        }

        spring = spring / counterSpring;
        summer = summer / counterSummer;
        fall = fall / counterFall;
        winter = winter / counterWinter;

        if (Double.isNaN(spring)) {
            spring = 0;
        }
        if (Double.isNaN(summer)) {
            summer = 0;
        }
        if (Double.isNaN(fall)) {
            fall = 0;
        }
        if (Double.isNaN(winter)) {
            winter = 0;
        }


        inputDTO.setSpring(spring);
        inputDTO.setSummer(summer);
        inputDTO.setFall(fall);
        inputDTO.setWinter(winter);
*/


        inputDTO.setSpring(0.0);
        inputDTO.setSummer(0.0);
        inputDTO.setFall(0.0);
        inputDTO.setWinter(0.0);

        return inputDTO;
    }

    public FranchiseAnalyzeDTO parseFranchiseAnalyzeDTO(String franchise, List<FranchiseAnalyzeResult> countFranchise,
                                                        List<FranchiseAnalyzeResult> eachAverage, List<FranchiseAnalyzeResult2> storesInCity,
                                                        List<FranchiseAnalyzeResult2> worstCity, List<FranchiseAnalyzeResult2> countWorstReview,
                                                        List<FranchiseAnalyzeResult2> bestCity, List<FranchiseAnalyzeResult2> countBestReview,
                                                        List<FranchiseAnalyzeResult2> countCategories) {
        FranchiseAnalyzeDTO dto = new FranchiseAnalyzeDTO();

        if (franchise != null) {
            dto.setFranchise(franchise);
            dto.setCountFranchise(countFranchise);
            dto.setEachAverage(eachAverage);
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

        List<FranchiseAnalyzeResult2> countCategorie = new ArrayList<>(10);

        //name = number of restaurants      counter = number of review
        List<FranchiseAnalyzeResult2> countBestReviews = new ArrayList<>(10);
        List<FranchiseAnalyzeResult2> bestCity = new ArrayList<>(10);

        List<FranchiseAnalyzeResult2> countWorstReviews = new ArrayList<>(10);
        List<FranchiseAnalyzeResult2> worstCity = new ArrayList<>(10);

        List<FranchiseAnalyzeResult2> storesInCity = new ArrayList<>(10);

        for(int i = 0; i<= countFranchise.size() -1 ; i++){

            String input = countFranchise.get(i).getName1();



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

            String worst1 = test.getListe().get(0).getName1();
            String worst2 = test.getListe().get(1).getName1();
            String worst3 = test.getListe().get(2).getName1();
            String worst4 = test.getListe().get(3).getName1();
            String worst5 = test.getListe().get(4).getName1();

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

        FranchiseAnalyzeDTO output = parseFranchiseAnalyzeDTO(franchise, countFranchise, eachAverage,
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

    public void sentimentToCSV() {
        //List<Sentiments> inputList = sentimentRepo.findAll();

        List<tempModel> resultsList = new ArrayList<>();
        tempModel vergleich;
        String line;
        String[] splitted;
        double stars, sent;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("USERPROFILE") + "\\Downloads\\sentimentTableNew.csv"));

            int monat, index;
            int counter = 0;
            line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                counter++;

                splitted = line.split(",");
                stars = Double.parseDouble(splitted[5]);
                sent = Double.parseDouble(splitted[6]);
                vergleich = new tempModel(splitted[3]);

                //monat = splitted[7];


                if (!resultsList.contains(vergleich)) {
                    resultsList.add(vergleich);
                }
                index = resultsList.indexOf(vergleich);

                //resultsList.get(index).addScore(monat, stars, sent);
                System.out.println("Durchlauf mit review-id: " + splitted[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fehler ist aufgetreten");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Fehler ist aufgetreten");
            throw new RuntimeException(e);
        }

        File outputCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\reducedReviews.csv");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputCSV));

            writer.write("business_id,countSpring,countSummer,countFall,countWinter,revSpring,revSummer,revFall,revWinter,sentSpring,sentSummer,sentFall,sentWinter");
            writer.newLine();
            for (tempModel t : resultsList) {
                writer.write(t.getBID() + ",");
                writer.write(t.getSpringCounter() + ",");
                writer.write(t.getSummerCounter() + ",");
                writer.write(t.getFallCounter() + ",");
                writer.write(t.getWinterCounter() + ",");
                writer.write(t.getRevSpring() + ",");
                writer.write(t.getRevSummer() + ",");
                writer.write(t.getRevFall() + ",");
                writer.write(t.getRevWinter() + ",");
                writer.write(t.getSentSpring() + ",");
                writer.write(t.getSentSummer() + ",");
                writer.write(t.getSentFall() + ",");
                writer.write(t.getSentWinter() + ",");

                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Fehler aufgetreten");
        }


    }

    public void splitReviewsToCSV() {

        File reviewsCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\" + "Reviews.csv");

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        ReviewMapping tempReview;

        String replaceLineBreaks;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("USERPROFILE") + "\\Downloads\\yelp_dataset\\yelp_academic_dataset_review.json"));
            String line;
            BufferedWriter writer = new BufferedWriter(new FileWriter(reviewsCSV));

            writer.write("review_id,user_id,business_id,stars,useful,funny,cool,date");
            writer.newLine();

            while ((line = reader.readLine()) != null) {
                tempReview = objectMapper.readValue(line, ReviewMapping.class);

                /*replaceLineBreaks = tempReview.getText();
                replaceLineBreaks = replaceLineBreaks.replaceAll("\n\n", "");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\n", "");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\r","");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\r\r","");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\r\n","");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\n\r","");
                replaceLineBreaks = replaceLineBreaks.replaceAll(",", "+");
                replaceLineBreaks = replaceLineBreaks.replaceAll(";","+");
*/
                writer.write(tempReview.getReview_id() + ",");
                writer.write(tempReview.getUser_id() + ",");
                writer.write(tempReview.getBusiness_id() + ",");
                writer.write(tempReview.getStars() + ",");
                writer.write(tempReview.getUseful() + ",");
                writer.write(tempReview.getFunny() + ",");
                writer.write(tempReview.getCool() + ",");
                //writer.write(replaceLineBreaks + ",");
                writer.write(tempReview.getDate());
                writer.newLine();

            }
            writer.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void splitCategoriesToCSV(List<Business> input) {

        File categoriesCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\" + "Categories.csv");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(categoriesCSV));
            writer.write("business_id,category");
            writer.newLine();

            String line, categories;
            String[] splitted;
            for (Business b : input) {
                categories = b.getCategories().replaceAll("§", ",");
                splitted = categories.split(",");

                for (String s : splitted) {
                    line = b.getBusiness_id() + "," + s;
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            System.out.println("CSV erstellt");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten" + e);
        }
    }

    public void splitBusinessToCSV() {
        String userpath = System.getenv("USERPROFILE");

        File businessCSV = new File(userpath + "\\Downloads\\" + "Business.csv");
        BufferedWriter writer;

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        BusinessMapping mapping;

        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userpath + "\\Downloads\\yelp_dataset\\yelp_academic_dataset_business.json"));

            writer = new BufferedWriter(new FileWriter(businessCSV));
            writer.write("business_id,name,address,city,state,postal_code,latitude,longitude,stars,review_count,is_open,attributes,categories,hours,");
            writer.newLine();

            String attributes;
            String hours;
            String categories;
            String name;
            String address, city, state;
            while ((line = reader.readLine()) != null) {
                mapping = objectMapper.readValue(line, BusinessMapping.class);

                writer.write(mapping.getBusiness_id() + ",");
                name = mapping.getName().replaceAll(",", "");
                writer.write(name + ",");
                address = mapping.getAddress().replaceAll(",", "");
                writer.write(address + ",");
                city = mapping.getCity().replaceAll(",", "");
                writer.write(city + ",");
                state = mapping.getState().replaceAll(",", "");
                writer.write(state + ",");
                writer.write(mapping.getPostal_code() + ",");
                writer.write(mapping.getLatitude() + ",");
                writer.write(mapping.getLongitude() + ",");
                writer.write(mapping.getStars() + ",");
                writer.write(mapping.getReview_count() + ",");
                writer.write(mapping.getIs_open() + ",");
                if (mapping.getAttributes() != null) {
                    attributes = mapping.getAttributes().toString().replaceAll(",", "§");
                } else {
                    attributes = "nan";
                }
                writer.write(attributes + ",");
                if (mapping.getCategories() != null) {
                    categories = mapping.getCategories().replaceAll(",", "§");
                } else {
                    categories = "nan";
                }
                writer.write(categories + ",");
                if (mapping.getHours() != null) {
                    hours = mapping.getHours().toString().replaceAll(",", "§");
                } else {
                    hours = "nan";
                }
                writer.write(hours + ",");
                writer.newLine();

            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("File nicht gefunden + \n " + e);
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten +\n" + e);
        }
    }

   /* public void addIntIdBusiness(){
        List<Business> list = businessRepo.selectAll();

        BusinessNew temp;

        int counter = 0;

        for (Business b : list){
            counter++;
            temp = new BusinessNew();

            temp.setBusiness_id(b.getBusiness_id());
            temp.setName(b.getName());
            temp.setAddress(b.getAddress());
            temp.setCity(b.getCity());
            temp.setState(b.getState());
            temp.setPostal_code(b.getPostal_code());
            temp.setLatitude(b.getLatitude());
            temp.setLongitude(b.getLongitude());
            temp.setStars(b.getStars());
            temp.setReview_count(b.getReview_count());
            temp.setIs_open(b.getIs_open());
            temp.setAttributes(b.getAttributes());
            temp.setCategories(b.getCategories());
            temp.setHours(b.getHours());

            businessNewRepo.save(temp);

            System.out.println("Business Nr. " + counter + "erstellt!");
        }

        /*File newBusinessCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\" + "NewBusiness.csv");
        BufferedWriter writer;

        String oldId, name, address, city, state, postal_code, latitude, longitude, attributes, categories, hours;
        double stars;
        int review_count, is_open, bId;
        bId = 0;
        try {
            writer = new BufferedWriter(new FileWriter(newBusinessCSV));
            writer.write("business_id,bId,name,address,city,state,postal_code,latitude,longitude,stars,review_count,is_open,attributes,categories,hours,");
            writer.newLine();
            String content;
            for (Business b : list){
                bId++;

                writer.write(b.getBusiness_id() + ";");
                writer.write(bId + ";");
                writer.write(b.getName() + ";");
                writer.write(b.getAddress() + ";");
                writer.write(b.getCity() + ";");
                writer.write(b.getState() + ";");
                writer.write(b.getPostal_code() + ";");
                writer.write(b.getState() + ";");
                writer.write(b.getPostal_code() + ";");
                writer.write(b.getLatitude() + ";");
                writer.write(b.getLongitude() + ";");
                writer.write(b.getStars() + ";");
                writer.write(b.getReview_count() + ";");
                writer.write(b.getIs_open() + ";");
                writer.write(b.getAttributes() + ";");
                writer.write(b.getCategories() + ";");
                writer.write(b.getHours() + ";");

                writer.newLine();
            }

        } catch (Exception e){
            System.out.println("Some ERROR happend mate!");
        }
*/

    public void splitAttributesToCSV(List<Business> input) {
        //if (attributes.length() > 2) {//Attribute sind vorhanden
        File attributesCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\" + "Attributes.csv");
        BufferedWriter writer;

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        Map<String, Object> jsonMap;

        String bId, attributes;
        String attributeLine;
        try {
            writer = new BufferedWriter(new FileWriter(attributesCSV));
            writer.write("bId,aName,content,content_is_table");
            writer.newLine();
            String content;
            for (Business b : input) {
                if (b.getAttributes().length() > 2) {
                    bId = b.getBusiness_id();

                    attributes = b.getAttributes().replaceAll("§", ",");
                    jsonMap = objectMapper.readValue(b.getAttributes(), new TypeReference<Map<String, Object>>() {
                    });
                    //Todo über alle Keys iterieren, pro key eine Zeile in der CSV schreiben
                    for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                        content = entry.getValue().toString().replace(",", "+");

                        if (entry.getValue().toString().contains(":")) {
                            writer.write(bId + "," + entry.getKey() + "," + content + ",true");
                        } else {
                            writer.write(bId + "," + entry.getKey() + "," + content + ",false");
                        }
                        writer.newLine();
                    }
                    System.out.println("id-" + bId + " | attributes-> \n \t" + jsonMap.toString());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten und wurde gecatcht!" + e);
        }
    }
}
