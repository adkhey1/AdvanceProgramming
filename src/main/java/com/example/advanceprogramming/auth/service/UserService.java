package com.example.advanceprogramming.auth.service;


import com.example.advanceprogramming.auth.model.UserModel;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;


}