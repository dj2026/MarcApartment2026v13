package com.example.MarcApartment.controller;

import com.example.MarcApartment.model.Person;
import com.example.MarcApartment.model.Owner;
import com.example.MarcApartment.model.Reviewer;
import com.example.MarcApartment.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@CrossOrigin(origins = "http://localhost:5173")
public class PersonRestController {private final PersonService personService;

    public PersonRestController(PersonService personService) {this.personService = personService;}
    
    @GetMapping("/all") public List<Person> getAll() {return personService.getAllPersons();}
    @PostMapping("/save") public Person save(@RequestBody Person person) {return personService.savePerson(person);}
    @GetMapping("/owners/all") public List<Owner> getAllOwners() {return personService.getAllOwners();}
    @PostMapping("/owners/save") public Owner saveOwner(@RequestBody Owner owner) {return personService.saveOwner(owner);}
    @GetMapping("/reviewers/all") public List<Reviewer> getAllReviewers() {return personService.getAllReviewers();}
    @PostMapping("/reviewers/save") public Reviewer saveReviewer(@RequestBody Reviewer reviewer) {return personService.saveReviewer(reviewer);}
}