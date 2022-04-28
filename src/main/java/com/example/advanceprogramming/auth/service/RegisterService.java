package com.example.advanceprogramming.auth.service;


import com.example.advanceprogramming.auth.model.UserModel;
import com.example.advanceprogramming.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RegisterService {

    @Autowired
    UserRepository userRepository;

    /**
     * @param user      all input for UserModel
     * @param password2 input password (second Password)
     * @param model
     * @return register = incorrect entries / redirect:wetten = successful regist
     */

    public String registrieren(UserModel user, String password2, Model model) {

        //for each different case, an if method is used to see what happens.


        //user with the same email already exists
        if (userExist(user.getEmail())) {
            model.addAttribute("register", "Email always exists");
            return "register";
        }

        //validate E-Mail
        if (!isEmail(user.getEmail())) {
            model.addAttribute("register", "Check your Email -> it needs an '@' and an '.'");
            return "register";
        }


        //Passwords are similar
        if (isSimilarPassword(user.getPassword(), password2)) {


            //Spring Security bin ich mir nicht sicher
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);


            userRepository.save(user);
            return "redirect:/app";
        } else {
            model.addAttribute("register", "Your passwords were not identical!");
        }
        return "register";
    }

    /**
     * Check if user exists in User table
     *
     * @param userEmail to search for
     * @return true = user exists / false = user does not exist
     */
    public boolean userExist(String userEmail) {
        if (!userEmail.isEmpty()) {
            return true;
        }
        return false; //eventuell true und false umgekehrt
    }

    /**
     * Validate if passwords are equal
     *
     * @param password  first input of password
     * @param password2 second input of password
     * @return true = equal passwords / false = unequal passwords
     */
    private boolean isSimilarPassword(String password, String password2) {
        return password.equals(password2);
    }

    /**
     * Validate email address
     *
     * @param email to validate
     * @return true = valid email / false = invalid email
     */
    private boolean isEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}