package com.example.advanceprogramming.auth.controller;



import com.example.advanceprogramming.auth.model.User;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Start Page
    @GetMapping({"/home","/",""})
    public String viewHomePage(){
        return "index";
    }

    //Register page
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "register_page";
    }

    @PostMapping("/registering")
    public String registerUser(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);

        return "registered";


    }

    @GetMapping("/user_dashboard")
    public String userDashboard (Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName(); //only for testing porpoise
        model.addAttribute("test",currentPrincipalName); //only for testing porpoise

        return"user_dashboard";
    }


    @GetMapping("/test")
    String test(Model model){
        model.addAttribute("something","testtttt");
        return "test";
    }

}
