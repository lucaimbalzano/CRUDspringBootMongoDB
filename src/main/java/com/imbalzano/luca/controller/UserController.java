package com.imbalzano.luca.controller;

import com.imbalzano.luca.Status;
import com.imbalzano.luca.repository.IUserRepository;
import com.imbalzano.luca.service.model.User;
import com.imbalzano.luca.transformer.UserResponseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    @Autowired
    private IUserRepository repository;

    private UserResponseApi userResponseApi = new UserResponseApi();

    @PostMapping("/addUser")
    public UserResponseApi saveUser(User u){
       try{
           repository.save(u);
           System.out.println("#######++-- User Saved --++#######");
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
           userResponseApi.setError(e.toString());
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
}
