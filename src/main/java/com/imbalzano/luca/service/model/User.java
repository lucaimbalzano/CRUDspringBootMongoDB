package com.imbalzano.luca.service.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "user")
public class User {
        @Id
        private org.bson.types.ObjectId _id;
        private String first_name;
        private String last_name;
        private String email;
        private String gender;
        private String ip_address;
        private int age;
        private String address;

}

//{
//        "_id" : ObjectId("614b12979b5224f41bf7d4f6"),
//        "first_name" : "Doll",
//        "last_name" : "Sabbins",
//        "email" : "dsabbins9@behance.net",
//        "gender" : "Polygender",
//        "ip_address" : "248.101.17.132",
//        "age" : 82,
//        "address" : "28 Independence Place"
//        }