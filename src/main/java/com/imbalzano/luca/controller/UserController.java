package com.imbalzano.luca.controller;

import com.imbalzano.luca.Status;
import com.imbalzano.luca.repository.IUserRepository;
import com.imbalzano.luca.service.model.User;
import com.imbalzano.luca.transformer.UserResponseApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class UserController {

    @Autowired
    private IUserRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private UserResponseApi userResponseApi = new UserResponseApi();

    @PostMapping("/addUser")
    public UserResponseApi saveUser(User u){
       try{
           repository.save(u);

           System.out.println("#######++-- User Saved --++#######");
           System.out.println(u.toString());
           System.out.println("#######++-- ------------ --++#######");
           userResponseApi.setStatus(Status.OKAY);
           userResponseApi.setMessage("Success: User "+u.getFirst_name()+" added correctly!");
           return userResponseApi;
//           return "okay";


       }catch(Exception e){
           System.out.println("#######++-- Error occurred: "+e.getMessage()+" --++#######");
           e.printStackTrace();
           System.out.println("#######++-- End of Error Message --++#######");
          userResponseApi.setStatus(Status.INTERNAL_SERVER_ERROR);
           userResponseApi.setMessage("Error occurred: "+e.getMessage());
           userResponseApi.setError("Internal Server Error");
           return userResponseApi;
//           return "err";
       }
    }

    @GetMapping("/getAllUsers")
    public Map<Object,Object> getAllUsers(){
        Map<Object,Object> listUser = new HashMap<>();
       listUser.put("message", "Request load with success!");
       listUser.put("status", 200);
       listUser.put("body", repository.findAll());
       return listUser;
    }

    @GetMapping("/getUserByEmail/{email}")
    public UserResponseApi getUserByEmail(@PathVariable String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(email));
        List<User> users = mongoTemplate.find(query, User.class);
        for(User u: users){
        System.out.println("#######++-- User Found --++#######");
        System.out.println(u.toString());
        System.out.println("#######++-- ------------ --++#######");
        userResponseApi.setStatus(Status.OKAY);
        userResponseApi.setMessage("Success: User " + u.getFirst_name() + " found with success!");
        return userResponseApi;
        }
        System.out.println("#######++-- User Not Found --++#######");
        userResponseApi.setStatus(Status.NOT_FOUND);
        userResponseApi.setMessage("Error: User not found");
        return userResponseApi;
    }

    @PutMapping("/updateUser/{email}")
    public UserResponseApi updateUser(@PathVariable String email, @RequestBody User userRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(email));
        List<User> users = mongoTemplate.find(query, User.class);
        for(User u: users){
            userRequest.setFirst_name(u.getFirst_name());
            userRequest.setLast_name(u.getLast_name());
            userRequest.setAddress(u.getAddress());
            userRequest.setAge(u.getAge());
            userRequest.setEmail(u.getEmail());
            userRequest.setGender(u.getGender());
            repository.save(userRequest);
            System.out.println("#######++-- User Updated --++#######");
            System.out.println(u.toString());
            System.out.println("#######++-- ------------ --++#######");
            userResponseApi.setStatus(Status.OKAY);
            userResponseApi.setMessage("Success: User " + u.getFirst_name() + " updated with success!");
            return userResponseApi;
        }
        System.out.println("#######++-- Error while updating --++#######");
        userResponseApi.setStatus(Status.INTERNAL_SERVER_ERROR);
        userResponseApi.setMessage("Error occurred: User not updated");
        return userResponseApi;
    }

    @DeleteMapping("/deleteUser/{email}")
    public UserResponseApi deleteUser(@PathVariable String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex(email));
        List<User> users = mongoTemplate.find(query, User.class);
        for(User u: users){
            repository.delete(u);
            System.out.println("#######++-- User deleted --++#######");
            userResponseApi.setStatus(Status.OKAY);
            userResponseApi.setMessage("Success: User " + u.getFirst_name() + " deleted with success!");
            return userResponseApi;
        }
        System.out.println("#######++-- User Not Found --++#######");
        userResponseApi.setStatus(Status.NOT_FOUND);
        userResponseApi.setMessage("Error: User not found");
        return userResponseApi;
    }


}
