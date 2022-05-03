package com.example.advanceprogramming.auth.controller;



import com.example.advanceprogramming.auth.model.User;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Start Page
    @GetMapping({"/home", "/", ""})
    public String viewHomePage() {
        return "index";
    }

    @GetMapping({"/test"})
    public String test() {
        return "test";
    }

    //Register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register_page";
    }

    @PostMapping("/registering")
    public String registerUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);

        return "registered";


    }

    @GetMapping("/user_dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName(); //only for testing porpoise
        model.addAttribute("test", currentPrincipalName); //only for testing porpoise

        return "user_dashboard";
    }

    @GetMapping("/map")
    public String getMap() {
        return "gmaps";
    }








    @RequestMapping(value="/restaurant/filtered",method=RequestMethod.POST)
    public  @ResponseBody String  getSearchUserProfiles(@RequestBody String search, HttpServletRequest request) {
        String pName = search.toString();
        //String lName = search.getLName();
        System.out.println(search.toString());
      //  System.out.println(lName);

        System.out.println(pName);
        System.out.println("XD");
        // your logic next
        return pName;
    }









}