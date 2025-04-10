package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.scm.config.OAuthAuthenticationSuccessHandler;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler;

    private Logger logger= LoggerFactory.getLogger(ContactController.class);
    
    @Autowired
    private ContactService contactService;
    
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    private List<Contact> byUser;

    ContactController(OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler) {
        this.OAuthAuthenticationSuccessHandler = OAuthAuthenticationSuccessHandler;
    }
    // add contact page: handler
    @RequestMapping("/add") //default GET
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
       
        model.addAttribute("contactForm", contactForm);        
        return "user/add_contact";
    }
    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult rBindingResult, Authentication authentication,HttpSession session){
        // process the form data
        String username = Helper.getEmailOfLoggedInUser(authentication);
        //validate the data
        if(rBindingResult.hasErrors()){
            session.setAttribute("message",Message.builder().content("Please correct the errors below!").type(MessageType.red).build());
            return "user/add_contact";
        }
        //form--> contact
        User user = userService.getUserByEmail(username);
      
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setFavourite(contactForm.isFavourite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
         //set the contact picture url
         if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), fileName); 
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(fileName);

         }
        contactService.save(contact);
       

        //set message to be displayed on the view
        session.setAttribute("message",  Message.builder().content("Contact added successfully!").type(MessageType.green).build());
       
      
        System.out.println(contactForm);
        return "redirect:/user/contacts/add";
    }

    //view contacts
    @RequestMapping
    public String viewContacts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = AppConstants.PAGE_SIZE + "") int size, @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String direction, Model model,Authentication authentication){

        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);


        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());
        
        return "user/contacts";
    }

    //search handler
    @RequestMapping("/search")
    public String searchHandler( @ModelAttribute ContactSearchForm contactSearchForm,@RequestParam(defaultValue = AppConstants.PAGE_SIZE + "") int size, 
    @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "name")String sortBy,
    @RequestParam(defaultValue = "asc")String direction, 
    Model model, Authentication authentication){

        logger.info("field {} keyword {}",contactSearchForm.getField(),contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
         pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

    

        return "user/search";
    }
    
        // delete contact
        @RequestMapping("/delete/{contactId}")
        public String deleteContact(@PathVariable String contactId,
        HttpSession session ){

            contactService.delete(contactId);
            logger.info("contactId{} deleted", contactId);

            session.setAttribute("message", 
            Message.builder()
            .content("Contact is deleted successfully!!")
            .type(MessageType.green)
            .build()
            );
            return "redirect:/user/contacts";
        }

        //update contact form view
        @GetMapping("/view/{contactId}")
        public String updataContactFormView(
            @PathVariable String contactId, Model model, Authentication authentication) {
               var contact =  contactService.getById(contactId);

               ContactForm contactForm = new ContactForm();
               contactForm.setName(contact.getName());
               contactForm.setEmail(contact.getEmail());
               contactForm.setPhoneNumber(contact.getPhoneNumber());
               contactForm.setAddress(contact.getAddress());
               contactForm.setDescription(contact.getDescription());
               contactForm.setFavourite(contact.isFavourite());
               contactForm.setWebsiteLink(contact.getWebsiteLink());
               contactForm.setLinkedInLink(contact.getLinkedInLink());
                contactForm.setPicture(contact.getPicture());
               model.addAttribute("contactForm", contactForm);
               model.addAttribute(contactId, contactId);
                return "user/update_contact_view";
            
            }

            @PostMapping("/update/{contactId}")
            public String updateContact(@PathVariable String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult, Model model){

                // update the contact
                if(bindingResult.hasErrors()){
                    return "/user/update_contact_view"; 
                }
                var con = contactService.getById(contactId);
                con.setId(contactId);
                con.setName(contactForm.getName());
                con.setEmail(contactForm.getEmail());
                con.setPhoneNumber(contactForm.getPhoneNumber());
                con.setAddress(contactForm.getAddress());
                con.setDescription(contactForm.getDescription());
                con.setFavourite(contactForm.isFavourite());
                con.setWebsiteLink(contactForm.getWebsiteLink());
                con.setLinkedInLink(contactForm.getLinkedInLink());
              
                //process image:
               if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
                String fileName = UUID.randomUUID().toString();
                String  imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
                con.setCloudinaryImagePublicId(fileName);
                con.setPicture(imageUrl);
                contactForm.setPicture(imageUrl);
               }

                var updateCon = contactService.update(con);
                logger.info("updated contact {}", updateCon);
                model.addAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());
                    return "redirect:/user/contacts";
            }
    
}
