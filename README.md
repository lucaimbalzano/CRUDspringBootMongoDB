# CRUDspringBootMongoDB

## @Query annotation


>   @Query("{'age': ?0}")
>    List<User> findByAge(Integer age);

>    @Query("{'gender': ?0}")
>    List<User> findByGender(String gender);

>    @Query("{'first_name': {'$regex': '?0'}}")
>    List<User> findByFirstName(String name);

>    @Query("{'age': ?0, 'gender': ?1 }")
>    List<User> findByAgeAndGender(Integer age, String gender);

>    @Query(value="{'age': ?0 }", fields ="{'gender': 1, 'first_name':1}")
>    List<User> findByAgeNameAndGender(Integer age, String gender, String name);

>    @Query("{age:{'$gt' : ?0, '$lt': ?1}}")
>    List<User> findByAgeBetween(int minAge, int maxAge);



- Sample Mongo Collection
```
{
            "_id": {
                "timestamp": 1632309911,
                "date": "2021-09-22T11:25:11.000+00:00"
            },
            "first_name": "Alfie",
            "last_name": "Ruler",
            "email": "aruler1c@umn.edu",
            "gender": "Female",
            "ip_address": "90.248.238.82",
            "age": 44,
            "address": "58 Eggendart Lane"
        }
```
   
 ## MongoTemplate
   
    @GetMapping("/getUserByEmail/{email}")
     public UserResponseApi getUserByEmail(@PathVariable String email);
     --------------------------------------------------------
     @PutMapping("/updateUser/{email}")
      public UserResponseApi updateUser(@PathVariable String email, @RequestBody User userRequest);
      --------------------------------------------------------
     @PostMapping("/addUser")
      public UserResponseApi saveUser(User u)
      --------------------------------------------------------
     @DeleteMapping("/deleteUser/{email}")
      public UserResponseApi deleteUser(@PathVariable String email) 
      