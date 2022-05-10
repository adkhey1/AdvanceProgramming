package com.example.advanceprogramming.analyze.service;

import com.example.advanceprogramming.analyze.DTO.BusinessDTO;
import com.example.advanceprogramming.analyze.DTO.FilterDTO;
import com.example.advanceprogramming.analyze.DTO.MarkerDTO;
import com.example.advanceprogramming.analyze.model.Business;
import com.example.advanceprogramming.analyze.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class AnalyzeServiceImpl implements AnalyzeService {

    @Autowired
    private RestaurantRepository restaurantRepo;


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

}
