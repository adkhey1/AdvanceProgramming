package com.example.advanceprogramming.auth.controller;

import com.example.advanceprogramming.auth.model.UserModel;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.advanceprogramming.auth.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;
    @Autowired
    UserRepository userRepository;

    /**
     * Create a new dokument from type UserModel by inputs
     *
     * input all Request Parameters
     * @return register by incorrect entries (repeated register.html) / redirekt:wetten goes to wetten.html
     */
    @PostMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,
                           @RequestParam String password, @RequestParam String password2, Model model) {

        int userID = userRepository.findAll().size() + 1;
        UserModel userModel = new UserModel(userID, firstName, lastName, email, password);

        //return "app" or "register"
        return registerService.registrieren(userModel, password2, model);
    }

}
