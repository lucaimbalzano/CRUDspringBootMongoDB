package com.imbalzano.luca.repository;

import com.imbalzano.luca.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface IUserRepository extends MongoRepository<User, Integer> {

}
