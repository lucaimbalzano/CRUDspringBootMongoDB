package com.imbalzano.luca.controller;

import com.imbalzano.luca.Status;
import com.imbalzano.luca.repository.IUserRepository;
import com.imbalzano.luca.service.model.User;
import com.imbalzano.luca.transformer.UserResponseApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
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

    //CRUD using @Query
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

    //TODO
    @GetMapping("getUserByAgeDesc/{age}")
    public Map<Object,Object>  getUserByAgeDesc(@PathVariable(value="age") Integer age){
        //Group
        GroupOperation groupOperation = Aggregation.group("age").count().as("age");
        //MatchOperation
        MatchOperation matchOperation = Aggregation.match(new Criteria("age").is(age));
        //SortOperation
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "age"));
        //Aggregation
        Aggregation aggregation = Aggregation.newAggregation(groupOperation, matchOperation, sortOperation);
        AggregationResults aggregationResults = mongoTemplate.aggregate(aggregation, "user", User.class);
        Map<Object,Object> response = new HashMap<>();
        Map<Object, Object> objectObjectMap =  aggregationResults.getMappedResults().isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with "+age+"yo not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", aggregationResults.getMappedResults());
        return response;
    }
    @GetMapping("getUserByName/{first_name}")
    public Map<Object,Object>  getUserByAge(@PathVariable(value="first_name") String name){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByFirstName(name);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user "+name+" not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    @GetMapping("getByAgeBetween/{minAge}/{maxAge}")
    public Map<Object,Object>  getByAgeBetween(@PathVariable(value="minAge") int minAge,
                                                     @PathVariable(value="maxAge") int maxAge){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByAgeBetween(minAge,maxAge);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with age between "+minAge+" and "+maxAge+" not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }


    @GetMapping("getUserByAgeAndGender/{age}/{gender}")
    public Map<Object,Object>  getUserByAgeAndGender(@PathVariable(value="age") Integer age,
                                            @PathVariable(value="gender") String gender){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByAgeAndGender(age,gender);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with properties "+gender+" and "+age+"yo not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    @GetMapping("getByAgeNameAndGender/{age}/{gender}/{first_name}")
    public Map<Object,Object>  getByAgeNameAndGender(@PathVariable(value="age") Integer age,
                                            @PathVariable(value="gender") String gender,
                                            @PathVariable(value="first_name") String first_name){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByAgeNameAndGender(age,gender,first_name);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with properties  "+first_name+" "+gender+" and "+age+"yo not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    @GetMapping("getByGender/{gender}")
    public Map<Object,Object>  getByAgeNameAndGender(@PathVariable(value="gender") String gender){
        Map<Object,Object> response = new HashMap<>();
        List<User> users = repository.findByGender(gender);
        Map<Object, Object> objectObjectMap = users.isEmpty() ? getStatusAndMessageResponseGeneral(response, "[❌]  Error occurred: user with "+gender+" not found",404):getStatusAndMessageResponseGeneral(response, "[✅] User found with success!", 200) ;
        response.put("body", users);
        return response;
    }

    //CRUD using Query and Criteria

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

    /*   UTILITIES   */
    private  Map<Object,Object> getStatusAndMessageResponseGeneral(Map<Object,Object> response, String message, int status){
        response.put("message", message);
        response.put("status", status);
        return response;
    }


}
