package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class); 
    
    @Autowired
    private UserService userService;

    
    //user dashboard page
    @RequestMapping(value="/dashboard") // by default get method
    public String userDashboard(){
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value="/profile") // by default get method
    public String userProfile(Model model,Authentication authentication){
       
        return "user/profile";
    }

    // user add contacts page
    // user view contacts page
    // user edit contact
    // user view contacts page



}
