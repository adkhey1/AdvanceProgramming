package com.example.advanceprogramming.auth.service;


import com.example.advanceprogramming.auth.model.UserModel;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    /**
     * check entries from login
     *
     * @param email exist Username in Database
     * @param password input Password
     * @param model
     * @return wetten = successful login; login = incorrect entries
     */

    public String login(String email, String password, Model model) {

        if (userNotExist(email)) {
            model.addAttribute("login", "User does not exists");
        } else {
            if (isValidPassword(email, password)) {
                return "redirect:/app";  //for progress: redirect
            } else {
                model.addAttribute("login", "Username and Password do not match");
            }
        }
        return "login";
    }

    /**
     * checks the existence of the user name
     *
     * @param email
     * @return true = user not exists; false = user exists
     */
    public boolean userNotExist(String email) {
        String userEmail = userRepository.findByEmail(email).getEmail();

        if(userEmail.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Validate if passwords are equal
     *
     * @param email input of User
     * @param password  first input of password
     * @return true = equal passwords / false = unequal passwords
     */
    private boolean isValidPassword(String email, String password) {

        String userPassword = userRepository.findByEmail(email).getPassword();
        if(userPassword.equals(password)){
            return true;
        }
        /*
        List<UserModel> users = userRepository.findAll();

        for (UserModel user : users) {
            if(user.getPassword().equals(password)) {
                return true;
            }
        }
        */
        return false;
    }
}

