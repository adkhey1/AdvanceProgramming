package com.example.advanceprogramming.auth.controller;

import com.example.advanceprogramming.auth.model.UserModel;

import com.example.advanceprogramming.auth.repository.UserRepository;
import com.example.advanceprogramming.auth.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginService loginService;

    @GetMapping
    public String index() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {

        return loginService.login(email, password, model);
    }
}