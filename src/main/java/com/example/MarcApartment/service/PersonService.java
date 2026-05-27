package com.example.MarcApartment.service;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    @Autowired private OwnerRepository ownerRepository;
    @Autowired private ReviewerRepository reviewerRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private DynamoDbTable<Person> personTable;

    public List<Person> getAllPersons() { return personRepository.findAll(); }

    @jakarta.transaction.Transactional
    public Person savePerson(Person person) {
        String manualId = person.getId();
        prepareHybridData(person, "GENERIC_OP");
        Person saved = personRepository.save(person);
        if (manualId != null && !manualId.isEmpty()) {saved.setId(manualId);}
        syncWithDynamo(saved);
        return saved;
    }

    public List<Owner> getAllOwners() { return ownerRepository.findAll(); }

    @jakarta.transaction.Transactional
    public Owner saveOwner(Owner owner) {
        String manualId = owner.getId(); 
        prepareHybridData(owner, "INITIAL_LOAD");
        Owner saved = ownerRepository.save(owner);
        if (manualId != null && !manualId.isEmpty()) {saved.setId(manualId);}
        System.out.println("DEBUG: Guardat a H2. ID final per a AWS: " + saved.getId());
        syncWithDynamo(saved);
        return saved;
    }

    public List<Reviewer> getAllReviewers() { return reviewerRepository.findAll(); }

    @jakarta.transaction.Transactional
    public Reviewer saveReviewer(Reviewer reviewer) {
        String manualId = reviewer.getId();
        prepareHybridData(reviewer, "REVIEWER_OP");
        Reviewer saved = reviewerRepository.save(reviewer);
        if (manualId != null && !manualId.isEmpty()) {saved.setId(manualId);}
        syncWithDynamo(saved);
        return saved;
    }

    private void prepareHybridData(Person p, String defaultOp) {if (p.getId() == null || p.getId().isEmpty()) {p.setId(UUID.randomUUID().toString());}
        if (p.getOperation() == null || p.getOperation().isEmpty()) {p.setOperation(defaultOp);}
    }

    private void syncWithDynamo(Person p) {
        try {
            System.out.println("--- AWS SYNC START ---");
            Person cleanPerson = new Person();
            cleanPerson.setId(p.getId());
            cleanPerson.setName(p.getName());
            cleanPerson.setEmail(p.getEmail());
            cleanPerson.setOperation(p.getOperation());
            cleanPerson.setAge(p.getAge()); 
            if (p.getExtraAttributes() != null) {cleanPerson.setExtraAttributes(new java.util.HashMap<>(p.getExtraAttributes()));}
            personTable.putItem(cleanPerson);
            System.out.println("--- AWS SYNC OK [ID: " + cleanPerson.getId() + "] ---");
            
        } catch (Throwable t) { 
            System.err.println("!!! AWS SYNC ERROR: " + t.getMessage());
        }
    }
}