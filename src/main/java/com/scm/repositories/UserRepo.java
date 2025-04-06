package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;


 // user for database interactions 
// Explaination: can be used directly in Controller but layered architeture Controller--> Services --> Repo
@Repository
public interface UserRepo extends JpaRepository<User, String> {
 // <Entity, datatype of id>
// extra methods dp relatedoperations
// custom query methods
// custom finder methods
Optional<User> findByEmail(String email);
Optional<User> findByEmailAndPassword(String email, String password);
}
