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

import java.nio.file.Path;
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
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findAll();
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: users not found",404):getStatusAndMessageResponseGeneral(response, "[✅] Users found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    @GetMapping("getUserByAge/{age}")
    public Map<Object,Object>  getUserByAge(@PathVariable(value="age") Integer age){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByAge(age);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with "+age+"yo not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    @GetMapping("getUserByAgeAndGender/{age}/{gender}")
    public Map<Object,Object>  getUserByAge(@PathVariable(value="age") Integer age,
                                            @PathVariable(value="gender") String gender){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByAgeAndGender(age,gender);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with properties "+gender+" and "+age+"yo not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
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



    private  Map<Object,Object> getStatusAndMessageResponseGeneral(Map<Object,Object> response, String message, int status){
        response.put("message", message);
        response.put("status", status);
        return response;
    }


}
