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

    @Query("{'gender': ?0}")
    List<User> findByGender(String gender);

    @Query("{'first_name': {'$regex': '?0'}}")
    List<User> findByFirstName(String name);

    @Query("{'age': ?0, 'gender': ?1 }")
    List<User> findByAgeAndGender(Integer age, String gender);

    @Query(value="{'age': ?0 }", fields ="{'gender': 1, 'first_name':1}")
    List<User> findByAgeNameAndGender(Integer age, String gender, String name);

    @Query("{age:{'$gt' : ?0, '$lt': ?1}}")
    List<User> findByAgeBetween(int minAge, int maxAge);


}
