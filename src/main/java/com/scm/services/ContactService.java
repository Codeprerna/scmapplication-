package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
//save Contacts
Contact save(Contact contact);

//update Contact
Contact update(Contact contact);

//get Contacts
List<Contact> getAll();

// get Contact by id
Contact getById(String id);

// DELETE Contact
void delete(String id);

// search Contacts
Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user);

Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user);

Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, User user);

//GET Contacts by userId
List<Contact> getByUserId(String userId);

Page<Contact> getByUser(User user, int page , int size, String sortField, String sortDirection);
}


