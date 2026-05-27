package com.example.MarcApartment.model;

import jakarta.persistence.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.util.HashMap;
import java.util.Map;

@Entity @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DynamoDbBean 
public class Person {
    @Id private String id; private String name; private String email; private String operation; private int age;

    @Transient private Map<String, Object> extraAttributes = new HashMap<>();
    public Person() {}
    public Person(String name, String email, String operation) {this.name = name; this.email = email; this.operation = operation;}

    @DynamoDbPartitionKey @DynamoDbAttribute("djmarc") public String getId() {return id;} public void setId(String id) {this.id = id;}
    @DynamoDbSortKey  @DynamoDbAttribute("dj") public String getOperation() {return operation;} public void setOperation(String operation) {this.operation = operation;}
    @DynamoDbAttribute("name") public String getName() {return name;}  public void setName(String name) {this.name = name;}
    @DynamoDbAttribute("email") public String getEmail() {return email;} public void setEmail(String email) {this.email = email;}
    @DynamoDbAttribute("age") public int getAge() {return age;} public void setAge(int age) {this.age = age;}
    @DynamoDbIgnore  public Map<String, Object> getExtraAttributes() {return extraAttributes;} public void setExtraAttributes(Map<String, Object> extraAttributes) {this.extraAttributes = (extraAttributes != null) ? extraAttributes : new HashMap<>();}
}