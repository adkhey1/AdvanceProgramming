package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.BusinessDTO;
import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.BusinessRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    @Autowired
    private BusinessRepository restaurantRepo;




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

    /**
     * split categories in a list of Strings
     *
     * @param allBusiness all filtered business from Controller
     * @return HashMap with BusinessID as Key and all categories as List of Strings as value
     */
    public HashMap<String, List<String>> splitCategorie(List<Business> allBusiness){
        //Todo Split all tuples and insert into table "categories"

        HashMap<String, List<String>> business = new HashMap<>();
        //Set<String> allCategories = new HashSet<>(); -> to list all different categories

        for (Business b : allBusiness) {
            String businessID = b.getBusiness_id();
            List<String> categories = Arrays.asList(b.getCategories().split(","));

            business.put(businessID, categories);
            //allCategories.addAll(categories);
        }

        return business;
    }

    public void splitCategoriesToCSV (List<Business> input){

        File categoriesCSV = new File(System.getenv("USERPROFILE")+"\\Downloads\\" + "Categories.csv");

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(categoriesCSV));
            writer.write("business_id,category");
            writer.newLine();

            String line;
            String[] splitted;
            for(Business b : input){
                splitted = b.getCategories().split(",");

                for (String s : splitted){
                    line = b.getBusiness_id() + "," + s;
                    writer.write(line);
                    writer.newLine();
                }
            }
            writer.close();
            System.out.println("CSV erstellt");
        }
        catch (IOException e){
            System.out.println("Ein Fehler ist aufgetreten");
        }
    }

    public void splitAttributesToCSV (List<Business> input){
        //if (attributes.length() > 2) {//Attribute sind vorhanden
        File attributesCSV = new File(System.getenv("USERPROFILE")+"\\Downloads\\" + "Attributes.csv");
        BufferedWriter writer;

        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        Map<String, Object> jsonMap;

        String bId;
        String attributeLine;
        try {
            writer = new BufferedWriter(new FileWriter(attributesCSV));
            writer.write("business_id,aName,value,value_is_table");
            for (Business b : input){
                bId = b.getBusiness_id();
                jsonMap = objectMapper.readValue(b.getAttributes(), new TypeReference<Map<String, Object>>() {});
                //Todo über alle Keys iterieren, pro key eine Zeile in der CSV schreiben
                System.out.println("id-" + bId + " | attributes-> \n \t" + jsonMap.toString());
            }

            writer.close();
        }
        catch (IOException e){
            System.out.println("Ein Fehler ist aufgetreten und wurde gecatcht!");
        }
    }

}
