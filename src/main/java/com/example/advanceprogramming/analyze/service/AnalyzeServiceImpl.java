package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.BasicAnalysisDTO;
import com.example.advanceprogramming.analyze.DTO.BusinessDTO;
import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.controller.AnalyzeController;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.BusinessRepository;
import com.example.advanceprogramming.analyze.repository.CategoriesRepository;
import com.example.advanceprogramming.analyze.temp.BusinessMapping;
import com.example.advanceprogramming.analyze.temp.ReviewMapping;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeServiceImpl.class);

    @Autowired
    private BusinessRepository restaurantRepo;

    @Autowired
    private CategoriesRepository categoriesRepository;


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

    @Override
    public List<MarkerDTO> getMarkerFromFilter(FilterDTO input) {

        //Todo implementieren
        return null;
    }

    public String[] splitCategorie(Business input) {
        //Todo Split all tuples and insert into table "categories"
            String[] categories = input.getCategories().split(",");
        return categories;
    }

    public HashMap<String, Integer> getCategorieInPostCode(Business business/* @RequestBody FilterDTO filterInput*/) {

        //Testing categorie by PostCode
        String[] categories = splitCategorie(business);
        HashMap<String, Integer> countCategorie = new HashMap<>();

        log.debug(">>>>>>> for Schleife in Service!");
        for (String x : categories) {

            int i = categoriesRepository.selectAllCategories(x, business.getPostal_code());
            countCategorie.put(x, i);

        }
        log.debug(">>>>>>> for Schleife in Service finish!");

        return countCategorie;
    }

    public void splitReviewsToCSV(){

        File reviewsCSV = new File(System.getenv("USERPROFILE") + "\\Downloads\\" + "Reviews.csv");

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        ReviewMapping tempReview;

        String replaceLineBreaks;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("USERPROFILE")+"\\Downloads\\yelp_dataset\\yelp_academic_dataset_review.json"));
            String line;
            BufferedWriter writer = new BufferedWriter(new FileWriter(reviewsCSV));

            writer.write("review_id,user_id,business_id,stars,useful,funny,cool,text,date");
            writer.newLine();

            while ((line= reader.readLine()) != null) {
                tempReview = objectMapper.readValue(line,ReviewMapping.class);

                replaceLineBreaks = tempReview.getText();
                replaceLineBreaks = replaceLineBreaks.replaceAll("\n\n","");
                replaceLineBreaks = replaceLineBreaks.replaceAll("\n","");
                replaceLineBreaks = replaceLineBreaks.replaceAll(",","+");

                writer.write(tempReview.getReview_id()+",");
                writer.write(tempReview.getBusiness_id()+",");
                writer.write(tempReview.getUser_id()+",");
                writer.write(tempReview.getStars()+",");
                writer.write(tempReview.getUseful()+",");
                writer.write(tempReview.getFunny()+",");
                writer.write(tempReview.getCool()+",");
                writer.write(replaceLineBreaks+",");
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
                categories = b.getCategories().replaceAll("§",",");
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

    public void splitBusinessToCSV(){
        String userpath = System.getenv("USERPROFILE");

        File businessCSV = new File(userpath + "\\Downloads\\" + "Business.csv");
        BufferedWriter writer;

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        BusinessMapping mapping;

        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userpath+"\\Downloads\\yelp_dataset\\yelp_academic_dataset_business.json"));

            writer = new BufferedWriter(new FileWriter(businessCSV));
            writer.write("business_id,name,address,city,state,postal_code,latitude,longitude,stars,review_count,is_open,attributes,categories,hours,");
            writer.newLine();

            String attributes;
            String hours;
            String categories;
            String name;
            String address, city, state;
            while ((line= reader.readLine()) != null) {
                mapping = objectMapper.readValue(line,BusinessMapping.class);

                writer.write(mapping.getBusiness_id()+",");
                name = mapping.getName().replaceAll(",","");
                writer.write(name+",");
                address = mapping.getAddress().replaceAll(",","");
                writer.write(address+",");
                city = mapping.getCity().replaceAll(",","");
                writer.write(city+",");
                state = mapping.getState().replaceAll(",","");
                writer.write(state+",");
                writer.write(mapping.getPostal_code()+",");
                writer.write(mapping.getLatitude()+",");
                writer.write(mapping.getLongitude()+",");
                writer.write(mapping.getStars()+",");
                writer.write(mapping.getReview_count()+",");
                writer.write(mapping.getIs_open()+",");
                if (mapping.getAttributes() != null){
                    attributes = mapping.getAttributes().toString().replaceAll(",","§");
                } else {
                    attributes = "nan";
                }
                writer.write(attributes+",");
                if(mapping.getCategories() != null){
                    categories = mapping.getCategories().replaceAll(",","§");
                }else {
                    categories = "nan";
                }
                writer.write(categories+",");
                if(mapping.getHours() != null){
                    hours = mapping.getHours().toString().replaceAll(",","§");
                } else {
                    hours = "nan";
                }
                writer.write(hours+",");
                writer.newLine();

            }
            writer.close();

        } catch (FileNotFoundException e){
            System.out.println("File nicht gefunden + \n " + e);
        } catch (IOException e){
            System.out.println("Ein Fehler ist aufgetreten +\n" +e);
        }
    }

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

                    attributes = b.getAttributes().replaceAll("§",",");
                    jsonMap = objectMapper.readValue(b.getAttributes(), new TypeReference<Map<String, Object>>() {
                    });
                    //Todo über alle Keys iterieren, pro key eine Zeile in der CSV schreiben
                    for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                        content = entry.getValue().toString().replace(",","+");

                        if (entry.getValue().toString().contains(":")){
                            writer.write(bId + "," + entry.getKey()+","+content+",true");
                        }else {
                            writer.write(bId + "," + entry.getKey()+","+content+",false");
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
