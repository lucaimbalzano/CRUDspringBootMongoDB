package com.imbalzano.luca.repository;

import com.imbalzano.luca.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends MongoRepository<User, Integer> {

    @Query("{'age': ?0}")
    List<User> findByAge(Integer age);

    @Query("{'age': ?0, 'gender': ?1 }")
    List<User> findByAgeAndGender(Integer age, String gender);
}
