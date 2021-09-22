package com.imbalzano.luca.repository;

import com.imbalzano.luca.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IUserRepository extends MongoRepository<User, Integer> {

}
