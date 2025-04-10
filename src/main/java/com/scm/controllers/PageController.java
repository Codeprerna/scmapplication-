package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        //sending data to view
        model.addAttribute("name","Substring");
        model.addAttribute("youtubeChannel","Learn Code");
        return "home";
    }
    
    //about route
    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About page loading");
        return "about";
    }
    //about services

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }

    //contact page
    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    @GetMapping("/login")
    public String login() {
        return new String("login");
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        //default data
        
        model.addAttribute("userForm", userForm);
        return "register";
    }
     
    
    //processing register
    @PostMapping("/do-register")
    public String processRegister( @Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session){ // this attribute will automatically create object and assign values into it
        System.out.println("Processing Registration");
        System.out.println(userForm);
        //fetch form data
        //validate form data
        if(rBindingResult.hasErrors()){
            return "register";
        }
        // save to database
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://img.freepik.com/free-psd/contact-icon-illustration-isolated_23-2151903337.jpg")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://img.freepik.com/free-psd/contact-icon-illustration-isolated_23-2151903337.jpg");

        User savedUser = userService.saveUser(user); 
        System.out.println("user saved: ");

        // add the message
        Message message = Message.builder().content("Registration successful").type(MessageType.green).build();
        session.setAttribute("message",message);

        // redirect to login page
        return "redirect:/register";
    }
}
