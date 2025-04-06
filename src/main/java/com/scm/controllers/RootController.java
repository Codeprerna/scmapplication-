package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.repositories.ContactRepo;
import com.scm.services.UserService;

@ControllerAdvice  // EXEXCUTE FOR ALL REQUESTS - add user in every method
public class RootController {

    
    @Autowired  // constructor injection production based application
    private UserService userService;
    
    @Autowired
    private ContactRepo contactRepo;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

   
@ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication == null){
            return;
        }
        System.out.println("adding logged in user information");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}",username);

        User user = userService.getUserByEmail(username);
        System.out.println(user); 
        System.out.println(user.getName());
        System.out.println(user.getEmail());


        long contactCount = contactRepo.countByUser(user);
        model.addAttribute("contactCount", contactCount);
        model.addAttribute("loggedInUser", user);
    
        }
        
}
