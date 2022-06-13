package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.temp.BusinessMapping;
import com.example.advanceprogramming.analyze.temp.ReviewMapping;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Old deprecated methods to calculate csv etc.
 *
 */
public class OldService {

    public void sentimentToCSV() {

        List<tempModel> resultsList = new ArrayList<>();
        tempModel vergleich;
        String line;
        String[] splitted;
        double stars, sent;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getenv("USERPROFILE") + "\\Downloads\\sentimentTableNew1.csv"));

            int monat, index;
            int counter = 0;
            line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                counter++;

                splitted = line.split(",");
                stars = Double.parseDouble(splitted[6]);
                sent = Double.parseDouble(splitted[7]);
                vergleich = new tempModel(splitted[3]);

                int indexfirst = splitted[5].indexOf("-");
                int indexLast = splitted[5].indexOf("-",indexfirst+1);

                monat = Integer.parseInt(splitted[5].substring(indexfirst+1,indexLast));


                if (!resultsList.contains(vergleich)) {
                    resultsList.add(vergleich);
                }
                index = resultsList.indexOf(vergleich);
                resultsList.get(index).addScore(monat,stars,sent);
                System.out.println("Durchlauf Nr." + counter + "  mit review-id: " + splitted[1]);
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
