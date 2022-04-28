package com.example.advanceprogramming.auth.security;


import com.example.advanceprogramming.auth.model.UserModel;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;



public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    private UserModel user;


    //Load User by email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(username);

        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserSecurity(user);

    }
}
