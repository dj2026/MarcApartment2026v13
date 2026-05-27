package com.example.MarcApartment.utils;

import com.example.MarcApartment.model.*;
import com.example.MarcApartment.repository.*;
import com.example.MarcApartment.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component 
public class PopulateDB {

    @Autowired ApartmentService apartmentService; 
    @Autowired SchoolRepository schoolRepository; 
    @Autowired ReviewerRepository reviewerRepository; 
    @Autowired ReviewRepository reviewRepository;  
    @Autowired OwnerRepository ownerRepository;
    @Autowired PropertyContractRepository contractRepository;

    @Transactional
    public int populateAll(int qty) {Owner marc = ownerRepository.findAll().stream().findFirst().orElseGet(() -> ownerRepository.save(new Owner("Marc", "marc@apartments.com", "POPULATE_OP", 30, true, false, "12345678X", LocalDate.now(), 100)));
        
        School escola = schoolRepository.findAll().stream().findFirst().orElse(null);

        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        String[] furnish = {"furnished", "semi-furnished", "unfurnished"};
        List<Apartment> apartments = new ArrayList<>();

        for (int i = 0; i < qty; i++) {Apartment apt; int typeChoice = rnd.nextInt(3); 
            if (typeChoice == 0) {House h = new House(); h.setPropertyType("House");  h.setYardSize(rnd.nextInt(50, 300)); h.setPool(rnd.nextBoolean() ? "yes" : "no"); h.setOwner(marc); apt = h;} 
            else if (typeChoice == 1) {Duplex d = new Duplex(); d.setPropertyType("Duplex"); d.setBalcony("large"); d.setElevator(rnd.nextBoolean() ? "yes" : "no"); d.setOwner(marc); apt = d;} 
            else {apt = new Apartment(); apt.setPropertyType("Apartment"); apt.setOwner(marc);}
            apt.setArea(rnd.nextInt(40, 400));
            apt.setBedrooms(rnd.nextInt(1, 5));
            apt.setBathrooms(rnd.nextInt(1, 3));
            apt.setAirconditioning(rnd.nextBoolean() ? "yes" : "no");
            apt.setFurnishingstatus(furnish[rnd.nextInt(furnish.length)]);
            apt.setStories(rnd.nextInt(1, 3));
            apt.setMainroad("yes");
            apt.setGuestroom("no");
            apt.setBasement("no");
            apt.setHotwaterheating("yes");
            apt.setParking(rnd.nextInt(0, 3));
            apt.setPrefarea("yes");
            apt.setDescription("Propietat generada automàticament.");
            apt.calcularPreuAutomatic();
            if (escola != null) {apt.addSchool(escola);}
            Apartment saved = apartmentService.createApartment(apt);apartments.add(saved);
            PropertyContract pc = new PropertyContract(marc, saved, "CONT-AUTO-" + rnd.nextInt(10000), LocalDate.now(), saved.getPrice().doubleValue());  contractRepository.save(pc);
        }
        
        return apartments.size();
    }
}